package socket.commands;

import java.util.ArrayList;

import game.Game;
import services.DiscoveryService;
import services.FormatService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command;
import socket.server.Server;

public class GameStateCommand extends Command {

	public GameStateCommand() {
		super("/gamestate", null, null, Role.AUTHENTICATED, "Get you state in the game.");
	}

	@Override
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		Game activeGame = Server.getActiveGame(player);
		if (activeGame == null) return ServerResponse.noGame;

		String header = FormatService.showPlayers(activeGame) + "\n";
		header += activeGame.isPlayerTurn(player) ? ServerResponse.yourTurn : "";
		return header + activeGame.displayPlayerGrids(player);
	}
	
}
