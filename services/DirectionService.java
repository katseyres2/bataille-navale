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
     * return array with vector north / south / east / west
     * @return
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
     * get the vector of the direction wanted
     * @param direction_vectors
     * @return
     */
    public static int[] getDirectionVector(String direction_vectors) {
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

    private static boolean onTheSameLine(@NotNull ArrayList<Cell> coordinates) {
        if (coordinates.size() == 0) return false;
        int reference = coordinates.get(0).getRow();

        for (int i = 1; i < coordinates.size(); i++) {
            if (coordinates.get(i).getRow() != reference) return false;
        }

        return true;
    }

    private static boolean onTheSameColumn(@NotNull ArrayList<Cell> coordinates) {
        if (coordinates.size() == 0) return false;
        int reference = coordinates.get(0).getColumn();

        for (int i = 1; i < coordinates.size(); i++) {
            if (coordinates.get(i).getColumn() != reference) return false;
        }

        return true;
    }

    private static boolean areVerticalNeighbors(@NotNull ArrayList<Cell> coordinates) {
        coordinates.sort((a,b) -> Math.min(a.getRow(), b.getRow()));

        for (int i = 0; i < coordinates.size(); i++) {
            if (i == coordinates.size() - 1) break;
            if (coordinates.get(i).getRow() + 1 != coordinates.get(i+1).getRow()) return false;
        }

        return true;
    }

    private static boolean areHorizontalNeighbors(@NotNull ArrayList<Cell> coordinates) {
        coordinates.sort((a,b) -> Math.min(a.getColumn(), b.getColumn()));

        for (int i = 0; i < coordinates.size(); i++) {
            if (i == coordinates.size() - 1) break;
            if (coordinates.get(i).getColumn() + 1 != coordinates.get(i+1).getColumn()) return false;
        }

        return true;
    }

    public static boolean areValidCoordinates(ArrayList<Cell> coordinates) {
        return (onTheSameColumn(coordinates) && areVerticalNeighbors(coordinates)) ||
               (onTheSameLine(coordinates) && areHorizontalNeighbors(coordinates));
    }

    public static boolean isBoatAlongBorder(@NotNull Boat boat, Grid grid) {
        int counter = 0;

        for (Cell c : boat.getCoordinates()) {
            counter += isCellAlongBorder(c, grid) ? 1 : 0;
        }

        return counter == boat.getLength();
    }

    private static boolean isCellAlongBorder(@NotNull Cell cell, Grid grid) {
        return cell.getRow() == 0 ||
               cell.getColumn() == 0 ||
               cell.getRow() == grid.getColumns() - 1 ||
               cell.getColumn() == grid.getColumns() - 1;
    }

    public static boolean isBoatAlongOther(Boat boat, @NotNull Grid grid) {
        ArrayList<Cell> filledCells = new ArrayList<>();

        // fill the array with all cells with a boat
        for (Boat b : grid.getBoats()) {
            filledCells.addAll(b.getCoordinates());
        }

        // iterate on each boat cell
        for (Cell cellReference : boat.getCoordinates()) {
            // for each cell, check all neighbors with the vectors
            for (Vector v : get4Vectors()) {
                int nextColumn = cellReference.getColumn() + v.getColumn();
                int nextRow = cellReference.getRow() + v.getRow();

                if (DiscoveryService.findCell(nextRow, nextColumn, filledCells) != null) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isInGrid(@NotNull Cell cell, @NotNull Grid grid) {
        if (cell.getRow() >= grid.getRows() || cell.getRow() < 0) {
            return false;
        }

        if (cell.getColumn() >= grid.getColumns() || cell.getColumn() < 0) {
            return false;
        }

        return true;
    }

    public static boolean isBoatInGrid(Boat boat, Grid grid) {
        for (Cell c : boat.getCoordinates()) {
            if (!isInGrid(c, grid)) {
                return false;
            }
        }
        return true;
    }
}
