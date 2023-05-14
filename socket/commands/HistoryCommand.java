package socket.commands;

import services.FormatService;
import socket.Command;
import socket.Message;
import socket.server.Player;
import socket.server.Server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class HistoryCommand extends Command {

    public HistoryCommand() {
        super("/history", null, null, Role.AUTHENTICATED, "The user command history.");
    }

    @Override
    public String execute(String[] args, Player player, ArrayList<Player> players) {
        String output = "";
        if (player == null) return output;

//        ArrayList<Message> messages = Server.findMessagesFrom(player.getUsername());
//        for (Message m : messages) {
//            output += "[" + FormatService.LocalDateTimeToString(m.getTime()) + "] FROM : " + m.getFrom().getUsername() + ", MESSAGE : " + m.getText() + ";";
//        }

        return output;
    }
}
