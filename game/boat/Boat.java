package boat;

public class Boat {
    private boolean sink = false;

    public enum typeBoat {
        AIRCRAFT_CARRIER('A', 5), CRUISER('C', 4), SUBMARINE('S', 3), DESTROYER('D', 3), WARSHIP('W', 2);

        private char label;
        private int length;

        typeBoat(char label, int length) {
            this.label = label;
            this.length = length;
        }

        public int getLength() {
            return length;
        }

        public char getLabel() {
            return label;
        }
    }

    private typeBoat type;

    public Boat(typeBoat type) {
        this.type = type;
        this.sink = false;
    }

    public boolean isSink() {
        return sink;
    }
}
