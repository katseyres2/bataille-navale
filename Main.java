
import grid.Grid;

public class Main {
	public static void main(String[] args) {
		Grid grid = new Grid();
		// grid.show();
		// int[] point = grid.getRandomPoint();
		// System.out.println(grid.canFillPoint(point[0], point[1]));
		grid.saveBoat(Grid.AIRCRAFT_CARRIER_LENGTH, "A");
		grid.saveBoat(Grid.BATTLESHIP_LENGTH, "B");
		grid.saveBoat(Grid.CRUISER_LENGTH, "C");
		grid.saveBoat(Grid.DESTROYER_LENGTH, "D");
		grid.saveBoat(Grid.SUBMARINE_LENGTH, "S");
		grid.show();
	}
}
