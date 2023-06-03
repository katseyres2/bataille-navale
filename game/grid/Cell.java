package game.grid;

public class Cell {
	private int rowIndex; // the row index of the cell
	private int columnIndex; // the column index of the cell
	private boolean discovered; // whether the cell has been discovered or not

	/**
	 * Constructs a new Cell object with the specified row, column and boat status.
	 *
	 * @param rowIndex     the row index of the cell in the grid
	 * @param columnIndex  the column index of the cell in the grid
	 */
	public Cell(int rowIndex, int columnIndex) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		discovered = false;
	}

	/**
	 * Returns a string representation of the cell in the format (row, column).
	 *
	 * @return a string representation of the cell
	 */
	public String toString() {
		return "(" + rowIndex + "," + columnIndex + ")";
	}

	/**
	 * Returns the row index of the cell in the grid.
	 *
	 * @return the row index of the cell
	 */
	public int getRow() {
		return rowIndex;
	}

	/**
	 * Returns the column index of the cell in the grid.
	 *
	 * @return the column index of the cell
	 */
	public int getColumn() {
		return columnIndex;
	}

	/**
	 * Sets the row index of the cell in the grid.
	 *
	 * @param value the new row index value
	 */
	public void setRowIndex(int value) {
		rowIndex = value;
	}

	/**
	 * Sets the column index of the cell in the grid.
	 *
	 * @param value the new column index value
	 */
	public void setColumnIndex(int value) {
		columnIndex = value;
	}

	/**
	 * Returns whether the cell has already been discovered.
	 *
	 * @return true if the cell has been discovered, false otherwise
	 */
	public boolean isDiscovered() {
		return discovered;
	}

	public void discover() {
		this.discovered = true;
	}
}
