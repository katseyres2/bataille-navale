package socket.commands;

import services.DiscoveryService;
import services.ServerResponse;
import socket.Command;
import socket.client.SocketClient;
import socket.server.Player;

import java.util.ArrayList;

public class HistoryCommand extends Command {

    public HistoryCommand() {
        super("/history", null, null, Role.AUTHENTICATED, "The user command history.");
    }

    @Override
    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
        Player player = DiscoveryService.findOneBy(client, players);
        if (player == null) return ServerResponse.notConnected;

//        ArrayList<Message> messages = Server.findMessagesFrom(player.getUsername());
//        for (Message m : messages) {
//            output += "[" + FormatService.LocalDateTimeToString(m.getTime()) + "] FROM : " + m.getFrom().getUsername() + ", MESSAGE : " + m.getText() + ";";
//        }

        return "yo";
    }
}
