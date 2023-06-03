package game.boat;

import game.grid.Cell;
import org.jetbrains.annotations.NotNull;
import services.DirectionService;

import java.util.ArrayList;
import java.util.List;

public class Boat {

    public enum Model {
        AIRCRAFT_CARRIER("A", "aircraft carrier", 5),
        CRUISER("C", "cruiser", 4),
        SUBMARINE("S", "submarine", 3),
        DESTROYER("D", "destroyer", 3),
        WARSHIP("W", "warship", 2);

        private String label;

        private int length;
        private String name;

        Model(String label, String name, int length) {
            this.label = label;
            this.name = name;
            this.length = length;
        }

        public int getLength() {
            return length;
        }
        public String getLabel() { return label; }
        public String getName() {
            return name;
        }
    }

    private Model model;
    public List<Cell> coordinates;

    public boolean isPlaced;

    public Boat(@NotNull Model model, @NotNull ArrayList<Cell> coordinates) {
        this.model = model;
        this.isPlaced = model.length != coordinates.size();

        if (!DirectionService.areValidCoordinates(coordinates)) {
            System.out.println("length is not equal to coordinate count");
            return;
        }

        this.coordinates = coordinates;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public List<Cell> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Cell> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isPlaced() {
        return getCoordinates() != null && getCoordinates().size() == getModel().getLength();
    }

    public void addCell(Cell cell) {
        getCoordinates().add(cell);
    }

//    public boolean isSink() {
//        return coordinates.stream().allMatch(Cell::isSink);
//    }


}