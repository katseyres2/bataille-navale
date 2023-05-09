package socket.commands;

import game.Game;
import socket.Command;
import socket.server.Player;
import socket.server.Server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static game.grid.Grid.convertCoordLetter;


public class PlaceBoatCommand extends Command {

    public PlaceBoatCommand() {
        super("/placeboat",
                null,
                new String[]{"coord", "length","vector"},
                Command.Role.AUTHENTICATED,
                "give coordonate for placing boat."
        );
    }


    public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {

        if (args.length != 3) return "Wrong number of parameters.";
        int row, column, length;
        String vector;

        try {
            // il faudra peut être inverser la row et la column
            row = convertCoordLetter(args[1].substring(0, 1));
            column = Integer.parseInt(args[1].substring(1)) - 1;
            length = Integer.parseInt(args[2]);
            vector = args[3];
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return "Wrong parameter format.";
        }

        if(vector == null) return "";

        // Fetch the player game.
        Game currentGame = Server.getActiveGame(player);
        // Send an action to this game.
        String response =  currentGame.placePlayerBoat(player, length,column, row, vector);

        if (response != null) return response;
        return "Action successful.";

    }
}
