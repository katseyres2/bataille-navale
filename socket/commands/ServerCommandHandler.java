package socket.commands;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerCommandHandler {
	private PrintWriter printWriter;
	
	public ServerCommandHandler(PrintWriter printWriter) {
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
		String message = "TODO";
		printWriter.println(message);
		printWriter.flush();
	}

	public void signIn() {
		String message = "TODO";
		printWriter.println(message);
		printWriter.flush();
	}

	public void signUp() {
		String message = "TODO";
		printWriter.println(message);
		printWriter.flush();
	}

	public void signOut() {
		String message = "TODO";
		printWriter.println(message);
		printWriter.flush();
	}

	public void notFound() {
		String message = "Command not found, please send the command /help for more information.";
		printWriter.println(message);
		printWriter.flush();
	}

	public void help() {
		String message = "List Of Commands;─┬───────────────; │;";

		for (Command command : ClientCommandHandler.COMMANDS) {
			int totalLength = 30;
			int fillWithSpace = totalLength - command.getName().length() - command.getParameters().length();
			message += " ├─ /" + command.getName() + " " + command.getParameters() + " ".repeat(fillWithSpace) + " : " + command.help() + ";";
		}

		message += " │;";
		message += " └───────────────";

		printWriter.println(message);
		printWriter.flush();
	}
}
