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
		super("/gamestate", null, null, Role.AUTHENTIFIED, "Get you state in the game.");
	}

	@Override
	public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {
		Game activeGame = Server.getActiveGame(player);
		String header = activeGame.isPlayerTurn(player) ? "That's your turn.;" : "";
		return header + activeGame.displayPlayerGrids(player);
	}
	
}
