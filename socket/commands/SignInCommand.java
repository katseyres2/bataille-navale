package socket.commands;

import java.util.ArrayList;

import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command;

public class SignInCommand extends Command {

	/**
	 * Constructs a SignInCommand object.
	 */
	public SignInCommand() {
		super(
				"/signin",
				null,
				new String[]{"username", "password"},
				Command.Role.UNDEFINED,
				"Connect to your account."
		);
	}

	/**
	 * Executes the signin command to authenticate a player.
	 *
	 * @param args    The command arguments.
	 * @param client  The SocketClient object associated with the command.
	 * @param players The list of players in the game.
	 * @return The result message of the command execution.
	 */
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);

		String message = "";
		String username;
		String password;
		boolean usernameMatched = false;

		if (DiscoveryService.findOneBy(player, players) != null) {
			return message + ServerResponse.alreadyConnected;
		}

		if (args.length != 3) {
			message += ServerResponse.wrongNumberOfParameters;
		} else {
			username = args[1];
			password = args[2];

			for (Player p : players) {
				if (p.getUsername().compareTo(username) == 0) {
					usernameMatched = true;

					if (p.checkCredentials(username, password)) {
						if (p.isLogged()) {
							message += ServerResponse.connectedOnAnotherDevice;
						} else {
							p.refreshConnection(client.getSocket(), client.getPrintWriter(), client.getBufferedReader());
							message += ServerResponse.welcome(p);
						}
					} else {
						message += ServerResponse.invalidCredentials;
					}

					break;
				}
			}

			if (!usernameMatched) {
				message += ServerResponse.playerNotFound;
			}
		}

		return message;
	}
}