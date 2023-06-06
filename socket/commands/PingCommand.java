package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command;

/**
 * The PingCommand class represents a command to send a ping request to the server
 * to check if the client is connected. It extends the Command class.
 */
public class PingCommand extends Command {

    /**
     * Constructs a PingCommand object.
     */
    public PingCommand() {
        super(
                "/ping",
                null,
                null,
                Command.Role.UNDEFINED,
                "Send a ping request to the server to check if you're connected."
        );
    }

    /**
     * Executes the ping command to send a ping request to the server.
     *
     * @param args    The command arguments.
     * @param client  The SocketClient object associated with the command.
     * @param players The list of players in the game.
     * @return The result message of the command execution.
     */
    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
        return ServerResponse.pong;
    }
}