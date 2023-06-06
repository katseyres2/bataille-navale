package game.boat;

import game.grid.Cell;
import game.grid.Vector;
import org.jetbrains.annotations.NotNull;
import services.DirectionService;

import java.util.ArrayList;

/**
 * The Boat class represents a boat object in a game.
 */
public class Boat {

    /**
     * The Model enum represents different types of boats in a game.
     */
    public enum Model {
        AIRCRAFT_CARRIER("A", "aircraft carrier", 5),
        CRUISER("C", "cruiser", 4),
        SUBMARINE("S", "submarine", 3),
        DESTROYER("D", "destroyer", 3),
        WARSHIP("W", "warship", 2);

        private final String label;
        private final int length;
        private final String name;

        /**
         * Constructs a Model enum constant with the specified label, name, and length.
         *
         * @param label  the label of the boat
         * @param name   the name of the boat
         * @param length the length of the boat
         */
        Model(String label, String name, int length) {
            this.label = label;
            this.name = name;
            this.length = length;
        }

        /**
         * Returns the length of the boat.
         *
         * @return the length of the boat
         */
        public int getLength() {
            return length;
        }

        /**
         * Returns the label of the boat.
         *
         * @return the label of the boat
         */
        public String getLabel() {
            return label;
        }

        /**
         * Returns the name of the boat.
         *
         * @return the name of the boat
         */
        public String getName() {
            return name;
        }
    }

    private final Model model;
    private final ArrayList<Cell> coordinates;

    /**
     * Constructs a Boat object with the specified model and coordinates.
     *
     * @param model      the model of the boat
     * @param coordinates the list of cells that the boat occupies
     * @throws InstantiationException if the number of coordinates or the coordinates themselves are invalid
     */
    public Boat(@NotNull Model model, ArrayList<Cell> coordinates) throws InstantiationException {
        this.model = model;

        if (model.getLength() != coordinates.size() || !DirectionService.areValidCoordinates(coordinates)) {
            throw new InstantiationException();
        }

        this.coordinates = coordinates;
    }

    /**
     * Returns the length of the boat.
     *
     * @return the length of the boat
     */
    public int getLength() {
        return model.getLength();
    }

    /**
     * Returns the label of the boat.
     *
     * @return the label of the boat
     */
    public String getLabel() {
        return model.getLabel();
    }

    /**
     * Returns the name of the boat.
     *
     * @return the name of the boat
     */
    public String getName() {
        return model.getName();
    }

    /**
     * Checks if the boat has been sunk.
     *
     * @return true if the boat has been sunk, false otherwise
     */
    public boolean isSunk() {
        for (Cell c : coordinates) {
            if (!c.isDiscovered()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the list of coordinates that the boat occupies.
     *
     * @return the list of coordinates
     */
    public ArrayList<Cell> getCoordinates() {
        return coordinates;
    }

    /**
     * Returns the list of coordinates that have not been hit yet.
     *
     * @return the list of coordinates not hit
     */
    public ArrayList<Cell> getCoordinatesNotHit() {
        ArrayList<Cell> output = new ArrayList<>();

        for (Cell c : coordinates) {
            if (!c.isDiscovered()) {
                output.add(c);
            }
        }
        return output;
    }

    /**
     * Returns a string representation of the boat.
     *
     * @return a string representation of the boat
     */
    public String toString() {
        String output = model.getLabel() + ": ";

        for (Cell c : coordinates) {
            output += c + " ";
        }
        return output;
    }
}
