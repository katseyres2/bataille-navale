package game.grid;

import game.Player;

import java.util.Random;

/**
 * Represents the grid used in the game for a specific player.
 */
public class Grid_Alex extends Grid {

    private int rows;
    private int columns;
    private Cell[][] cells;
    private Random random;

    /**
     * Constructs a Grid_Alex object with the specified player, cells, rows, and columns.
     *
     * @param player   The player associated with the grid.
     * @param cells    The 2D array of cells representing the grid.
     * @param rows     The number of rows in the grid.
     * @param columns  The number of columns in the grid.
     */
    public Grid_Alex(Player player, Cell[][] cells, int rows, int columns) {
        super(player);
        this.rows = rows;
        this.columns = columns;
        this.cells = cells;
    }

    //----------------------------------------------------------------

    /**
     * Returns the player associated with the grid.
     *
     * @return The player object.
     */
    @Override
    public Player getPlayer() {
        return super.getPlayer();
    }

    /**
     * Returns the number of rows in the grid.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Sets the number of rows in the grid.
     *
     * @param rows The number of rows to set.
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Returns the number of columns in the grid.
     *
     * @return The number of columns.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Sets the number of columns in the grid.
     *
     * @param columns The number of columns to set.
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Returns the 2D array of cells representing the grid.
     *
     * @return The 2D array of cells.
     */
    public Cell[][] getCells() {
        return cells;
    }

    /**
     * Sets the 2D array of cells representing the grid.
     *
     * @param cells The 2D array of cells to set.
     */
    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    /**
     * Sets up the specified cell at the given coordinate in the grid.
     *
     * @param coordinate The coordinate of the cell.
     * @param cell       The cell object to set.
     */
    public void setupCell(Coordinate coordinate, Cell cell) {
        getCells()[coordinate.getX()][coordinate.getY()] = cell;
    }

    //----------------------------------------------------------------

    public void setupVoidGrid() {
        for (int x = 0; x < getColumns(); x++){
            for (int y = 0; y < getRows(); y++){
                setupCell(new Coordinate(x, y), new Cell(x, y, null));
            }
        }
    }

    //----------------------------------------------------------------
}