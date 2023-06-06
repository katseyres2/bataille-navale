
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import services.LogService.LEVEL;
import socket.client.SocketClient;
import socket.server.Server;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("server[0] or client[1] : ");
		int response = Integer.parseInt(sc.nextLine());

		// Game game = new Game();
		// game.start();

		if (response == 0) {
			// SERVER CREATION
			Server.setLogLevel(LEVEL.DEBUG);
			Server server = new Server();
			server.start(Server.PORT);
		} else if (response == 1) {
			// CLIENT CREATION
			// int port;

			// try {
			// 	port = Integer.parseInt(args[0]);
			// } catch (NumberFormatException e) {
			// 	System.out.println("Invalid port format");
			// 	return;
			// }

			final Socket s;
			final BufferedReader br;
			final PrintWriter pw;

			try {
				String host = "10.1.20.240";
				// String host = "45.147.97.136";
				System.out.println(host + ":" + Server.PORT);
				s = new Socket(host, Server.PORT);
				pw = new PrintWriter(s.getOutputStream());
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch (Exception e) {
				System.out.println("Unable to create the server socket");
				return;
			}

			// Player client = new Player(socket, printWriter, bufferedReader);
			(new SocketClient(s, pw, br)).start(Server.PORT);
		}
	}
}
