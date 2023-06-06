package socket.commands;

import java.util.ArrayList;

import game.Game;
import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command;
import socket.server.Server;

public class SurrendCommand extends Command {

	/**
	 * Constructs a SurrendCommand object.
	 */
	public SurrendCommand() {
		super("/surrend", null, null, Role.AUTHENTICATED, "Surrender the game.");
	}

	/**
	 * Executes the surrend command to surrender the game.
	 *
	 * @param args    The command arguments.
	 * @param client  The SocketClient object associated with the command.
	 * @param players The list of players in the game.
	 * @return The result message of the command execution.
	 */
	@Override
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		Game activeGame = Server.getActiveGame(player);

		if (activeGame == null) return ServerResponse.notPlayingGame;
		if (!activeGame.isPlayerTurn(player)) return ServerResponse.notYourTurn;

		return ServerResponse.youCantSurrender;
	}

}