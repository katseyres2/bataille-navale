package socket.commands;

import java.util.ArrayList;

import services.DiscoveryService;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command;
import socket.server.ServerCommandHandler;

/**
 * The HelpCommand class represents a command to display all available commands.
 * It extends the Command class.
 */
public class HelpCommand extends Command {

	/**
	 * Constructs a HelpCommand object.
	 */
	public HelpCommand() {
		super("/help",
				null,
				null,
				Command.Role.UNDEFINED,
				"Display all available commands."
		);
	}

	/**
	 * Executes the help command to display all available commands.
	 *
	 * @param args    The command arguments.
	 * @param client  The SocketClient object associated with the command.
	 * @param players The list of players in the game.
	 * @return The response message with the list of available commands.
	 */
	@Override
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);

		String message = "List Of Commands\n |-----------------\n";
		Role role = player != null ? player.getRole() : Role.UNDEFINED;

		for (Command command : ServerCommandHandler.commands) {
			if (command.hasPermission(role)) {
				int totalLength = 60;
				int fillWithSpace = totalLength - command.getName().length() - command.getParameters().length();
				message += " | " + command.getName() + " " + command.getParameters() + " ".repeat(fillWithSpace) + " : " + command.getHelp() + "\n";
			}
		}

		message += " | \n";
		message += " |---------------";

		return message;
	}
}