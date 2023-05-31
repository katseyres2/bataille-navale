package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import services.FormatService;
import socket.Command;

public class UserListCommand extends Command {
	public UserListCommand() {
		super("/userlist", null, null, Role.AUTHENTICATED, "List all connected users.");
	}
	
	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		String message = "List Of Users;".concat("--------------; |;");

		for (int i = 0; i < players.size(); i++) {
			if (player == null || players.get(i).getUsername().compareTo(player.getUsername()) == 0) continue;
			message += " | " + players.get(i).getUsername() + " " + (players.get(i).isLogged() ? "online" : "offline").toUpperCase() + ", "
						+ FormatService.LocalDateTimeToString(players.get(i).getLastConnection())
						+ ", V = " + players.get(i).getVictories() + ", D = " + players.get(i).getDefeats()
						+ ";";
					}
		return message += " |; |------------";
	}
}
