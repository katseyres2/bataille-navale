package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Game;
import socket.server.Player;
import socket.Command;
import socket.server.Server;

public class SurrendCommand extends Command {

	public SurrendCommand() {
		super("/surrend", null, null, Role.AUTHENTICATED, "Surrend the game.");
	}

	@Override
	public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {
		Game activeGame = Server.getActiveGame(player);
		
		if (activeGame == null) return "You are not playing a game.";
		if (!activeGame.isPlayerTurn(player)) return "It's not your turn.";

		return "You can't surrend yet.";
	}
	
}
