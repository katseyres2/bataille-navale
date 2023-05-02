package game.boat;

public class Boat {
    private boolean sink = false;
    public int hp;

    public enum typeBoat {
        AIRCRAFT_CARRIER("A", "aircraft carrier", 5),
        CRUISER("C", "cruiser", 4),
        SUBMARINE("S", "submarine", 3),
        DESTROYER("D", "destroyer", 3),
        WARSHIP("W", "warship", 2);

        public String label;
        public int length;
        public String name;

        typeBoat(String label, String name, int length) {
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