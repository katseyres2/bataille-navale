package socket.commands;

import java.util.ArrayList;

import game.Game;
import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command;
import socket.server.Server;

public class ActionCommand extends Command {

	public ActionCommand() {
		super("/action", new String[] {}, new String[] {"player", "row", "column"}, Role.AUTHENTICATED, "Send an action to your grid for the current game.");
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
		// Send an action to this game.
		String response =  currentGame.sendAction(player, targetPlayer, column, row);
		
		if (response != null) return response;
		return ServerResponse.actionSuccessful;
	}
	
}
