package socket.commands;

import java.util.ArrayList;

import game.Game;


import services.DiscoveryService;

import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command;
import socket.server.Server;

/**
 * The ActionCommand class represents a command for sending an action to the grid of the current game.
 * It extends the Command class.
 */
public class ActionCommand extends Command {

	/**
	 * Constructs an ActionCommand object.
	 */
	public ActionCommand() {
		super("/action", new String[] {}, new String[] {"player", "letter", "number"},
				Role.AUTHENTICATED, "Send an action to your grid for the current game.");
	}

	/**
	 * Executes the action command.
	 *
	 * @param args    The command arguments.
	 * @param client  The SocketClient object associated with the command.
	 * @param players The list of players in the game.
	 * @return The response message after executing the command.
	 */
	@Override
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		int row, column;
		Player targetPlayer = null;

		if (args.length != 4) return ServerResponse.wrongNumberOfParameters;
		String username = args[1];

		try {
			row = Integer.parseInt(args[2]);
			column = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
			return ServerResponse.wrongParameterFormat;
		}

		for (Player p : players) {
			if (p.getUsername().compareTo(username) == 0) {
				targetPlayer = p;
				break;
			}
		}

		if (targetPlayer == null) return ServerResponse.playerNotFound;

		// Fetch the player's game.
		Game currentGame = Server.getActiveGame(player);
		if (currentGame == null) return ServerResponse.notPlayingGame;

		return currentGame.sendAction(player, targetPlayer, row, column);
	}
}