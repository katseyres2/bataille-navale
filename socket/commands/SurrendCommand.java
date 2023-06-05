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

	public SurrendCommand() {
		super("/surrend", null, null, Role.AUTHENTICATED, "Surrend the game.");
	}

	@Override
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		Game activeGame = Server.getActiveGame(player);
		
		if (activeGame == null) return ServerResponse.notPlayingGame;
		if (!activeGame.isPlayerTurn(player)) return ServerResponse.notYouTurn ;

		return ServerResponse.youCantSurrender;
	}
	
}
