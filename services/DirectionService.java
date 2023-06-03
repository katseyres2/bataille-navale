package services;

import game.grid.Cell;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class DirectionService {
    /**
     * return array with vector north / south / east / west
     * @return
     */
    public static int[][] getVectors() {
        return new int[][] {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
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

    public static int[] getRandomVector(int[][] vectors) {
        int i = (new Random()).nextInt(vectors.length);
        return vectors[i];
    }

    /**
     * return an array with all vectors
     * @return
     */
    public static int[][] getFullVectors() {
        return new int[][] {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                {  0, -1 },            {  0, 1 },
                {  1, -1 }, {  1, 0 }, {  1, 1 }
        };
    }

    public static boolean onTheSameLine(@NotNull ArrayList<Cell> coordinates) {
        if (coordinates.size() == 0) return false;
        int reference = coordinates.get(0).getRowIndex();

        for (int i = 1; i < coordinates.size(); i++) {
            if (coordinates.get(i).getRowIndex() != reference) return false;
        }

        return true;
    }

    public static boolean onTheSameColumn(@NotNull ArrayList<Cell> coordinates) {
        if (coordinates.size() == 0) return false;
        int reference = coordinates.get(0).getColumnIndex();

        for (int i = 1; i < coordinates.size(); i++) {
            if (coordinates.get(i).getColumnIndex() != reference) return false;
        }

        return true;
    }

    public static boolean areVerticalNeighbors(@NotNull ArrayList<Cell> coordinates) {
        coordinates.sort((a,b) -> Math.min(a.getRowIndex(), b.getRowIndex()));

        for (int i = 0; i < coordinates.size(); i++) {
            if (i == coordinates.size() - 1) break;
            if (coordinates.get(i).getRowIndex() + 1 != coordinates.get(i+1).getRowIndex()) return false;
        }

        return true;
    }

    public static boolean areHorizontalNeighbors(@NotNull ArrayList<Cell> coordinates) {
        coordinates.sort((a,b) -> Math.min(a.getColumnIndex(), b.getColumnIndex()));

        for (int i = 0; i < coordinates.size(); i++) {
            if (i == coordinates.size() - 1) break;
            if (coordinates.get(i).getColumnIndex() + 1 != coordinates.get(i+1).getColumnIndex()) return false;
        }

        return true;
    }

    public static boolean areValidCoordinates(ArrayList<Cell> coordinates) {
        return (onTheSameColumn(coordinates) && areVerticalNeighbors(coordinates)) ||
               (onTheSameLine(coordinates) && areHorizontalNeighbors(coordinates));
    }
}
