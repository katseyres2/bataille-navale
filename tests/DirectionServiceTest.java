package tests;

import game.grid.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static services.DirectionService.get4Vectors;

class DirectionServiceTest {
    @Test
    public void testGet4Vectors() throws InstantiationException {
        ArrayList<Vector> output = new ArrayList<>();
        output.add(new Vector(-1, 0));
        output.add(new Vector(0, -1));
        output.add(new Vector(0, 1));
        output.add(new Vector(1, 0));

        Assertions.assertEquals(output, get4Vectors());
    }

        @Test
   public void testGetDirectionVector() {
            int[] output;
            output = new int[] {-1, 0};
            output = new int[] {1, 0};
            output = new int[] {0, -1};
            output = new int[] {0, 1};
            Assertions.assertEquals( output,new int[] {0, -1} );




        }

    @Test
   public void testGet8Vectors() {
        var output = new ArrayList<Vector>();
        output.addAll(get4Vectors());
        output.add(new Vector(-1,-1));
        output.add(new Vector(-1,1));
        output.add(new Vector(1,-1));
        output.add(new Vector(1,1));
        Assertions.assertEquals(output, output.addAll(get4Vectors()));
    }

    @Test
  public void testAreValidCoordinates() {
    }
//
//
//    @Test
//    public void isBoatAlongBorder() {
//    }
//
//    @Test
//   public void isBoatAlongOther() {
//    }
//
//    @Test
//  public  void isCellInGrid() {
//    }
//
//    @Test
//    public void isBoatInGrid() {
//    }
}
