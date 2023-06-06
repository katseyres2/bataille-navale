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

    /**
     * Constructs a SignOutCommand object.
     */
    public SignOutCommand() {
        super(
                "/signout",
                null,
                null,
                Command.Role.AUTHENTICATED,
                "Disconnect from your account."
        );
    }

    /**
     * Executes the signout command to disconnect a player.
     *
     * @param args    The command arguments.
     * @param client  The SocketClient object associated with the command.
     * @param players The list of players in the game.
     * @return The result message of the command execution.
     */
    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
        Player player = DiscoveryService.findOneBy(client, players);

        if (player == null)
            return ServerResponse.notConnected;

        player.clear();
        return ServerResponse.seeYouSoon;
    }
}