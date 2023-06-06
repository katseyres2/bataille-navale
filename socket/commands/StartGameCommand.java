package socket.commands;

import Bots.Bot;
import game.Game;
import services.DiscoveryService;
import services.ServerResponse;
import socket.Command;
import socket.client.SocketClient;
import socket.server.Player;
import socket.server.Server;

import java.util.ArrayList;

public class StartGameCommand extends Command {
    public StartGameCommand() {
        super("/startgame", new String[] {}, new String[] {}, Command.Role.AUTHENTICATED, "Start the game.");
    }

    @Override
    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
        Player player = DiscoveryService.findOneBy(client, players);
        if (player == null) return ServerResponse.notConnected;

        if (args.length != 1) return ServerResponse.wrongNumberOfParameters;
        Game game = Server.getActiveGame(player);
        if (game == null) return ServerResponse.noGame;
        if (game.getPlayers().size() < 2) return ServerResponse.notEnoughPlayers;

        // start the game
        game.run();
        return ServerResponse.gameStarted;
    }
}
