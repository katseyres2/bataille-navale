package game.grid;

import game.boat.Boat;
import socket.server.Player;

import java.util.Random;

/**
 * Represents the grid used in the game for a specific player.
 */
public class Grid {
    public static final int DEFAULT_ROW_COUNT = 10;
    public static final int DEFAULT_COLUMN_COUNT = 10;

    private int rows;
    private int columns;
    private Cell[][] grid;
    private Player owner;

    //
    final private static int[][] VECTORS = getFullVectors();
    static final String[] POSITIONS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };

    private Random random;

    /**
     * Constructs a Grid_Alex object with the specified player, cells, rows, and columns.
     *
     * @param owner    The player owner.
     * @param rows     The number of rows in the grid.
     * @param columns  The number of columns in the grid.
     */
    public Grid(Player owner, int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new Cell[][]{};
        this.owner = owner;
    }

    public boolean isPlayerOwner(Player player) {
        return player == owner;
    }

    //----------------------------------------------------------------

    /**
     * Returns the number of rows in the grid.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Sets the number of rows in the grid.
     *
     * @param rows The number of rows to set.
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Returns the number of columns in the grid.
     *
     * @return The number of columns.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Sets the number of columns in the grid.
     *
     * @param columns The number of columns to set.
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Returns the 2D array of cells representing the grid.
     *
     * @return The 2D array of cells.
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * Sets the 2D array of cells representing the grid.
     *
     * @param grid The 2D array of cells to set.
     */
    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    /**
     * Sets up the specified cell at the given coordinate in the grid.
     *
     * @param coordinate The coordinate of the cell.
     * @param cell       The cell object to set.
     */
    public void setupCell(Coordinate coordinate, Cell cell) {
        getGrid()[coordinate.getX()][coordinate.getY()] = cell;
    }

    //----------------------------------------------------------------

    private static int[][] getFullVectors() {
        return new int[][] {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };
    }

    private static int[][] getVectors() {
        return new int[][] {
                { -1, 0 },
                { 0, -1 }, { 0, 1 },
                { 1, 0 },
        };
    }

    public int[] getRandomVector(int[][] vectors) {
        int i = random.nextInt(vectors.length);
        return vectors[i];
    }

    public int[] getDirectionVector(String direction_vectors) {
        int[] output;

        switch (direction_vectors.toLowerCase()) {
            case "n":
            case "north":
            case "nord":
                output = new int[] { -1, 0 };
                break;
            case "s":
            case "south":
            case "sud":
                output = new int[] { 1, 0 };
                break;
            case "w":
            case "west":
            case "ouest":
            case "o":
                output = new int[] { 0, -1 };
                break;
            case "e":
            case "east":
            case "est":
                output = new int[] { 0, 1 };
                break;
            default:
                output = null;
                break;
        }

        return output;
    }

    //----------------------------------------------------------------

    /**
     * Sets up a void grid by initializing cells with null values.
     */
    public void setupVoidGrid() {
        // Iterate over each column
        for (int x = 0; x < getColumns(); x++){
            // Iterate over each row
            for (int y = 0; y < getRows(); y++){
                // Set up a cell with coordinates (x, y) and a null value
                setupCell(new Coordinate(x, y), new Cell(x, y, null));
            }
        }
    }

    //----------------------------------------------------------------

    /**
     * Récupère une cellule aléatoire qui satisfait certaines conditions.
     *
     * @return Une cellule aléatoire qui respecte les conditions.
     */
    public Cell getRandomCell() {
        Cell randomCell = null; // Cellule aléatoire à retourner
        int maxRows = getColumns() - 1; // Indice maximum de ligne
        int maxColumns = getRows() - 1; // Indice maximum de colonne

        while (randomCell == null) {
            int x = random.nextInt(getColumns()); // Génère un indice de ligne aléatoire
            int y = random.nextInt(getRows()); // Génère un indice de colonne aléatoire

            // Vérifie si les coordonnées de la cellule générée se trouvent à l'intérieur des limites de la grille
            if (x > 0 && x < maxRows && y > 0 && y < maxColumns) {
                Cell currentCell = grid[x][y]; // Obtient la cellule actuelle aux coordonnées générées
                boolean hasNullNeighbor = false; // Indique si la cellule actuelle a une cellule voisine avec un bateau null

                // Vérifie chaque cellule voisine
                for (int[] vector : VECTORS) {
                    int neighborX = x + vector[0]; // Calcule la coordonnée X de la cellule voisine
                    int neighborY = y + vector[1]; // Calcule la coordonnée Y de la cellule voisine

                    // Si la cellule voisine a un bateau null, définit le drapeau et sort de la boucle
                    if (grid[neighborX][neighborY].getBoat() == null) {
                        hasNullNeighbor = true;
                        break;
                    }
                }

                // Si la cellule actuelle a un bateau non null et aucune cellule voisine null, l'assigne à randomCell
                if (currentCell.getBoat() != null && !hasNullNeighbor) {
                    randomCell = currentCell;
                }
            }
        }

        return randomCell;
    }

    public Cell getCell(int x, int y) {
        Cell randomCell = null; // Cellule aléatoire à retourner
        int maxRows = getColumns() - 1; // Indice maximum de ligne
        int maxColumns = getRows() - 1; // Indice maximum de colonne

        while (randomCell == null) {


            // Vérifie si les coordonnées de la cellule générée se trouvent à l'intérieur des limites de la grille
            if (x > 0 && x < maxRows && y > 0 && y < maxColumns) {
                Cell currentCell = grid[x][y]; // Obtient la cellule actuelle aux coordonnées générées
                boolean hasNullNeighbor = false; // Indique si la cellule actuelle a une cellule voisine avec un bateau null

                // Vérifie chaque cellule voisine
                for (int[] vector : VECTORS) {
                    int neighborX = x + vector[0]; // Calcule la coordonnée X de la cellule voisine
                    int neighborY = y + vector[1]; // Calcule la coordonnée Y de la cellule voisine

                    // Si la cellule voisine a un bateau null, définit le drapeau et sort de la boucle
                    if (grid[neighborX][neighborY].getBoat() == null) {
                        hasNullNeighbor = true;
                        break;
                    }
                }

                // Si la cellule actuelle a un bateau non null et aucune cellule voisine null, l'assigne à randomCell
                if (currentCell.getBoat() != null && !hasNullNeighbor) {
                    randomCell = currentCell;
                }
            }
        }

        return randomCell;
    }

    /**
     * Checks if a cell can be set up at the given coordinates.
     *
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return {@code true} if the cell can be set up, {@code false} otherwise.
     */
    public boolean canSetupCell(int x, int y) {
        // Check if the coordinates are within the grid boundaries
        if (x < 0 || x > getColumns() || y < 0 || y > getRows()) {
            // System.out.print("Overflow\n");
            return false;
        }

        // Check each neighboring cell
        for (int[] vector : VECTORS) {
            int neighborX = x + vector[0]; // Calculate the x-coordinate of the neighboring cell
            int neighborY = y + vector[1]; // Calculate the y-coordinate of the neighboring cell

            // Check if the neighboring cell is within the grid boundaries or if it's already occupied
            if (neighborX < 0 || neighborX > getColumns() || neighborY < 0 || neighborY > getRows() || grid[neighborX][neighborY] != null) {
                return false;
            }
        }

        return true;
    }

    //----------------------------------------------------------------

    /**
     * Places a boat randomly on the grid.
     *
     * @param length The length of the boat.
     * @param boat   The boat object to be placed.
     */
    public void placeRandomBoat(int length, Boat boat) {
        Cell cell;
        int[][] vectors = getVectors(); // Get the available vectors

        while (true) {
            cell = getRandomCell(); // Get a random cell on the grid

            // Check if all points can be filled
            boolean canPlace = true;
            for (int[] vector : vectors) {
                for (int i = 0; i < length; i++) {
                    int x = cell.getColumnIndex() + i * vector[0];
                    int y = cell.getRowIndex() + i * vector[1];

                    if (!canSetupCell(x, y)) {
                        canPlace = false;
                        break;
                    }
                }

                if (canPlace) {
                    // Place the boat
                    for (int i = 0; i < length; i++) {
                        int x = cell.getColumnIndex() + i * vector[0];
                        int y = cell.getRowIndex() + i * vector[1];
                        Cell currentCell = grid[y][x];
                        currentCell.setBoat(boat.getType());
                        boat.addCoordinate(new Coordinate(x, y, false));
                    }
                    return;
                }

                canPlace = true;
            }
        }
    }


    public void placeBoat(Boat boat, Integer x, Integer y, String direction) {
        Cell cell;
        int[] vector = getDirectionVector(direction); // Get the available vectors

        cell = getCell(x,y);

        // Check if all points can be filled
        boolean canPlace = true;
        for (int i = 0; i < boat.getType().length; i++) {
            x = cell.getColumnIndex() + i * vector[0];
            y = cell.getRowIndex() + i * vector[1];
            if (!canSetupCell(x, y)) {
                canPlace = false;
                break;
            }
        }

        if (canPlace) {
            // Place the boat
            for (int i = 0; i < boat.getType().length; i++) {
                x = cell.getColumnIndex() + i * vector[0];
                y = cell.getRowIndex() + i * vector[1];
                cell.setBoat(boat.getType());
                boat.addCoordinate(new Coordinate(x, y, false));
            }
        }

    }

    //----------------------------------------------------------------

    /**
     * Display the grid by printing its contents.
     */
    public void show() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                // Display the letter on the first column
                if (j == 1) {
                    if (i >= 1) {
                        sb.append(POSITIONS[i - 1]).append(" ");
                    }
                }
                // Display the position (number) on the first row
                else if (i == 0) {
                    if (j == 0) {
                        sb.append("\\ ");
                    } else {
                        sb.append(" ").append(j).append(" ");
                    }
                }
                // Display the grid contents
                else if (j >= 1) {
                    if (grid[i - 1][j - 1] == null) {
                        sb.append(" • ");
                    } else {
                        sb.append(" ").append(grid[i - 1][j - 1]).append(" ");
                    }
                }
            }
            sb.append("\n");
        }

        System.out.print(sb);
    }
    //----------------------------------------------------------------

    /**
     * Convert the first coord ( letter A to J ) into a int
     *
     * @param letter
     * @return the letter convert in integer
     */
    public int convertCoordLetter(String letter) {
        int coord = 0;
        for (int i = 0; i < POSITIONS.length; i++) {
            if (POSITIONS[i].equals(letter.toUpperCase())) {
                coord = i;
                break;
            }
        }
        return coord;
    }
}