package socket.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

import services.DiscoveryService;
import services.LoaderService;
import socket.Command;
import socket.client.SocketClient;

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

	public static String executeCommand(String line, SocketClient client, ArrayList<Player> players) {
		if (line == null) return "";

		System.out.println("From client : " + line);

		String messageToSend = "";
		String[] args = line.split(" ");

		for (Command c : commands) {
			if (c.getName().compareTo(args[0]) == 0) {
				String response = c.execute(args, client, players);
				System.out.println("To   client : " + response);
				return messageToSend + response;
			}
		}

		return notFound();
	}

	private static String notFound() {
		return "Command not found, please send the command /help for more information.";
	}
}
