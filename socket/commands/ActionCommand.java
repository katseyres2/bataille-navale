package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Game;
import game.Player;
import socket.Command;
import socket.server.Server;

public class ActionCommand extends Command {

	public ActionCommand() {
		super("/action", new String[] {}, new String[] {"player", "row", "column"}, Role.AUTHENTICATED, "Send an action to your grid for the current game.");
	}

	@Override
	public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {
		int row, column;
		Player targetPlayer = null;

		if (args.length != 4) return "Wrong number of parameters.";
		String username = args[1];

		try {
			row = Integer.parseInt(args[2]);
			column = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
			return "Wrong parameter format.";
		}

		for (Player p : players) {
			if (p.getUsername().compareTo(username) == 0) {
				targetPlayer = p;
				break;
			}
		}

		if (targetPlayer == null) return "Player with this username not found.";

		// Fetch the player game.
		Game currentGame = Server.getActiveGame(player);
		// Send an action to this game.
		String response =  currentGame.sendAction(player, targetPlayer, column, row);
		
		if (response != null) return response;
		return "Action successful.";
	}
	
}
