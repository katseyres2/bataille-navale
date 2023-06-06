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

public class SimulateGameCommand extends Command {
    /**
     * Constructs an InfoCommand object.
     */
    public SimulateGameCommand() {
        super("/simulategame",
                null,
                new String[]{"bot1","bot2"},
                Command.Role.AUTHENTICATED,
                "Simulate a game with 2 bots."
        );
    }

    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
        Player player = DiscoveryService.findOneBy(client, players);
        if (player == null) return ServerResponse.notConnected;
        if (args.length != 3) return ServerResponse.wrongNumberOfParameters;

        Game game = new Game();

        for (String username : new String[]{args[1],args[2]}) {
            Bot bot = (Bot)DiscoveryService.findPlayerByUsername(username, Server.getPlayers());
            if (bot == null) return ServerResponse.playerNotFound(username);
            if (!bot.isBot()) return ServerResponse.isNotBot(bot);
            game.addPlayer(bot);
        }

        Server.pushGame(game);
        game.run();
        return ServerResponse.gameStarted;
    }
}
