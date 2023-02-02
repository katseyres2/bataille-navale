
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import game.Game;
import socket.Player;
import socket.Server;
import socket.SocketClient;

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

		Game game = new Game();
		game.start();


		
		
		if (args.length == 0) {
			// SERVER CREATION
			Server.setLogLevel(Server.Log.DEBUG);
			Server server = new Server();
			server.start(5000);
		} else {
			// CLIENT CREATION
			int port;
			
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.out.println("Invalid port format");
				return;
			}
	
			final Socket s;
			final BufferedReader br;
			final PrintWriter pw;
			
			try {
				String host = "127.0.0.1";
				// String host = "45.147.97.136";
				System.out.println(host + ":" + port);
				s = new Socket(host, port);
				pw = new PrintWriter(s.getOutputStream());
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch(Exception e) {
				System.out.println("Unable to create the server socket");
				return;
			}
	
			// Player client = new Player(socket, printWriter, bufferedReader);
			(new Player(s, pw,br)).start(port);
		}
	}
}
