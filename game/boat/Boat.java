package boat;

public class Boat {
    private boolean sink = false;

    public enum typeBoat {
        AIRCRAFT_CARRIER("A", 5), CRUISER("C", 4), SUBMARINE("S", 3), DESTROYER("D", 3), WARSHIP("W", 2);

        public String label;
        public int length;
        public int hp;

        typeBoat(String label, int length) {
            this.label = label;
            this.length = length;
            this.hp = length;
        }

        public int getLength() {
            return length;
        }

        public String getLabel() {
            return label;
        }

        public int getHp() {
            return hp;
        }

        public int getHit() {
            if (hp != 0) {
                return hp--;
            } else {
                return 0;
            }
        }

    }

    public typeBoat type;

    public Boat(typeBoat type) {
        this.type = type;
        this.sink = false;
    }

    public boolean isSink() {
        return sink;
    }

}