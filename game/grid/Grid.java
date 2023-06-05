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
    private final int rows;
    private final int columns;
    private final ArrayList<ArrayList<Cell>> plate;
    final private ArrayList<Boat> boats = new ArrayList<Boat>();
    static public final String[] POSITIONS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    private final Player player;

    public ArrayList<Boat> getBoats() {
        return boats;
    }

    public ArrayList<Cell> getAllCells() {
        ArrayList<Cell> output = new ArrayList<>();

        for (ArrayList<Cell> cells : plate) {
            output.addAll(cells);
        }

        return output;
    }

    public ArrayList<Cell> getDiscoveredCells() {
        ArrayList<Cell> output = new ArrayList<>();
        for (Cell cell : getAllCells()) {
            if (cell.isDiscovered()) output.add(cell);
        }
        return output;
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

    /**
     * Returns the number of rows in the grid.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
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
     * Returns the 2D array of cells representing the grid.
     *
     * @return The 2D array of cells.
     */
    public ArrayList<ArrayList<Cell>> getPlate() {
        return plate;
    }

    public Player getPlayer() { return player; }

    /**
     * Récupère une cellule aléatoire qui satisfait certaines conditions.
     *
     * @return Une cellule aléatoire qui respecte les conditions.
     */
    public Cell getRandomCell(ArrayList<Cell> cells) {
        return cells.get((new Random().nextInt(cells.size())));
    }

    /**
     * Places a boat randomly on the grid.
     *
     */
    private void placeRandomBoat(Boat.Model model) {
        ArrayList<Vector> vectors = DirectionService.get4Vectors();

        while (true) {
            // Get a random cell on the grid
            Cell cell = getRandomCell(getEmptyCells());
            Collections.shuffle(vectors);

            // Check if all points can be filled
            for (Vector vector : DirectionService.get4Vectors()) {
                try {
                    ArrayList<Cell> cells = new ArrayList<>();

                    for (int i = 0; i < model.getLength(); i++) {
                        Cell c = DiscoveryService.findCellInGrid(
                                cell.getRow() + i * vector.getRow(),
                                cell.getColumn() + i * vector.getColumn(),
                                this
                        );

                        cells.add(c);
//                        cells.add(new Cell(
//                                reference.getRow() + i * vector.getRow(),
//                                reference.getColumn() + i * vector.getColumn()
//                        ));
                    }

                    Boat boat = new Boat(model, cells);

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

    public String toString(boolean showBoats) {
        StringBuilder output = new StringBuilder("\n" + player.getUsername() + "\n");

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                // Display the letter on the first column
                if (j == 1 && i >= 1) output.append(POSITIONS[i - 1]).append(" ");

                // Display position on the first row
                if (i == 0) {
                    if (j == 0) output.append("\\ ");
                    if (j >= 1) {
                        output.append(j > 9 ? " " : "  ");
                        output.append(j).append(" ");
                    }
                } else if (j >= 1) {
                    Boat boat = DiscoveryService.findBoatWhichHasCell(plate.get(i-1).get(j-1), boats);
                    String label = boat != null ? boat.getLabel() : "-";

                    output.append(plate.get(i - 1).get(j - 1).isDiscovered() || showBoats ? "  " + label + " " : "  . ");
                }
            }

            output.append("\n");
        }

        return output.toString();
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

    public int getAllAliveBoatCells() {
        int counter = 0;

        for (Boat b : boats) {
            counter += b.getCoordinatesNotHit().size();
        }

        return counter;
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

        if (!DirectionService.isInGrid(target, this)) return "You are out of the grid";

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
    public boolean allBoatAreSunk(){
        return boats.stream().allMatch(Boat::isSunk);
    }
}