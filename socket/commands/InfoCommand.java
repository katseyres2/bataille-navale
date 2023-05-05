package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import socket.server.Player;
import socket.Command;

public class InfoCommand extends Command {
    public InfoCommand() {
        super("/info",
		 	null,
		 	null,
		 	Command.Role.AUTHENTICATED,
		 	"Get all information about the player."
        );
    }

    public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {
		String message = "";

		message += "Username    : " + player.getUsername();
		message += ";Victories   : " + player.getVictories();
		message += ";Defeats     : " + player.getDefeats();

		return message;
	}
}
