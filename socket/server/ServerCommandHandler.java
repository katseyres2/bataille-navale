package socket.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import services.LoaderService;
import socket.Command;
import socket.client.SocketClient;
import socket.commands.*;

public class ServerCommandHandler {
	public static final ArrayList<Command> commands = new ArrayList<>();

	/**
	 * Add all elements from the package "socket.commands".
	 * All classes in this package must inherit from the parent class Command.
	 */
	public static void populateCommands() {
		// extract all classes from the package "socket.commands"
		Set<Class> classes = LoaderService.findAllClassesUsingClassLoader("socket.commands");

		// iterate on each class
		for (Class myClass : classes) {
			try {
				// call the constructor to build a new instance for this class
				Constructor inst = myClass.getConstructor(null);
				// instantiate a new object and cast it in Command type
				Command cmd = (Command)inst.newInstance();
				// add the new command in the command list
				commands.add(cmd);
			} catch (InstantiationException|IllegalAccessException|NoSuchMethodException|InvocationTargetException ignored) {
				System.out.println("Something went wrong during populateCommands.");
			}
		}
	}

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

		if (player == null) {
			player = new Player(s,pw,br,null,null,false);
		}

		for (Command c : commands) {
			if (c.getName().contains(args[0])) {
				messageToSend += c.execute(args, player, players);
				return messageToSend;
			}
		}

		return notFound();
	}

	private static String notFound() {
		return "Command not found, please send the command /help for more information.";
	}
}
