import java.util.ArrayList;
import java.util.Random;

public class Grid extends ArrayList<ArrayList<Cell>> {
    private static final int MAX_SIZE = 10;
    private static final int MIN_SIZE = 3;
    private static Grid instance;

    private int length;
    private int width;
    private Hero player;
    private Cell currentCell;

    private Grid(int length, int width) {
        this.length = Math.max(length, MIN_SIZE);
        this.width = Math.max(width, MIN_SIZE);
    }

    public static Grid generateGrid(Hero hero) {
        Random random = new Random();
        int randomLength = random.nextInt(MAX_SIZE) + 1;
        int randomWidth = random.nextInt(MAX_SIZE) + 1;
        instance = new Grid(randomLength, randomWidth);
        instance.initializeGrid(hero);
        return instance;
    }

    private void initializeGrid(Hero hero) {
        for (int i = 0; i < length; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new Cell(i, j));
            }
            this.add(row);
        }

        addToGrid(hero);
    }

    private void addToGrid(Hero hero) {
        int sanctuaryCount = 2;
        int enemyCount = 4;

        while (sanctuaryCount > 0) {
            place(CellEntityType.SANCTUARY);
            sanctuaryCount--;
        }

        while (enemyCount > 0) {
            place(CellEntityType.ENEMY);
            enemyCount--;
        }

        place(CellEntityType.PORTAL);

        if (hero.profession.equals("Warrior")) {
            player = new Warrior(hero.hname, hero.experience, hero.level);
        } else if (hero.profession.equals("Rogue")) {
            player = new Rogue(hero.hname, hero.experience, hero.level);
        } else if (hero.profession.equals("Mage")) {
            player = new Mage(hero.hname, hero.experience, hero.level);
        }

        setCurrentCell(place(CellEntityType.PLAYER));

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = this.get(i).get(j);
                if (cell.getType() == null) {
                    cell.setType(CellEntityType.VOID);
                }
            }
        }
    }

    private Cell place(CellEntityType type) {
        Random random = new Random();
        while (true) {
            int x = random.nextInt(length);
            int y = random.nextInt(width);
            Cell cell = this.get(x).get(y);
            if (cell.getType() == null) {
                cell.setType(type);
                return cell;
            }
        }
    }

    public void goNorth() throws ImpossibleMove {
        moveTo(currentCell.getX() - 1, currentCell.getY());
    }

    public void goSouth() throws ImpossibleMove {
        moveTo(currentCell.getX() + 1, currentCell.getY());
    }

    public void goWest() throws ImpossibleMove {
        moveTo(currentCell.getX(), currentCell.getY() - 1);
    }

    public void goEast() throws ImpossibleMove {
        moveTo(currentCell.getX(), currentCell.getY() + 1);
    }

    private void moveTo(int x, int y) {
        if (x >= 0 && x < length && y >= 0 && y < width) {
            setCurrentCell(this.get(x).get(y));
        } else {
            throw new ImpossibleMove("Impossible move!");
        }
    }

    public void setLength(int length) {
        this.length = length;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        if (this.currentCell != null) {
            this.currentCell.setType(CellEntityType.VOID);
        }
        this.currentCell = currentCell;
        currentCell.setVisited(true);
    }

    public void displayGrid() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = this.get(i).get(j);
                if (cell == currentCell) {
                    System.out.print("P ");
                } else if (cell.isVisited()) {
                    System.out.print("V ");
                } else if (!cell.isVisited()) {
                    System.out.print("N ");
                }
            }
            System.out.println();
        }
    }

    public void hardcodeGrid() {
        this.clear();
        this.setLength(5);
        this.setWidth(5);

        for (int i = 0; i < length; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new Cell(i, j));
            }
            this.add(row);
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                this.get(i).get(j).setType(CellEntityType.VOID);
                this.get(i).get(j).setVisited(false);
            }
        }

        this.get(0).get(0).setType(CellEntityType.PLAYER);
        this.get(0).get(3).setType(CellEntityType.SANCTUARY);
        this.get(1).get(3).setType(CellEntityType.SANCTUARY);
        this.get(2).get(0).setType(CellEntityType.SANCTUARY);
        this.get(3).get(4).setType(CellEntityType.ENEMY);
        this.get(4).get(3).setType(CellEntityType.SANCTUARY);
        this.get(4).get(4).setType(CellEntityType.PORTAL);

        setCurrentCell(this.get(0).get(0));
    }
}
