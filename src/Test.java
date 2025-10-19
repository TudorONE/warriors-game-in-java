import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<Account> accounts = JsonInput.deserializeAccounts();

        for (Account account : accounts) {
            System.out.println("Account Name: " + account.getInformation().getName());
            System.out.println("Country: " + account.getInformation().getCountry());
            System.out.println("Email: " + account.getInformation().getCredentials().getEmail());
            System.out.println("Games Completed: " + account.getGamesCompleted());

            System.out.println("Favorite Games:");
            for (String game : account.getInformation().getFavoriteGames()) {
                System.out.println(" - " + game);
            }

            System.out.println("Heroes:");
            for (Hero hero : account.getHeroes()) {
                System.out.println(" * " + hero.getProfession() + " - " + hero.getHname() +
                        " (Level: " + hero.getLevel() + ", XP: " + hero.getExperience() + ")");
            }
        }

        Game game = Game.getInstance(accounts);
        game.run();
    }
}
