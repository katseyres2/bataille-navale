package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Player;
import services.FormatService;
import socket.Command;

public class UserListCommand extends Command {
	public UserListCommand() {
		super("/userlist", null, null, Role.AUTHENTICATED, "List all connected users.");
	}
	
	public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {
		String message = "List Of Users;".concat("─┬────────────; │;");

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getUsername().compareTo(player.getUsername()) == 0) continue;
			message += " ├─ " + (players.get(i).isLogged() ? "🟢 " : "🔴 ") + players.get(i).getUsername()
			+ " is " + (players.get(i).isLogged() ? "online" : "offline") + ", last message sent "
						+ FormatService.LocalDateTimeToString(players.get(i).getLastConnection())
						+ ", victories: " + players.get(i).getVictories() + ", defeats:" + players.get(i).getDefeats()
						+ ";";
					}
		return message += " │; └────────────";
	}
}
