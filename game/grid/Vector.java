package game.grid;

public class Vector {
    private final int vertical;
    private final int horizontal;

    public Vector(int vertical, int horizontal) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public int getRow() {
        return horizontal;
    }

    public int getColumn() {
        return vertical;
    }
}
