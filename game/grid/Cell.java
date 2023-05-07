package game.grid;

import game.boat.Boat;

public class Cell {
	private int rowIndex; // the row index of the cell
	private int columnIndex; // the column index of the cell
	private boolean discovered; // whether the cell has been discovered or not
	private Boat.typeBoat boat;


	/**
	 * Constructs a new Cell object with the specified row, column and boat status.
	 *
	 * @param rowIndex     the row index of the cell in the grid
	 * @param columnIndex  the column index of the cell in the grid
	 * @param boat
	 */
	public Cell(int rowIndex, int columnIndex, Boat.typeBoat boat) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.boat = boat;
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
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * Returns the column index of the cell in the grid.
	 *
	 * @return the column index of the cell
	 */
	public int getColumnIndex() {
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
	 * Returns whether or not the cell has a boat and marks it as discovered if it hasn't been already.
	 *
	 * @return true if the cell has a boat, false otherwise
	 */
	public Boat.typeBoat isHasBoat(int row, int column) {
		if (!discovered) {
			discovered = true;
			return boat;
		}
		return null;
	}

	/**
	 * Returns whether the cell has already been discovered.
	 *
	 * @return true if the cell has been discovered, false otherwise
	 */
	public boolean alreadyDiscovered() {
		return discovered;
	}

	public Boat.typeBoat getBoat() {
		return boat;
	}

	public void setBoat(Boat.typeBoat boat) {
		this.boat = boat;
	}
}
