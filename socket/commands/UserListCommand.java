package socket.commands;

import java.util.ArrayList;

import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import services.FormatService;
import socket.Command;

public class UserListCommand extends Command {

	/**
	 * Constructs a UserListCommand object.
	 */
	public UserListCommand() {
		super("/userlist", null, null, Role.AUTHENTICATED, "List all connected users.");
	}

	/**
	 * Executes the userlist command to retrieve a list of all connected users.
	 *
	 * @param args    The command arguments.
	 * @param client  The SocketClient object associated with the command.
	 * @param players The list of players in the game.
	 * @return The list of connected users as a formatted string.
	 */
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		String message = "List Of Users;\n".concat(" |--------------;\n");

		for (int i = 0; i < players.size(); i++) {
			if (player == null || players.get(i).getUsername().compareTo(player.getUsername()) == 0)
				continue;

			message += " | (" + (players.get(i).isBot() ? "BOT" : "PLAYER") + ") "
					+ players.get(i).getUsername() + " "
					+ (players.get(i).isLogged() ? "online" : "offline").toUpperCase() + ", "
					+ FormatService.LocalDateTimeToString(players.get(i).getLastConnection())
					+ ", V = " + players.get(i).getVictories() + ", D = " + players.get(i).getDefeats()
					+ ";\n";
		}

		return message += " |;\n |------------";
	}
}