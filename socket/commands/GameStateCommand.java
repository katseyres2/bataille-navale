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

/**
 * The GameStateCommand class represents a command to get the player's state in the game.
 * It extends the Command class.
 */
public class GameStateCommand extends Command {

	/**
	 * Constructs a GameStateCommand object.
	 */
	public GameStateCommand() {
		super("/gamestate",
				null,
				null,
				Role.AUTHENTICATED,
				"Get your state in the game."
		);
	}

	/**
	 * Executes the game state command to get the player's state in the game.
	 *
	 * @param args    The command arguments.
	 * @param client  The SocketClient object associated with the command.
	 * @param players The list of players in the game.
	 * @return The response message with the player's state in the game.
	 */
	@Override
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		Game activeGame = Server.getActiveGame(player);
		if (activeGame == null) return ServerResponse.noGame;

		// Build the header with player information and turn status.
		String header = FormatService.showPlayers(activeGame) + "\n";
		header += activeGame.isPlayerTurn(player) ? ServerResponse.yourTurn : "";

		// Build the complete response with player grids.
		return header + activeGame.displayPlayerGrids(player);
	}
}