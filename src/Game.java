import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private static Game instance;
    private ArrayList<Account> accounts;
    private Grid grid;
    private Account currentAccount;
    private Hero currentHero;
    private int level = 1;
    private Scanner scanner = new Scanner(System.in);

    private Game(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public static Game getInstance(ArrayList<Account> accounts) {
        if (instance == null) {
            instance = new Game(accounts);
        }
        return instance;
    }

    public void run() {
        System.out.println("Welcome to League of Warriors!");
        authenticate(scanner);

        selectHero(scanner);
        System.out.println("Generating game grid...");
        grid = Grid.generateGrid(currentHero);
        grid.hardcodeGrid();

        runNewGame(grid);

        scanner.close();
    }

    private void authenticate(Scanner scanner) {
        System.out.println("Please log in with your email and password.");

        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            for (Account account : accounts) {
                Credentials credentials = account.getInformation().getCredentials();
                if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                    currentAccount = account;
                    System.out.println("Login successful! Welcome, " + account.getInformation().getName() + "!");
                    return;
                }
            }

            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private void selectHero(Scanner scanner) {
        ArrayList<Hero> heroes = currentAccount.getHeroes();

        System.out.println("Select a hero to play:");
        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            System.out.println((i + 1) + ". " + hero.getProfession() + " - " + hero.hname);
        }

        while (true) {
            try {
                System.out.print("Enter hero number: ");
                int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

                if (choice >= 0 && choice < heroes.size()) {
                    currentHero = heroes.get(choice);
                    System.out.println("You selected: " + currentHero.getProfession() + " - " + currentHero.hname);
                    return;
                } else {
                    System.out.println("Invalid choice. Please select a valid hero.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void processCurrentCell() {
        Cell cell = grid.getCurrentCell();
        switch (cell.getType()) {
            case ENEMY:
                System.out.println("You encountered an enemy!");
                System.out.println(currentHero.hname + " stats: Strength " + currentHero.strength +
                        ", Charisma " + currentHero.charisma +
                        ", Dexterity " + currentHero.dexterity);
                battle(new Enemy());
                break;
            case SANCTUARY:
                System.out.println("You found a sanctuary! Recovering life and mana.");
                currentHero.regenLife(20);
                currentHero.regenMana(20);
                break;
            case PORTAL:
                System.out.println("You found a portal!");

                int expGained = level * 5;
                currentHero.addExperience(expGained);
                System.out.println("You gained " + expGained + " experience points!");

                level++;
                System.out.println("Congratulations! You've advanced to level " + level + ".");

                int gamesCompleted = currentAccount.getGamesCompleted();
                gamesCompleted++;
                currentAccount.setGamesCompleted(gamesCompleted);
                System.out.println("Total games completed: " + gamesCompleted);

                currentHero.regenLife(Entity.MAX_LIFE);
                currentHero.regenMana(Entity.MAX_MANA);

                System.out.println("Moving to the next game...");
                System.out.println("Generating game grid...");
                grid = Grid.generateGrid(currentHero);
                runNewGame(grid);
                break;
            case VOID:
                System.out.println("This cell is empty.");
                break;
        }
    }

    private void battle(Enemy enemy) {
        Scanner scanner = new Scanner(System.in);
        boolean inBattle = true;
        Random random = new Random();

        while (inBattle) {
            System.out.println("Your life: " + currentHero.getLife() + ", Mana: " + currentHero.getMana());
            System.out.println("Enemy life: " + enemy.getLife() + ", Mana: " + enemy.getMana());

            System.out.println("Choose action: ATTACK (a/A) or USE ABILITY (u/U)");
            String action = scanner.nextLine().trim().toUpperCase();

            try {
                switch (action) {
                    case "A":
                        enemy.receiveDamage(currentHero.getDamage());
                        break;
                    case "U":
                        if (currentHero.getAbilities().isEmpty()) {
                            System.out.println("No abilities available. Attacking normally...");
                            enemy.receiveDamage(currentHero.getDamage());
                            break;
                        }

                        System.out.println("Select an ability:");
                        ArrayList<Spell> abilities = currentHero.getAbilities();
                        for (int i = 0; i < abilities.size(); i++) {
                            System.out.println((i + 1) + ". " + abilities.get(i));
                        }

                        int choice = -1;
                        while (choice < 0 || choice >= abilities.size()) {
                            try {
                                System.out.print("Enter ability number: ");
                                choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a number.");
                            }

                            if (choice < 0 || choice >= abilities.size()) {
                                System.out.println("Invalid choice. Please select a valid ability.");
                            }
                        }

                        boolean abilityUsed = currentHero.useAbility(abilities.get(choice), enemy);
                        if (!abilityUsed) {
                            System.out.println("Defaulting to normal attack...");
                            enemy.receiveDamage(currentHero.getDamage());
                        }
                        break;
                    default:
                        throw new InvalidCommandException("Invalid action: " + action + ". Please choose ATTACK or USE ABILITY.");
                }

                if (enemy.isDead()) {
                    System.out.println("Enemy defeated!");
                    random = new Random();
                    currentHero.addExperience(random.nextInt(51) + 20);
                    currentHero.regenLife(currentHero.life);
                    currentHero.regenMana(Entity.MAX_MANA);
                    inBattle = false;
                } else {
                    System.out.println("Enemy's turn to attack!");
                    ArrayList<Spell> enemyAbilities = enemy.getAbilities();

                    boolean useAbility = random.nextBoolean();
                    if (useAbility && !enemyAbilities.isEmpty()) {
                        Spell ability = enemyAbilities.get(random.nextInt(enemyAbilities.size()));

                        if (enemy.getMana() >= ability.getCost()) {
                            enemy.useAbility(ability, currentHero);
                            System.out.println("Enemy used " + ability.getName() + "!");
                        } else {
                            currentHero.receiveDamage(enemy.getDamage());
                            System.out.println("Enemy attacked normally!");
                        }
                    } else {
                        currentHero.receiveDamage(enemy.getDamage());
                        System.out.println("Enemy attacked normally!");
                    }
                }
                if (currentHero.isDead()) {
                    System.out.println("You have been defeated. Game over.");
                    currentHero.regenLife(Entity.MAX_LIFE);
                    currentHero.regenMana(Entity.MAX_MANA);
                    selectHero(scanner);
                    System.out.println("Generating game grid...");
                    grid = Grid.generateGrid(currentHero);
                    runNewGame(grid);
                }
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void runNewGame(Grid grid) {
        boolean running = true;
        while (running) {
            try {
                grid.displayGrid();
                String command = scanner.nextLine().trim().toUpperCase();

                switch (command) {
                    case "N":
                        grid.goNorth();
                        break;
                    case "S":
                        grid.goSouth();
                        break;
                    case "E":
                        grid.goEast();
                        break;
                    case "W":
                        grid.goWest();
                        break;
                    case "Q":
                        System.out.println("Exiting the current game...");
                        currentHero.regenLife(Entity.MAX_LIFE);
                        currentHero.regenMana(Entity.MAX_MANA);
                        selectHero(scanner);
                        System.out.println("Generating game grid...");
                        grid = Grid.generateGrid(currentHero);
                        runNewGame(grid);
                        return;
                    default:
                        throw new InvalidCommandException("Invalid command: " + command);
                }

                processCurrentCell();
            } catch (InvalidCommandException | ImpossibleMove e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
