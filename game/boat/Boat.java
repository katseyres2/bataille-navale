package game.boat;

import game.grid.Coordinate;

import java.util.List;

public class Boat {

    public enum typeBoat {

        AIRCRAFT_CARRIER("A", "aircraft carrier", 5, false),
        CRUISER("C", "cruiser", 4, false),
        SUBMARINE("S", "submarine", 3, false),
        DESTROYER("D", "destroyer", 3, false),
        WARSHIP("W", "warship", 2, false);

        public String label;

        public int length;
        public String name;


        typeBoat(String label, String name, int length) {
            this.label = label;
            this.name = name;
            this.length = length;
        }

        public int getLength() {
            return length;
        }

//        public String getLabel() {
//            return label;
//        }

        public String getName() {
            return name;
        }

    }

    public typeBoat type;
    public List<Coordinate> coordinates;

    public boolean isPlaced;

    public Boat(typeBoat type, List<Coordinate> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
        this.isPlaced = false;

    }

    public typeBoat getType() {
        return type;
    }

    public void setType(typeBoat type) {
        this.type = type;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isPlaced() {
        return getCoordinates() != null && getCoordinates().size() == getType().getLength();
    }

    public void addCoordinate(Coordinate coordinate) {
        getCoordinates().add(coordinate);
    }

    public boolean isSink() {
        return coordinates.stream().allMatch(Coordinate::isSink);
    }


}