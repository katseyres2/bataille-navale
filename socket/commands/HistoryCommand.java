package socket.commands;

import services.DiscoveryService;
import services.ServerResponse;
import socket.Command;
import socket.client.SocketClient;
import socket.server.Player;

import java.util.ArrayList;

/**
 * The HistoryCommand class represents a command to retrieve the user command history.
 * It extends the Command class.
 */
public class HistoryCommand extends Command {

    /**
     * Constructs a HistoryCommand object.
     */
    public HistoryCommand() {
        super("/history",
                null,
                null,
                Role.AUTHENTICATED,
                "The user command history."
        );
    }

    /**
     * Executes the history command to retrieve the user command history.
     *
     * @param args    The command arguments.
     * @param client  The SocketClient object associated with the command.
     * @param players The list of players in the game.
     * @return The user command history.
     */
    @Override
    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
        Player player = DiscoveryService.findOneBy(client, players);
        if (player == null) return ServerResponse.notConnected;

        // TODO: Implement fetching the user command history
        // ArrayList<Message> messages = Server.findMessagesFrom(player.getUsername());
        // String output = "";
        // for (Message m : messages) {
        //     output += "[" + FormatService.LocalDateTimeToString(m.getTime()) + "] FROM : " + m.getFrom().getUsername() + ", MESSAGE : " + m.getText() + ";";
        // }

        return "yo"; // Placeholder return statement
    }
}