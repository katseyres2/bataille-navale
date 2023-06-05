package socket.commands;

import java.util.ArrayList;

import game.Game;
import game.grid.Cell;
import game.grid.Grid;
import services.DiscoveryService;
import services.FormatService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command;
import socket.server.Server;

public class ActionCommand extends Command {

	public ActionCommand() {
		super("/action", new String[] {}, new String[] {"player", "letter", "number"}, Role.AUTHENTICATED, "Send an action to your grid for the current game.");
	}

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

		// Fetch the player game.
		Game currentGame = Server.getActiveGame(player);
		if (currentGame == null) return ServerResponse.notPlayingGame;

		return currentGame.sendAction(player, targetPlayer, row, column);
	}
}
