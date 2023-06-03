package game.grid;


import services.DirectionService;
import socket.server.Player;
import game.boat.Boat;

import java.util.ArrayList;
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
    final private static ArrayList<Boat> boats = new ArrayList<Boat>(5);
    static public final String[] POSITIONS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    private final Player player;

    /**
     * Constructs a Grid_Alex object with the specified player, cells, rows, and columns.
     *
     * @param player   The player associated with the grid.
     * @param rows     The number of rows in the grid.
     * @param columns  The number of columns in the grid.
     */
    public Grid(Player player, int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.player = player;
        this.grid = new Cell[rows][columns];

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

    public Player getPlayer() { return player; }

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
        var random = new Random();

        while (randomCell == null) {
            int x = random.nextInt(getColumns()); // Génère un indice de ligne aléatoire
            int y = random.nextInt(getRows()); // Génère un indice de colonne aléatoire

            // Vérifie si les coordonnées de la cellule générée se trouvent à l'intérieur des limites de la grille
            if (x > 0 && x < maxRows && y > 0 && y < maxColumns) {
                Cell currentCell = grid[x][y]; // Obtient la cellule actuelle aux coordonnées générées
                boolean hasNullNeighbor = false; // Indique si la cellule actuelle a une cellule voisine avec un bateau null

                // Vérifie chaque cellule voisine
                for (int[] vector : DirectionService.getFullVectors()) {
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
                for (int[] vector : DirectionService.getFullVectors()) {
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
        for (int[] vector : DirectionService.getFullVectors()) {
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
        int[][] vectors = DirectionService.getVectors(); // Get the available vectors

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
                        currentCell.setBoat(boat);
                        boat.addCell(new Coordinate(x, y, false));
                    }
                    return;
                }

                canPlace = true;
            }
        }
    }

    /**
     * place a boat in the grid
     * you have to choose a direction ( north, south, east, west ) and point in the grid
     * at the point in the grid, the function will check if every position with the lenght of the boat are free
     * if all position are free, it will place the boat
     * @param boat
     * @param x
     * @param y
     * @param direction
     * @return
     */
    public boolean placeBoat(Boat boat, Integer x, Integer y, String direction) {
        Cell cell;
        int[] vector = DirectionService.getDirectionVector(direction); // Get the available vectors

        cell = getCell(x,y);

        // Check if all points can be filled
        boolean canPlace = true;
        for (int i = 0; i < boat.getModel().getLength(); i++) {
            x = cell.getColumnIndex() + i * vector[0];
            y = cell.getRowIndex() + i * vector[1];
            if (!canSetupCell(x, y)) {
                return false;
            }
        }

        if (canPlace) {
            // Place the boat
            for (int i = 0; i < boat.getModel().getLength(); i++) {
                x = cell.getColumnIndex() + i * vector[0];
                y = cell.getRowIndex() + i * vector[1];
                cell.setBoat(boat);
                boat.addCell(new Coordinate(x, y, false));
                boat.isPlaced = true;
            }
            return true;
        }
        return false;
    }

    //----------------------------------------------------------------
    @Override
    public String toString() {
        String output = "\n";

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                // Affiche la lettre sur la première colonne
                if (j == 1) {
                    if (i >= 1) {
                        output += POSITIONS[i - 1] + " ";
                    }
                }

                // Affiche la position ( chiffre ) sur la première ligne
                if (i == 0) {
                    if (j == 0) {
                        output += "\\ ";
                    }
                    if (j >= 1) {
                        output += " " + j + " ";
                    }
                } else if (j >= 1 && (grid[i - 1][j - 1] == null)) {
                    output += " . ";
                } else if (j >= 1 && (grid[i - 1][j - 1] != null)) {
                    output += " " + grid[i - 1][j - 1] + " ";
                }
            }

            if (i - 1 > 0 && i - 1 < Boat.Model.values().length) {
                Boat.Model tb = Boat.Model.values()[i - 1];
                output += "          [" + tb.getName() + "] boat : " + tb.getName() + ", length : " + tb.getLength();
            }

            output += "\n";
        }

        return output;
    }

    /**
     * Display the grid by printing its contents.
     */
    public String show() {
        System.out.println(grid.length);
        String output = "\n";

        // iterate on each row
        for (int i = 0; i < rows; i++) {
            // iterate on each column
            for (int j = 0; j < columns; j++) {
                if (j == 1) {
                    // Display the letter on the first column
                    if (i >= 1) output += POSITIONS[i - 1] + " ";
                } else if (i == 0) {
                    // Display the position (number) on the first row
                    if (j == 0) output += "\\ ";
                    else output += " " + j + " ";
                }

                // Display the grid contents
                else if (j >= 1 && i >= 1) {
                    if (grid[i - 1][j - 1] == null) output += " - ";
                    else output += " " + grid[i - 1][j - 1] + " ";
                }
            }
            output += "\n";
        }

        return output;
    }
    //----------------------------------------------------------------

    /**
     *
     * @param length
     * @return return a boat from the list by his length
     */
    public Boat getBoatWithLength(int length){
        for (Boat boat : boats) {
            if (boat.getModel().getLength() == length && !boat.isPlaced) {
                return boat;
            }
        }
        return null;
    }

    /**
     * return true if all boats are placed in the grid
     * @return state of the grid
     */
    public boolean isConfigured(){
        int counter = 0;
        System.out.println("myboats = " + boats.size());
        for (Boat boat : boats) {
            System.out.println(boat.getModel() + " " + boat.isPlaced + " " + boat.coordinates);
            if (boat.isPlaced) {
                counter++;
            }
        }
        return counter == boats.size();
    }

    /**
     * return a cell from a position in the grid
     * @param x
     * @param y
     * @return
     */
    public Cell getCellWithPosition(int x, int y){
        return grid[x][y];
    }

    /***
     * fire on a position of the grid, update the grid where a fire is land and return a message for the user if
     * he touch a boat
     * he already hit the position
     * he sink a boat
     * @param x
     * @param y
     * @return
     */
    public String fire(int x, int y) {
        Cell valuePosition = getCellWithPosition(x,y);
        if(valuePosition != null){
            if(valuePosition.isDiscovered()){
                return "you already hit this position";
            }
            if(valuePosition.hasBoat()){
                valuePosition.setDiscovered();
                valuePosition.getBoat().getCoordinates().stream()
                        .filter(coord -> coord.getRowIndex() == x && coord.getColumnIndex() == y)
                        .forEach(coord -> coord.setSink(true));
                if(valuePosition.getBoat().isSink()){
                    return "You just sink the boat " + valuePosition.getBoat().getModel().getName();
                }else{
                    return "You hit the boat" + valuePosition.getBoat().getModel().getName();
                }

            }else{
                valuePosition.setDiscovered();
                return "Sadly, it's only water...";
            }
        }
       return "You are out of the grid";
    }

    /**
     * Return true if all of the boat in the myboats List are Sink
     * @return
     */
    public boolean allBoatAreSink(){
        return boats.stream().allMatch(Boat::isSink);
    }
}