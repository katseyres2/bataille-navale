package socket.commands;

import java.util.ArrayList;

import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
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

    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;
		String message = "";

		message += "Username    : " + player.getUsername();
		message += ";Victories   : " + player.getVictories();
		message += ";Defeats     : " + player.getDefeats();

		return message;
	}
}
