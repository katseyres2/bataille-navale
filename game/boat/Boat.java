package game.boat;

public class Boat {
    private boolean sink = false;
    public int hp;

    public enum typeBoat {
        AIRCRAFT_CARRIER("A", 5), CRUISER("C", 4), SUBMARINE("S", 3), DESTROYER("D", 3), WARSHIP("W", 2);

        public String label;
        public int length;

        typeBoat(String label, int length) {
            this.label = label;
            this.length = length;
        }

        public int getLength() {
            return length;
        }

        public String getLabel() {
            return label;
        }

    }

    public typeBoat type;

    public Boat(typeBoat type) {
        this.type = type;
        this.sink = false;
        this.hp = type.length;
    }

    public boolean isSink() {
        return sink;
    }

    public int getHp() {
        return hp;
    }

    public void getHit() {
        if (hp != 0) {
            setHP();
        }
        if (hp == 0) {
            sink = true;
        }
    }

    public void setHP() {
        hp--;
    }

}