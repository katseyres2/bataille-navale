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

    private Model model;
    private ArrayList<Cell> coordinates;

    private boolean isPlaced;

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

    public Boat(Model model, Cell reference, Vector vector) throws InstantiationException {
        ArrayList<Cell> cells = new ArrayList<>();

        for (int i = 0; i < model.length; i++) {
            cells.add(new Cell(
                reference.getRow() + i * vector.getRow(),
                reference.getColumn() + i * vector.getColumn()
            ));
        }

        this.model = model;
        this.isPlaced = model.length == cells.size();

        if (!isPlaced || !DirectionService.areValidCoordinates(cells)) {
//            if (!isPlaced) System.out.println("isPlaced : " + model.length + ", " + cells.size());
//            else System.out.println("invalidCoordinates");
            throw new InstantiationException();
        }

        this.coordinates = cells;
    }

    public Boat(@NotNull Model model, @NotNull ArrayList<Cell> coordinates) throws InstantiationException {
        this.model = model;
        this.isPlaced = model.length == coordinates.size();

        if (!isPlaced || !DirectionService.areValidCoordinates(coordinates)) {
//            if (!isPlaced) System.out.println("isPlaced : " + model.length + ", " + coordinates.size());
//            else System.out.println("invalidCoordinates");
            throw new InstantiationException();
        }

        this.coordinates = coordinates;
    }

    public ArrayList<Cell> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Cell> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isPlaced() {
        return getCoordinates() != null && getCoordinates().size() == model.getLength();
    }

//    public void addCell(Cell cell) {
//        getCoordinates().add(cell);
//    }

//    public boolean isSink() {
//        return coordinates.stream().allMatch(Cell::isSink);
//    }


}