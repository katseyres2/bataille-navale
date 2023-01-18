package socket.commands;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerCommand {
	private PrintWriter printWriter;
	
	public ServerCommand(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	public void users(ArrayList<Socket> hosts) {
		String message = "";
		
		for (int i = 0; i < hosts.size(); i++) {
			message += hosts.get(i).getInetAddress().getHostAddress() + ":" + hosts.get(i).getPort() + ", ";
		}

		printWriter.println(message);
		printWriter.flush();
	}

	public void pong() {
		String message = "pong";
		printWriter.println(message);
		printWriter.flush();
	}

	public void invite() {
		
	}

	public void notFound() {
		String message = "command not found";
		printWriter.println(message);
		printWriter.flush();
	}

	public void help() {
		String message = "LIST OF COMMANDS;"
			.concat("─┬───────────────;")
			.concat(" ├─ /help   : all available commands;")
			.concat(" ├─ /users  : list of connected users;")
			.concat(" ├─ /invite : invite a user to play a game;")
			.concat(" └─ /ping   : ping the server");

		System.out.println(message);
		
		printWriter.println(message);
		printWriter.flush();
	}
}
