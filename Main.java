
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import socket.Player;
import socket.Server;
import socket.SocketClient;

public class Main {
	public static void main(String[] args) {

		// Game game = new Game();
		// game.start();

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
			} catch (Exception e) {
				System.out.println("Unable to create the server socket");
				return;
			}

			// Player client = new Player(socket, printWriter, bufferedReader);
			(new Player(s, pw, br, null, null, null)).start(port);
		}
	}
}
