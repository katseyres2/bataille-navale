package game.grid;


import org.jetbrains.annotations.NotNull;
import services.DirectionService;
import services.DiscoveryService;
import socket.server.Player;
import game.boat.Boat;

import java.util.*;

/**
 * Represents the grid used in the game for a specific player.
 */
public class Grid {

    public static final int DEFAULT_ROW_COUNT = 10;
    public static final int DEFAULT_COLUMN_COUNT = 10;
    private int rows;
    private int columns;
    private ArrayList<ArrayList<Cell>> plate;
    final private ArrayList<Boat> boats = new ArrayList<Boat>();
    static public final String[] POSITIONS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    private final Player player;

    public ArrayList<Boat> getBoats() {
        return boats;
    }

    public ArrayList<Cell> getEmptyCells() {
        ArrayList<Cell> emptyCells = new ArrayList<>();

        for (ArrayList<Cell> p : plate) {
            for (Cell c : p) {
                if (DiscoveryService.findCellInBoats(c.getRow(), c.getColumn(), boats) == null) {
                    emptyCells.add(c);
                }
            }
        }

        return emptyCells;
    }

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
        this.plate = new ArrayList<>();

        for (int row=0; row<rows; row++) {
            var tmp = new ArrayList<Cell>();
            for (int col=0; col<columns; col++) {
                tmp.add(new Cell(row,col));
            }
            plate.add(tmp);
        }
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
    public ArrayList<ArrayList<Cell>> getPlate() {
        return plate;
    }

    public Player getPlayer() { return player; }

    /**
     * Sets the 2D array of cells representing the grid.
     *
     * @param plate The 2D array of cells to set.
     */
    public void setPlate(ArrayList<ArrayList<Cell>> plate) {
        this.plate = plate;
    }

//    /**
//     * Sets up the specified cell at the given coordinate in the grid.
//     *
//     * @param coordinate The coordinate of the cell.
//     * @param cell       The cell object to set.
//     */
//    public void setupCell(Cell coordinate, Cell cell) {
//        getPlate().get(coordinate.getRowIndex()).get(coordinate.getColumnIndex()) = cell;
//    }

    //----------------------------------------------------------------

    /**
     * Récupère une cellule aléatoire qui satisfait certaines conditions.
     *
     * @return Une cellule aléatoire qui respecte les conditions.
     */
    public Cell getRandomCell(ArrayList<Cell> cells) {
        return cells.get((new Random().nextInt(cells.size() - 1)));
    }

//    public Cell getCell(int x, int y) {
//        Cell selectedCell = null;           // Cellule aléatoire à retourner
//        int maxRows = getRows() - 1;        // Indice maximum de ligne
//        int maxColumns = getColumns() - 1;  // Indice maximum de colonne
//
//        while (selectedCell == null) {
//            // Vérifie si les coordonnées de la cellule générée se trouvent à l'intérieur des limites de la grille
//            if (x > 0 && x < maxRows && y > 0 && y < maxColumns) {
//                Cell currentCell = grid[x][y]; // Obtient la cellule actuelle aux coordonnées générées
//                boolean hasNullNeighbor = false; // Indique si la cellule actuelle a une cellule voisine avec un bateau null
//
//                // Vérifie chaque cellule voisine
//                for (int[] vector : DirectionService.get8Vectors()) {
//                    int neighborX = x + vector[0]; // Calcule la coordonnée X de la cellule voisine
//                    int neighborY = y + vector[1]; // Calcule la coordonnée Y de la cellule voisine
//
//                    // Si la cellule voisine a un bateau null, définit le drapeau et sort de la boucle
//                    if (grid[neighborX][neighborY].getBoat() == null) {
//                        hasNullNeighbor = true;
//                        break;
//                    }
//                }
//
//                // Si la cellule actuelle a un bateau non null et aucune cellule voisine null, l'assigne à randomCell
//                if (currentCell.getBoat() != null && !hasNullNeighbor) {
//                    selectedCell = currentCell;
//                }
//            }
//        }
//
//        return selectedCell;
//    }

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
        for (Vector vector : DirectionService.get8Vectors()) {
            int neighborX = x + vector.getColumn(); // Calculate the x-coordinate of the neighboring cell
            int neighborY = y + vector.getRow(); // Calculate the y-coordinate of the neighboring cell

            // Check if the neighboring cell is within the grid boundaries or if it's already occupied
            if (neighborX < 0 || neighborX > getColumns() || neighborY < 0 || neighborY > getRows() || plate.get(neighborX).get(neighborY) != null) {
                return false;
            }
        }

