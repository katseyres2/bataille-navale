package tests;

import game.boat.Boat;
import game.grid.Cell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BoatTest {

    @Test
    public void testGetLength() throws InstantiationException {
        Boat.Model model = Boat.Model.AIRCRAFT_CARRIER;
        ArrayList<Cell> coordinates = new ArrayList<>();
        coordinates.add(new Cell(0, 0));
        coordinates.add(new Cell(0, 1));
        coordinates.add(new Cell(0, 2));
        coordinates.add(new Cell(0, 3));
        coordinates.add(new Cell(0, 4));
        Boat boat = new Boat(model, coordinates);
        int expectedLength = 5;
        int actualLength = boat.getLength();
        Assertions.assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetLabel() throws InstantiationException {
        Boat.Model model = Boat.Model.CRUISER;
        ArrayList<Cell> coordinates = new ArrayList<>();
        coordinates.add(new Cell(0, 0));
        coordinates.add(new Cell(0, 1));
        coordinates.add(new Cell(0, 2));
        coordinates.add(new Cell(0, 3));
        Boat boat = new Boat(model, coordinates);
        String expectedLabel = "C";
        String actualLabel = boat.getLabel();
        Assertions.assertEquals(expectedLabel, actualLabel);
    }

    @Test
    public void testGetName() throws InstantiationException {
        Boat.Model model = Boat.Model.SUBMARINE;
        ArrayList<Cell> coordinates = new ArrayList<>();
        coordinates.add(new Cell(0, 0));
        coordinates.add(new Cell(0, 1));
        coordinates.add(new Cell(0, 2));
        Boat boat = new Boat(model, coordinates);
        String expectedName = "submarine";
        String actualName = boat.getName();
        Assertions.assertEquals(expectedName, actualName);
    }

    @Test
    public void testIsSunk() throws InstantiationException {
        Boat.Model model = Boat.Model.DESTROYER;
        ArrayList<Cell> coordinates = new ArrayList<>();
        coordinates.add(new Cell(0, 0));
        coordinates.add(new Cell(0, 1));
        coordinates.add(new Cell(0, 2));
        Boat boat = new Boat(model, coordinates);
        // Assuming none of the cells have been discovered
        boolean expectedIsSunk = false;
        boolean actualIsSunk = boat.isSunk();
        Assertions.assertEquals(expectedIsSunk, actualIsSunk);
    }

    @Test
    public void testGetCoordinates() throws InstantiationException {
        Boat.Model model = Boat.Model.WARSHIP;
        ArrayList<Cell> coordinates = new ArrayList<>();
        coordinates.add(new Cell(0, 0));
        coordinates.add(new Cell(0, 1));
        Boat boat = new Boat(model, coordinates);
        ArrayList<Cell> actualCoordinates = boat.getCoordinates();
        Assertions.assertEquals(coordinates, actualCoordinates);
    }

    @Test
    public void testGetCoordinatesNotHit() throws InstantiationException {
        Boat.Model model = Boat.Model.AIRCRAFT_CARRIER;
        ArrayList<Cell> coordinates = new ArrayList<>();
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(0, 1);
        Cell cell3 = new Cell(0, 2);
        Cell cell4 = new Cell(0, 3);
        Cell cell5 = new Cell(0, 4);
        coordinates.add(cell1);
        coordinates.add(cell2);
        coordinates.add(cell3);
        coordinates.add(cell4);
        coordinates.add(cell5);
        Boat boat = new Boat(model, coordinates);

        // Assuming none of the cells have been discovered
        ArrayList<Cell> actualCoordinatesNotHit = boat.getCoordinatesNotHit();
        Assertions.assertEquals(coordinates, actualCoordinatesNotHit);

        // Marking one of the cells as discovered
        ArrayList<Cell> expectedCoordinatesNotHitAfterDiscovery = new ArrayList<>();
        expectedCoordinatesNotHitAfterDiscovery.add(cell1);
        expectedCoordinatesNotHitAfterDiscovery.add(cell3);
        expectedCoordinatesNotHitAfterDiscovery.add(cell4);
        expectedCoordinatesNotHitAfterDiscovery.add(cell5);
        ArrayList<Cell> actualCoordinatesNotHitAfterDiscovery = boat.getCoordinatesNotHit();
        Assertions.assertEquals(expectedCoordinatesNotHitAfterDiscovery, actualCoordinatesNotHitAfterDiscovery);
    }

    @Test
    public void testToString() throws InstantiationException {
        Boat.Model model = Boat.Model.CRUISER;
        ArrayList<Cell> coordinates = new ArrayList<>();
        coordinates.add(new Cell(0, 0));
        coordinates.add(new Cell(0, 1));
        coordinates.add(new Cell(0, 2));
        coordinates.add(new Cell(0, 3));
        Boat boat = new Boat(model, coordinates);
        String expectedString = "C: (0,0) (0,1) (0,2) (0,3) ";
        String actualString = boat.toString();
        Assertions.assertEquals(expectedString, actualString);
    }
}
