package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Player;
import socket.Command;

public class PingCommand extends Command {

    public PingCommand() {
        super(
            "/ping",
            null,
            null,
            Command.Role.UNDEFINED,
            "Send a ping request the server to check if you're connected."
		);
    }

    public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {
		  return "pong!";
	  }
}
