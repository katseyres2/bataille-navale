package interfaces;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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
     * @param player the player who sent the command.
     * @param players the players the server holds.
     * @return the response to send to the user.
     */
    public abstract String execute(String[] args, Player player, ArrayList<Player> players);
}
