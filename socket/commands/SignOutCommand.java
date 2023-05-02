package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Player;
import socket.Command;

public class SignOutCommand extends Command {
    public SignOutCommand() {
        super("/signout",
        null,
        null,
        Command.Role.AUTHENTICATED,
        "Disconnect from your account."
        );
    }

    public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {
		for (Player p : players) {
			if (p.getSocket() == player.getSocket()) {
				p.clear();													// Assigns null to all socket parameters.
				if (p.isLogged()) p.toggleLog();							// Sets to isLogged to false.
				break;
			}
		}

		return "You're disconnected, see you soon. 🙂";
	}
}
