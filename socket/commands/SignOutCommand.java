package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
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

    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;
		player.clear();
		return ServerResponse.seeYouSoon;
	}
}
