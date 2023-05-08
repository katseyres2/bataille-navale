package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Game;
import socket.server.Player;
import socket.Command;
import socket.server.Server;

public class GameStateCommand extends Command {

	public GameStateCommand() {
		super("/gamestate", null, null, Role.AUTHENTICATED, "Get you state in the game.");
	}

	@Override
	public String execute(String[] args, Player player, ArrayList<Player> players) {
		Game activeGame = Server.getActiveGame(player);
		if (activeGame == null) return "No game.";
		String header = activeGame.isPlayerTurn(player) ? "That's your turn.;" : "";
		return header + activeGame.displayPlayerGrids(player);
	}
	
}
