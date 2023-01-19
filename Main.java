
import game.grid.Grid;
import socket.Client;
import socket.Server;

public class Main {
	public static void main(String[] args) {
		// int coord[][] = {
		// 	{0, 0},
		// 	{0, 1},
		// 	{0, 2},
		// 	{0, 3}
		// };

		// int coord2[][] = {
		// 	{5, 7},
		// 	{1, 5},
		// 	{0, 1},
		// 	{3, 5}
		// };

		// Grid grid = new Grid();
		// grid.placeBoat(coord, "H");
		// grid.placeBoat(coord2, "L");
		// grid.show();
		
		// int[] point = grid.getRandomPoint();
		// System.out.println(grid.canFillPoint(point[0], point[1]));
		// grid.saveBoat(Grid.AIRCRAFT_CARRIER_LENGTH, "A");
		// grid.saveBoat(Grid.BATTLESHIP_LENGTH, "B");
		// grid.saveBoat(Grid.CRUISER_LENGTH, "C");
		// grid.saveBoat(Grid.DESTROYER_LENGTH, "D");
		// grid.saveBoat(Grid.SUBMARINE_LENGTH, "S");
		// grid.show();

		// Game game = new Game();
		// game.start();

		int port;

		if (args.length == 0) {
			Server server = new Server();
			server.start(5000);
			return;
		}

		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("Invalid port format");
			return;
		}

		Client client = new Client();
		client.start(port);
	}
}
