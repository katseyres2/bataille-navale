package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import socket.client.SocketClient;
import socket.server.Player;
import services.FormatService;
import socket.Command;

public class SignUpCommand extends Command {
    public SignUpCommand() {
        super(
			"/signup",
		 	null,
		 	new String[]{"username", "password"},
		 	Command.Role.UNDEFINED,
		 	"Create a new account."
		);
    }

	/**
     * Create a new Client with new credentials
     */
	public String execute(String[] args, SocketClient client, @NotNull ArrayList<Player> players) {
		String message = "";
		boolean usernameAlreadyExists = false;
		String username;
		String password;
		Player connectedPlayer = null;

		for (Player p : players) {
			if (p.getSocket() == client.getSocket()) {
				connectedPlayer = p;
				break;
			}
		}
		
		if (args.length != 3) {
			message += "You must specify <username> <password>.";
		} else if (args[1].length() > FormatService.USERNAME_MAX_LENGTH) {
			message += "The username must have up to "+ FormatService.USERNAME_MAX_LENGTH +" characters.";
		} else if (args[2].length() > FormatService.PASSWORD_MAX_LENGTH) {
			message += "The password must have up to "+ FormatService.PASSWORD_MAX_LENGTH +" characters.";
		} else if (connectedPlayer != null) {
			message += "You're already connected.";
		} else {
			username = args[1];
			password = args[2];
			
			for (Player p : players) {
				if (p.getUsername().compareTo(username) == 0) {
					usernameAlreadyExists = true;
					message += "This username already exists.";
					break;
				}
			}
			
			if (! usernameAlreadyExists) {
				Player newPlayer = new Player(client.getSocket(), client.getPrintWriter(), client.getBufferedReader(), username, password, false);
				players.add(newPlayer);
				
				message += "Your have created a new account, welcome " + username + ".;;";
				message +=  (new UserListCommand()).execute(args, client, players);
			}
		}

		return message;
	}
}
