package game.boat;

import game.grid.Cell;
import game.grid.Vector;
import org.jetbrains.annotations.NotNull;
import services.DirectionService;

import java.util.ArrayList;

public class Boat {

    public enum Model {
        AIRCRAFT_CARRIER("A", "aircraft carrier", 5),
        CRUISER("C", "cruiser", 4),
        SUBMARINE("S", "submarine", 3),
        DESTROYER("D", "destroyer", 3),
        WARSHIP("W", "warship", 2);

        private final String label;

        private final int length;
        private final String name;

        Model(String label, String name, int length) {
            this.label = label;
            this.name = name;
            this.length = length;
        }

        public int getLength() {
            return length;
        }

        public String getLabel() {
            return label;
        }

        public String getName() {
            return name;
        }
    }

    private final Model model;
    private final ArrayList<Cell> coordinates;


    public int getLength() {
        return model.getLength();
    }

    public String getLabel() {
        return model.label;
    }

    public String getName() {
        return model.name;
    }

    public boolean isSunk() {
        for (Cell c : coordinates) {
            if (!c.isDiscovered()) return false;
        }

        return true;
    }

    public Boat(@NotNull Model model, ArrayList<Cell> cells) throws InstantiationException {
        this.model = model;
        System.out.println(cells);

        if (model.length != cells.size() || !DirectionService.areValidCoordinates(cells)) {
            throw new InstantiationException();
        }

        this.coordinates = cells;
    }

    public ArrayList<Cell> getCoordinates() {
        return coordinates;
    }

    public ArrayList<Cell> getCoordinatesNotHit() {
        ArrayList<Cell> output = new ArrayList<>();

        for (Cell c : coordinates) {
            if (!c.isDiscovered()) output.add(c);
        }

        return output;
    }

    public String toString() {
        String output = model.label + ": ";

        for (Cell c : coordinates) {
            output += c + " ";
        }

        return output;
    }
}