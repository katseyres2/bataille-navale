package interfaces;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Player;
import socket.Command.Role;

public interface ICommand {
    public String getParameters();
    public String getHelp();
    public String getName();
    public Role getRole();
    public boolean hasPermission(Role role);
    public abstract String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br);
}
