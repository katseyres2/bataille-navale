package interfaces;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command.Role;

/**
 * The ICommand interface represents a command that can be executed.
 */
public interface ICommand {

    /**
     * Retrieves the parameters of the command.
     *
     * @return the parameters of the command.
     */
    String getParameters();

    /**
     * Retrieves the help information for the command.
     *
     * @return the help information for the command.
     */
    String getHelp();

    /**
     * Retrieves the name of the command.
     *
     * @return the name of the command.
     */
    String getName();

    /**
     * Retrieves the role required to execute the command.
     *
     * @return the role required to execute the command.
     */
    Role getRole();

    /**
     * Checks if a given role has permission to execute the command.
     *
     * @param role the role to check.
     * @return true if the role has permission to execute the command, false otherwise.
     */
    boolean hasPermission(Role role);

    /**
     * Executes the command.
     *
     * @param args the space-separated elements from the user input.
     * @param client the client socket.
     * @param players the list of players the server holds.
     * @return the response to send to the user.
     */
    String execute(String[] args, SocketClient client, ArrayList<Player> players);
}