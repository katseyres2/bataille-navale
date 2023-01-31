package boat;

public class Boat {
    private final int width;
    private final int length;
    private boolean sink = false;


    /*
    public final static String LABEL = null;

    public final static int AIRCRAFT_CARRIER = 0;
    public final static int CRUISER = 0;
    public final static int SUBMARINE = 0;
    public final static int DESTROYER = 0;
    public final static int WARSHIP = 0;


     */

    public enum getBoatType {
        AIRCRAFT_CARRIER("A", 5),
        CRUISER("C", 4),
        SUBMARINE("S", 3),
        DESTROYER("D", 3),
        WARSHIP("W", 2);



        getBoatType(String label, int length) {



        }
    }

    public Boat(int width, int length) {
        this.width = width;
        this.length = length;
        this.sink = false;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public boolean isSink() {
        return sink;
    }

    public void setType(){

    }

}




