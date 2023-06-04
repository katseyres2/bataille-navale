package socket.commands;

import java.util.ArrayList;

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
		} else if (Server.getActiveGame(player) != null) {
			message.append(ServerResponse.alreadyInGame);
		} else {
			String username = args[1];
			boolean playerMatch = false;
			
			for (Player userToInvite : players) {
				if (userToInvite.isLogged() && userToInvite.getUsername().compareTo(username) == 0) {
					playerMatch = true;
					Game game = Server.getActiveGame(player);

					try {
						player.tryInvite(userToInvite);

						userToInvite.getPrintWriter().println(ServerResponse.receiveAnInvitationFrom(player, userToInvite));
						userToInvite.getPrintWriter().flush();
						message.append(ServerResponse.invitationSentTo(userToInvite));

						for (Player cli : players) {
							if (cli.compareTo(player)) {
								cli.addInUsersYouInvited(userToInvite);
							}
						}

						userToInvite.addInUsersWhoInvitedYou(player);
//						Game game = new Game();
						game.addPlayer(player);
						Server.pushGame(game);
					} catch (UserAlreadyInvitedYouException e) {
						message.append(ServerResponse.youAlreadyReceivedAnInvitation(userToInvite));
					} catch (InvitationAlreadySentException e) {
						message.append(ServerResponse.youAlreadySentAnInvitation(userToInvite));
					}

					break;
				}
			}

			if (! playerMatch) {
				message.append(ServerResponse.playerNotFound);
			}
		}

		return message.toString();
	}
}
