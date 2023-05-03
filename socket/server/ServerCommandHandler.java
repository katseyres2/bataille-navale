package socket.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import socket.Command;
import socket.commands.*;

public class ServerCommandHandler {
	public static final ArrayList<Command> COMMANDS = new ArrayList<Command>(Arrays.asList(
		new HelpCommand(),
		new UserListCommand(),
		new SignOutCommand(),
		new HelpCommand(),
		new SignInCommand(),
		new InviteCommand(),
		new ConfirmCommand(),
		new SignUpCommand(),
		new InfoCommand(),
		new ActionCommand(),
		new SurrendCommand(),
		new GameStateCommand(),
		new HistoryCommand()
	));

	public static String executeCommand(String line, Socket s, PrintWriter pw, BufferedReader br, ArrayList<Player> players) {
		if (line == null || s == null || pw == null || br == null || players == null) return "";

		String messageToSend = "";
		Player player = null;
		String[] args = line.split(" ");

		for (Player p : players) {
			if (p.getSocket() == s) {
				player = p;
				break;
			}
		}

		for (Command c : COMMANDS) {
			if (c.getName().contains(args[0])) {
				messageToSend += c.execute(args, player, players, s, pw, br);
				return messageToSend;
			}
		}

		return notFound();
	}

	private static String notFound() {
		return "Command not found, please send the command /help for more information.";
	}
}
