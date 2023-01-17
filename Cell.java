public class Cell {
	private int row;
	private int column;
	private boolean hasBoat;
	private boolean discovered;

	public Cell(int row, int column, boolean hasBoat) {
		this.row = row;
		this.column = column;
		this.hasBoat = hasBoat;
		discovered = false;
	}

	public String toString() { return "(" + row + "," + column + ")"; }
	public int getRow() { return row; }
	public int getColumn() { return column; }
	public void setRow(int value) { row = value; }
	public void setColumn(int value) { column = value; }
	public boolean touchBoat(int row, int column) { return hasBoat; }
	public boolean alreadyDiscovered() { return discovered; }
}