        return true;
    }

    //----------------------------------------------------------------

    /**
     * Places a boat randomly on the grid.
     *
     */
    private void placeRandomBoat(Boat.Model model) {
        ArrayList<Vector> vectors = DirectionService.get4Vectors();

        while (true) {
            Cell cell = getRandomCell(getEmptyCells()); // Get a random cell on the grid
            Collections.shuffle(vectors);

            // Check if all points can be filled
            for (Vector vector : DirectionService.get4Vectors()) {
                try {
                    Boat boat = new Boat(model, cell, vector);

                    if (DirectionService.isBoatAlongBorder(boat, this)) {
//                        System.out.println("Along border");
                    } else if (DirectionService.isBoatAlongOther(boat, this)) {
//                        System.out.println("Along other");
                    } else if (!DirectionService.isBoatInGrid(boat, this)) {
//                        System.out.println("Boat not in grid");
                    } else {
                        System.out.println("New boat " + boat.getName() + " at " + boat.getCoordinates());
                        boats.add(boat);
                        return;
                    }
                } catch (InstantiationException e) {
//                    System.out.println("Error on boat instantiation " + e.getMessage());
                }
            }
        }
    }

    public void populateRandomly() {
        for (Boat.Model model : Boat.Model.values()) {
            placeRandomBoat(model);
        }
    }

//    /**
//     * place a boat in the grid
//     * you have to choose a direction ( north, south, east, west ) and point in the grid
//     * at the point in the grid, the function will check if every position with the lenght of the boat are free
//     * if all position are free, it will place the boat
//     * @param x
//     * @param y
//     * @param direction
//     * @return
//     */
//    public boolean placeBoat(Boat.Model model, Integer x, Integer y, String direction) {
//        Boat boat = null;
//        ArrayList<Cell> selectedCells = new ArrayList<>();
//        Cell cell;
//        int[] vector = DirectionService.getDirectionVector(direction); // Get the available vectors
//
//        cell = getCell(x,y);
//
//        for (int i = 0; i < model.getLength(); i++) {
//            x = cell.getColumnIndex() + i * vector[0];
//            y = cell.getRowIndex() + i * vector[1];
//            if (!canSetupCell(x, y)) {
//                return false;
//            }
//        }
//
//        // Place the boat
//        for (int i = 0; i < model.getLength(); i++) {
//            x = cell.getColumnIndex() + i * vector[0];
//            y = cell.getRowIndex() + i * vector[1];
//            selectedCells.add(new Cell(x, y));
//        }
//
//        try {
//            boat = new Boat(model, selectedCells);
//            boats.add(boat);
//        } catch (InstantiationException e) {
//            System.out.println("Error on boat instantiation " + e.getMessage());
//        }
//
//        return boat != null;
//    }

    //----------------------------------------------------------------

    public String toString(boolean showBoats) {
        String output = "\n";

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                // Affiche la lettre sur la première colonne
                if (j == 1 && i >= 1) output += POSITIONS[i - 1] + " ";

                // Affiche la position ( chiffre ) sur la première ligne
                if (i == 0) {
                    if (j == 0) output += "\\ ";
                    if (j >= 1) {
                        output += j > 9 ? " " : "  ";
                        output += j + " ";
                    }
                } else if (j >= 1) {
                    Boat boat = DiscoveryService.findBoatWhichHasCell(plate.get(i-1).get(j-1), boats);
                    String label = boat != null ? boat.getLabel() : "-";

                    output += plate.get(i-1).get(j-1).isDiscovered() || showBoats ? "  " + label + " " : "  . ";
                }
            }

//            if (i - 1 > 0 && i - 1 < Boat.Model.values().length) {
//                Boat.Model tb = Boat.Model.values()[i - 1];
//                output += "          [" + tb.getName() + "] boat : " + tb.getName() + ", length : " + tb.getLength();
//            }

            output += "\n";
        }

        return output;
    }

    /**
     * Display the grid by printing its contents.
     */
    public String show() {
        System.out.println(plate.size());
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
                    if (plate.get(i-1).get(j-1) == null) output += " - ";
                    else output += " " + plate.get(i-1).get(j-1) + " ";
                }
            }
            output += "\n";
        }

        return output;
    }

    /**
     * return true if all boats are placed in the grid
     * @return state of the grid
     */
    public boolean isConfigured(){
        return boats.size() == 5;
    }

    /**
     * return a cell from a position in the grid
     * @return Cell
     */
    public Cell getCellFromPosition(int row, int column){
        return plate.get(row).get(column);
    }

    public boolean isEmptyCell(Cell cell) {
        return isEmptyCell(cell.getRow(), cell.getColumn());
    }

    public boolean isEmptyCell(int row, int column) {
        return DiscoveryService.findCellInBoats(row, column, boats) != null;
    }

    /***
     * fire on a position of the grid, update the grid where a fire is land and return a message for the user if
     * he touches a boat
     * he already hit the position
     * he sinks a boat
     * @return String
     */
    public String fire(@NotNull Cell target) {
        if (target.isDiscovered()) return "you already hit this position";
        target.discover();

        if (!DirectionService.isInGrid(target, this)) return "You are out of the grid";;

        Boat boat = DiscoveryService.findBoatWhichHasCell(target, boats);
        if (boat == null) return "Sadly, it's only water...";

        for (Cell c : boat.getCoordinates()) {
            if (! c.isDiscovered()) {
                return "You hit the boat" + boat.getName();
            }
        }

        return "You just sink the boat " + boat.getName();
    }

    /**
     * Return true if all the boat in the myboats List are Sink
     * @return boolean
     */
    public boolean allBoatAreSink(){
        return boats.stream().allMatch(Boat::isSunk);
    }
}