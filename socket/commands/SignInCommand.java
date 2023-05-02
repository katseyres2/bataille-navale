package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Player;
import socket.Command;

public class SignInCommand extends Command {
    public SignInCommand() {
		super("/signin",
			null,
			new String[]{"username",
			"password"},
			Command.Role.UNDEFINED,
			"Connect to you account."
		);
    }

    public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {
		String message = "";
		String username;
		String password;
		boolean usernameMatched = false;

		for (Player p : players) {
			if (p.getSocket() == socket) {
				message += "You're already connected.";
				return message;
			}
		}

		if (args.length != 3) {
			message += "You must specify <username> <password>.";
		} else {
			username = args[1];
			password = args[2];

			for (Player p : players) {
				if (p.getUsername().compareTo(username) == 0) {
					usernameMatched = true;

					if (p.checkCredentials(username, password)) {
						if (p.isLogged()) {
							message += "You're connected on another device.";
						} else {
							p.toggleLog();
							p.refreshConnection(socket, pw, br);
							message += "Welcome back " + p.getUsername() + ".";
						}
					} else {
						message += "Invalid credentials.";
					}

					break;
				}
			}

			if (! usernameMatched) {
				message += "The user " + username + " does not exist.";
			}
		}

		return message;
	}
}
