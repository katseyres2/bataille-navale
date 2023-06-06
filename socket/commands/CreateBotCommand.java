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

/**
 * The CreateBotCommand class represents a command for creating a new bot.
 * It extends the Command class.
 */
public class CreateBotCommand extends Command {

    /**
     * Constructs a CreateBotCommand object.
     */
    public CreateBotCommand() {
        super("/createbot",
                new String[]{},
                new String[]{"username", "difficulty(0,1,2)"},
                Command.Role.AUTHENTICATED,
                "Create a new bot."
        );
    }

    /**
     * Executes the create bot command.
     *
     * @param args    The command arguments.
     * @param client  The SocketClient object associated with the command.
     * @param players The list of players in the game.
     * @return The response message after executing the command.
     */
    @Override
    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
        Player player = DiscoveryService.findOneBy(client, players);
        if (player == null) return ServerResponse.notConnected;

        if (args.length != 3) return ServerResponse.wrongNumberOfParameters;
        String username = args[1];
        int difficultyLevel;
        Bot.Difficulty difficulty;

        try {
            difficultyLevel = Integer.parseInt(args[2]);
            difficulty = Bot.Difficulty.values()[difficultyLevel];
        } catch (NumberFormatException e) {
            return ServerResponse.wrongParameterFormat;
        }

        Server.addPlayer(new Bot(username, difficulty));
        return ServerResponse.botCreated;
    }
}