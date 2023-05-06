package game.grid;

public class Coordinate {

    private int x, y;  // x and y coordinates of the Coordinate object
    private boolean isSink;  // true if this coordinate corresponds to a sunken ship

    /**
     * Constructor for Coordinate class.
     * @param x x-coordinate of the coordinate
     * @param y y-coordinate of the coordinate
     * @param isSink true if the coordinate corresponds to a sunken ship
     */
    public Coordinate(int x, int y, boolean isSink) {
        this.x = x;
        this.y = y;
        this.isSink = isSink;
    }

    /**
     * Getter for x-coordinate.
     * @return x-coordinate of the coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Setter for x-coordinate.
     * @param x x-coordinate of the coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter for y-coordinate.
     * @return y-coordinate of the coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for y-coordinate.
     * @param y y-coordinate of the coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Getter for isSink attribute.
     * @return true if the coordinate corresponds to a sunken ship
     */
    public boolean isSink() {
        return isSink;
    }

    /**
     * Setter for isSink attribute.
     * @param sink true if the coordinate corresponds to a sunken ship
     */
    public void setSink(boolean sink) {
        isSink = sink;
    }
}
