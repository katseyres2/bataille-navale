package socket.commands;

import java.util.ArrayList;

import Bots.Bot;
import game.Game;
import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import services.exceptions.InvitationAlreadySentException;
import services.exceptions.UserAlreadyInvitedYouException;
import socket.Command;
import socket.server.Server;

public class InviteCommand extends Command {
    public InviteCommand() {
        super(
            "/invite",
            null,
            new String[]{"username"},
            Command.Role.AUTHENTICATED,
            "Invite your friends to play a new game."
        );
    }

    public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		StringBuilder message = new StringBuilder();

		if (args.length != 2) {
			message.append(ServerResponse.wrongNumberOfParameters);
		} else if (args[1].compareTo(player.getUsername()) == 0) {
			message.append(ServerResponse.inviteYourself);
		} else {
			String username = args[1];

			Game game = Server.getActiveGame(player);
			if (game == null) game = new Game();

			// try to find the user you want invite
			for (Player userToInvite : players) {
				// if you find the user and if he's connected
				if (userToInvite.getUsername().compareTo(username) == 0) {
					if (!userToInvite.isLogged()) {
						message.append(ServerResponse.playerNotConnected);
						return message.toString();
					}

					game.addPlayer(player);
					Server.pushGame(game);

					if (userToInvite.isBot()) {
						game.addPlayer(userToInvite);
						message.append(ServerResponse.botInvited((Bot) userToInvite));
					} else {
						try {
							// try to invite the user, if you can't invite it throws an exception
							player.tryInvite(userToInvite);
							userToInvite.getPrintWriter().println(ServerResponse.receiveAnInvitationFrom(player, userToInvite));
							userToInvite.getPrintWriter().flush();

							message.append(ServerResponse.invitationSentTo(userToInvite));

							player.addInUsersYouInvited(userToInvite);    // add in your list the user you invited
							userToInvite.addInUsersWhoInvitedYou(player); // add in the invited user your invitation

						} catch (UserAlreadyInvitedYouException e) {
							message.append(ServerResponse.youAlreadyReceivedAnInvitation(userToInvite));
						} catch (InvitationAlreadySentException e) {
							message.append(ServerResponse.youAlreadySentAnInvitation(userToInvite));
						}
					}

					return message.toString();
				}
			}

			message.append(ServerResponse.playerNotFound);
			return message.toString();
		}

		return message.toString();
	}
}
