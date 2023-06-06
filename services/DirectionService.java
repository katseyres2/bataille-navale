package services;

import game.boat.Boat;
import game.grid.Cell;
import game.grid.Grid;
import game.grid.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class DirectionService {
    /**
     * Returns an array with north/south/east/west vectors.
     *
     * @return The array of vectors.
     */
    public static ArrayList<Vector> get4Vectors() {
        ArrayList<Vector> output = new ArrayList<>();
        output.add(new Vector(-1, 0));
        output.add(new Vector(0, -1));
        output.add(new Vector(0, 1));
        output.add(new Vector(1, 0));
        return output;
    }

    /**
     * Gets the vector corresponding to the specified direction.
     *
     * @param direction The direction string.
     * @return The vector representing the direction.
     */
    public static int[] getDirectionVector(String direction) {
        int[] output;

        switch (direction.toLowerCase()) {
            case "n":
            case "north":
            case "nord":
                output = new int[] {-1, 0};
                break;
            case "s":
            case "south":
            case "sud":
                output = new int[] {1, 0};
                break;
            case "w":
            case "west":
            case "ouest":
            case "o":
                output = new int[] {0, -1};
                break;
            case "e":
            case "east":
            case "est":
                output = new int[] {0, 1};
                break;
            default:
                output = null;
                break;
        }
        return output;
    }

    /**
     * Returns a random vector from the provided list of vectors.
     *
     * @param vectors The list of vectors.
     * @return A random vector.
     */
    private static Vector getRandomVector(ArrayList<Vector> vectors) {
        int i = (new Random()).nextInt(vectors.size());
        return vectors.get(i);
    }

    /**
     * return an array with all vectors
     * @return
     */
    public static ArrayList<Vector> get8Vectors() {
        var output = new ArrayList<Vector>();
        output.addAll(get4Vectors());
        output.add(new Vector(-1,-1));
        output.add(new Vector(-1,1));
        output.add(new Vector(1,-1));
        output.add(new Vector(1,1));
        return output;
    }

    /**
     * Checks if all coordinates are on the same line.
     *
     * @param coordinates The list of coordinates.
     * @return {@code true} if all coordinates are on the same line, {@code false} otherwise.
     */
    private static boolean onTheSameLine(@NotNull ArrayList<Cell> coordinates) {
        if (coordinates.size() == 0) return false;
        int reference = coordinates.get(0).getRow();

        for (int i = 1; i < coordinates.size(); i++) {
            if (coordinates.get(i).getRow() != reference) return false;
        }

        return true;
    }

    /**
     * Checks if all coordinates are on the same column.
     *
     * @param coordinates The list of coordinates.
     * @return {@code true} if all coordinates are on the same column, {@code false} otherwise.
     */
    private static boolean onTheSameColumn(@NotNull ArrayList<Cell> coordinates) {
        if (coordinates.size() == 0) return false;
        int reference = coordinates.get(0).getColumn();

        for (int i = 1; i < coordinates.size(); i++) {
            if (coordinates.get(i).getColumn() != reference) return false;
        }

        return true;
    }

    /**
     * Checks if the coordinates are vertical neighbors.
     *
     * @param coordinates The list of coordinates.
     * @return {@code true} if the coordinates are vertical neighbors, {@code false} otherwise.
     */
    private static boolean areVerticalNeighbors(@NotNull ArrayList<Cell> coordinates) {
        coordinates.sort((a,b) -> Math.min(a.getRow(), b.getRow()));

        for (int i = 0; i < coordinates.size(); i++) {
            if (i == coordinates.size() - 1) break;
            if (coordinates.get(i).getRow() + 1 != coordinates.get(i+1).getRow()) return false;
        }

        return true;
    }

    /**
     * Checks if the given coordinates are horizontal neighbors.
     *
     * @param coordinates The list of coordinates.
     * @return {@code true} if the coordinates are horizontal neighbors, {@code false} otherwise.
     */
    private static boolean areHorizontalNeighbors(@NotNull ArrayList<Cell> coordinates) {
        coordinates.sort((a,b) -> Math.min(a.getColumn(), b.getColumn()));

        for (int i = 0; i < coordinates.size(); i++) {
            if (i == coordinates.size() - 1) break;
            if (coordinates.get(i).getColumn() + 1 != coordinates.get(i+1).getColumn()) return false;
        }

        return true;
    }

    /**
     * Checks if the given coordinates are valid.
     *
     * @param coordinates The list of coordinates.
     * @return {@code true} if the coordinates are valid, {@code false} otherwise.
     */
    public static boolean areValidCoordinates(ArrayList<Cell> coordinates) {
        for (Cell c : coordinates) {
            if (c == null) return false;
        }

        return (onTheSameColumn(coordinates) && areVerticalNeighbors(coordinates))
                || (onTheSameLine(coordinates) && areHorizontalNeighbors(coordinates));
    }

    /**
     * Checks if the boat is placed along the border of the grid.
     *
     * @param boat The boat object.
     * @param grid The grid object.
     * @return {@code true} if the boat is along the border, {@code false} otherwise.
     */
    public static boolean isBoatAlongBorder(@NotNull Boat boat, Grid grid) {
        int counter = 0;

        for (Cell c : boat.getCoordinates()) {
            counter += isCellAlongBorder(c, grid) ? 1 : 0;
        }

        return counter == boat.getLength();
    }

    /**
     * Checks if the given cell is along the border of the grid.
     *
     * @param cell The cell to check.
     * @param grid The grid object.
     * @return {@code true} if the cell is along the border, {@code false} otherwise.
     */
    private static boolean isCellAlongBorder(@NotNull Cell cell, Grid grid) {
        return cell.getRow() == 0 ||
                cell.getColumn() == 0 ||
                cell.getRow() == grid.getColumns() - 1 ||
                cell.getColumn() == grid.getColumns() - 1;
    }

    /**
     * Checks if the boat is placed adjacent to any other boat on the grid.
     *
     * @param boat The boat object.
     * @param grid The grid object.
     * @return {@code true} if the boat is adjacent to another boat, {@code false} otherwise.
     */
    public static boolean isBoatAlongOther(Boat boat, @NotNull Grid grid) {
        ArrayList<Cell> filledCells = new ArrayList<>();

        // Fill the array with all cells containing a boat
        for (Boat b : grid.getBoats()) {
            filledCells.addAll(b.getCoordinates());
        }

        // Iterate over each cell of the boat
        for (Cell cellReference : boat.getCoordinates()) {
            // For each cell, check all neighbors with the vectors
            for (Vector v : get4Vectors()) {
                int nextColumn = cellReference.getColumn() + v.getColumn();
                int nextRow = cellReference.getRow() + v.getRow();

                // Check if the neighbor cell contains a boat
                if (DiscoveryService.findCell(nextRow, nextColumn, filledCells) != null) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the given cell is within the boundaries of the grid.
     *
     * @param cell The cell to check.
     * @param grid The grid object.
     * @return {@code true} if the cell is within the grid, {@code false} otherwise.
     */
    public static boolean isCellInGrid(Cell cell, @NotNull Grid grid) {
        if (cell == null || cell.getRow() >= grid.getRows() || cell.getRow() < 0) {
            return false;
        }

        return cell.getColumn() < grid.getColumns() && cell.getColumn() >= 0;
    }

    /**
     * Checks if the boat is completely within the grid boundaries.
     *
     * @param boat The boat object.
     * @param grid The grid object.
     * @return {@code true} if the boat is within the grid, {@code false} otherwise.
     */
    public static boolean isBoatInGrid(@NotNull Boat boat, Grid grid) {
        for (Cell c : boat.getCoordinates()) {
            if (!isCellInGrid(c, grid)) {
                return false;
            }
        }
        return true;
    }
}
