package tests;

import game.grid.Cell;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 *
 */
class CellTest {

    @Test
   public void testToString() {
        Cell cell = new Cell(1, 2);
        String exp = "(1,2)";
        String actual = cell.toString();
        Assert.assertEquals(exp,"(1,2)");

    }

    @Test
   public void getRow() {
        Cell cell = new Cell(1, 2);
        int exp = 1;
        int actual = cell.getRow();
        Assert.assertEquals(exp, 1);
    }

    @Test
   public void getColumn() {
        Cell cell = new Cell(1, 2);
        int exp = 2;
        int actual = cell.getColumn();
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