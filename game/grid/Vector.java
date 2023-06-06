package game.grid;

public class Vector {
    private final int column;
    private final int row;

    /**
     * Constructs a Vector object with the specified column and row values.
     *
     * @param vertical   The vertical component of the vector.
     * @param horizontal The horizontal component of the vector.
     */
    public Vector(int vertical, int horizontal) {
        this.row = horizontal;
        this.column = vertical;
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return A string representing the vector in the format "[row,column]".
     */
    public String toString() {
        return "[" + row + "," + column + "]";
    }

    /**
     * Returns the row component of the vector.
     *
     * @return The row component.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column component of the vector.
     *
     * @return The column component.
     */
    public int getColumn() {
        return column;
    }
}