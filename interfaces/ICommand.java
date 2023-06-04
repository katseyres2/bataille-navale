package interfaces;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import socket.client.SocketClient;
import socket.server.Player;
import socket.Command.Role;

public interface ICommand {
    public String getParameters();
    public String getHelp();
    public String getName();
    public Role getRole();
    public boolean hasPermission(Role role);

    /**
     *
     * @param args all space-separated elements from the user input.
     * @param players the players the server holds.
     * @return the response to send to the user.
     */
    public abstract String execute(String[] args, SocketClient client, ArrayList<Player> players);
}
