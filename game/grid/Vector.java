package game.grid;

public class Vector {
    private final int column;
    private final int row;

    public Vector(int vertical, int horizontal) {
        this.row = horizontal;
        this.column = vertical;
    }

    public String toString() {
        return "[" + row + "," + column + "]";
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
