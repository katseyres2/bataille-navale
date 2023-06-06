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

    /**
     * Constructs a PlaceBoatCommand object.
     */
    public PlaceBoatCommand() {
        super(
                "/placeboat",
                null,
                new String[]{"coord", "length", "vector"},
                Command.Role.AUTHENTICATED,
                "Give coordinates for placing a boat."
        );
    }

    /**
     * Executes the placeboat command to place a boat for the player in the active game.
     *
     * @param args    The command arguments.
     * @param client  The SocketClient object associated with the command.
     * @param players The list of players in the game.
     * @return The result message of the command execution.
     */
    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
        Player player = DiscoveryService.findOneBy(client, players);
        if (player == null) return ServerResponse.notConnected;

        if (args.length != 3) return ServerResponse.wrongNumberOfParameters;

        int row, column, length;
        String vector;

        try {
            // Convert the coordinate letters to row value.
            row = FormatService.convertCoordLetter(args[1].substring(0, 1));
            column = Integer.parseInt(args[1].substring(1)) - 1;
            length = Integer.parseInt(args[2]);
            vector = args[3];
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return ServerResponse.wrongParameterFormat;
        }

        if (vector == null) return "";

        // Fetch the active game of the player.
        Game currentGame = Server.getActiveGame(player);
        // Place the boat for the player in the game.
        String response = currentGame.placePlayerBoat(player, length, column, row, vector);

        if (response != null) return response;

        return ServerResponse.actionSuccessful;
    }
}