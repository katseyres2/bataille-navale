package tests;

import game.boat.Boat;
import game.grid.Cell;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 *
 */
class CellTest {

    @Test
   public void testToString() {
        Cell cell = new Cell(1, 2, Boat.typeBoat.CRUISER);
        String exp = "(1,2)";
        String actual = cell.toString();
        Assert.assertEquals(exp,"(1,2)");

    }

    @Test
   public void getRowIndex() {
        Cell cell = new Cell(1, 2, Boat.typeBoat.CRUISER);
        int exp = 1;
        int actual = cell.getRowIndex();
        Assert.assertEquals(exp, 1);
    }

    @Test
   public void getColumnIndex() {
        Cell cell = new Cell(1, 2, Boat.typeBoat.CRUISER);
        int exp = 2;
        int actual = cell.getColumnIndex();
        Assert.assertEquals(exp, 2);
    }

//    @Test
//    void setRow() {
//    }

//    @Test
//    void setColumn() {
//    }
//
//    @Test
//    void touchBoat() {
//    }

    @Test
  public void alreadyDiscovered() {
    }
}