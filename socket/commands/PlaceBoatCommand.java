package socket.commands;

import game.Game;
import services.DiscoveryService;
import services.FormatService;
import services.ServerResponse;
import socket.Command;
import socket.client.SocketClient;
import socket.server.Player;
import socket.server.Server;

import java.util.ArrayList;


public class PlaceBoatCommand extends Command {

    public PlaceBoatCommand() {
        super("/placeboat",
                null,
                new String[]{"coord", "length","vector"},
                Command.Role.AUTHENTICATED,
                "give coordonate for placing boat."
        );
    }

    /**
     * get the active game and place the boat of the player
     * @param args all space-separated elements from the user input.
     * @param player the player who sent the command.
     * @param players the players the server holds.
     * @return
     */
    public String execute(String[] args, Player player, ArrayList<Player> players) {

        if (args.length != 3) return ServerResponse.wrongNumberOfParameters;
        int row, column, length;
        String vector;

        try {
            // il faudra peut être inverser la row et la column
            row = FormatService.convertCoordLetter(args[1].substring(0, 1));
            column = Integer.parseInt(args[1].substring(1)) - 1;
            length = Integer.parseInt(args[2]);
            vector = args[3];
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return ServerResponse.wrongParameterFormat;
        }

        if(vector == null) return "";

        // Fetch the player game.
        Game currentGame = Server.getActiveGame(player);
        // Send an action to this game.
        String response =  currentGame.placePlayerBoat(player, length,column, row, vector);

        if (response != null) return response;
        return ServerResponse.actionSuccessful;
    }
}
