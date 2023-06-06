package socket.commands;

import java.util.ArrayList;

import Bots.Bot;
import game.Game;
import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import services.FormatService;
import socket.Command;
import socket.server.Server;

public class UserListCommand extends Command {

	/**
	 * Constructs a UserListCommand object.
	 */
	public UserListCommand() {
		super("/userlist", null, null, Role.AUTHENTICATED, "List all connected users.");
	}

	/**
	 * Executes the userlist command to retrieve a list of all connected users.
	 *
	 * @param args    The command arguments.
	 * @param client  The SocketClient object associated with the command.
	 * @param players The list of players in the game.
	 * @return The list of connected users as a formatted string.
	 */
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		String message = "List Of Users;\n".concat(" |--------------\n");

		for (int i = 0; i < players.size(); i++) {
			if (player == null || players.get(i).getUsername().compareTo(player.getUsername()) == 0)
				continue;

			Player target = players.get(i);

			String difficulty = null;
			if(target.isBot()){
				Bot bot = (Bot)target;
				switch (bot.getDifficulty()) {
					case EASY -> difficulty = "EASY";
					case MEDIUM -> difficulty = "MEDIUM";
					case HARD -> difficulty = "HARD";
				}
			}

			Game activeGame = Server.getActiveGame(target);

			message += " | (" + (target.isBot() ? "BOT " + difficulty: "PLAYER") + ") "
					+ target.getUsername() + " " + (activeGame != null ? "IN GAME" : "Not IN GAME") + " "
					+ (target.isLogged() ? "online" : "offline").toUpperCase() + ", "
					+ FormatService.LocalDateTimeToString(target.getLastConnection())
					+ ", V = " + target.getVictories() + ", D = " + target.getDefeats()
					+ "\n";
		}
		message += " | \n";
		message += " |---------------";
		return message;
	}
}