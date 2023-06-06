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
	 * Populates the list of commands.
	 * This method adds all classes from the "socket.commands" package that inherit from the Command class.
	 */
	public static void populateCommands() {
		// Extract all classes from the package "socket.commands"
		Set<Class> classes = LoaderService.findAllClassesUsingClassLoader("socket.commands");

		// Iterate over each class
		for (Class myClass : classes) {
			try {
				// Call the constructor to build a new instance for this class
				Constructor inst = myClass.getConstructor(null);
				// Instantiate a new object and cast it to the Command type
				Command cmd = (Command) inst.newInstance();
				// Add the new command to the command list
				commands.add(cmd);
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
				System.out.println("Something went wrong during populateCommands.");
			}
		}
	}

	/**
	 * Executes a command based on the provided line.
	 *
	 * @param line    The command line received from the client.
	 * @param client  The SocketClient object representing the client connection.
	 * @param players The list of players in the server.
	 * @return The response message to be sent back to the client.
	 */
	public static String executeCommand(String line, SocketClient client, ArrayList<Player> players) {
		if (line == null) return "";

		System.out.println("From client: " + line);

		String messageToSend = "";
		String[] args = line.split(" ");

		for (Command c : commands) {
			if (c.getName().compareTo(args[0]) == 0) {
				String response = c.execute(args, client, players);
				System.out.println("To client: " + response);
				return messageToSend + response;
			}
		}

		return notFound();
	}

	/**
	 * Generates a "command not found" message.
	 *
	 * @return The "command not found" message.
	 */
	private static String notFound() {
		return "Command not found. Please send the command /help for more information.";
	}
}