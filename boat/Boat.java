package boat;

public class Boat {
    private int width;
    private int length;
    private boolean sink;

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

}
