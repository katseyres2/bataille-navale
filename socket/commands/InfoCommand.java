package socket.commands;

import java.util.ArrayList;

import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command;

/**
 * The InfoCommand class represents a command to get all information about the player.
 * It extends the Command class.
 */
public class InfoCommand extends Command {

	/**
	 * Constructs an InfoCommand object.
	 */
	public InfoCommand() {
		super("/info",
				null,
				null,
				Command.Role.AUTHENTICATED,
				"Get all information about the player."
		);
	}

	/**
	 * Executes the info command to retrieve all information about the player.
	 *
	 * @param args    The command arguments.
	 * @param client  The SocketClient object associated with the command.
	 * @param players The list of players in the game.
	 * @return The information about the player.
	 */
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		// Generate the player information
		String message = "";
		message += "Username    : " + player.getUsername();
		message += ";Victories   : " + player.getVictories();
		message += ";Defeats     : " + player.getDefeats();

		return message;
	}
}