package socket.commands;

import java.util.ArrayList;

import game.Game;
import services.DiscoveryService;
import services.ServerResponse;
import socket.client.SocketClient;
import socket.server.Player;
import services.FormatService;
import services.exceptions.InvitationAlreadySentException;
import services.exceptions.NoInvitationReceivedException;
import socket.Command;
import socket.server.Server;

public class ConfirmCommand extends Command {
    public ConfirmCommand() {
        super("/confirm",
            null,
            new String[]{"username"},
            Command.Role.AUTHENTICATED,
            "Confirm the invitation."
        );
    }

	public String execute(String[] args, SocketClient client, ArrayList<Player> players) {
		Player player = DiscoveryService.findOneBy(client, players);
		if (player == null) return ServerResponse.notConnected;

		StringBuilder message = new StringBuilder();

		if (args.length != 2) {
			message.append(ServerResponse.wrongNumberOfParameters);
		} else if (Server.getActiveGame(player) != null) {
			message.append(ServerResponse.alreadyInGame);
		} else {
			String username = args[1];
			boolean playerMatch = false;

			for (Player userWhoInvitedYou : players) {
				if (userWhoInvitedYou.isLogged() && userWhoInvitedYou.getUsername().compareTo(username) == 0) {
					playerMatch = true;

					try {
						player.tryConfirm(userWhoInvitedYou);

						userWhoInvitedYou.getPrintWriter().println(ServerResponse.sendInvitationMessage(player, userWhoInvitedYou));
						userWhoInvitedYou.getPrintWriter().flush();

						message.append(ServerResponse.confirmationInvitationSent(userWhoInvitedYou));
						player.removeFromUserWhoInvitedYou(userWhoInvitedYou);

						userWhoInvitedYou.removeFromUsersYouInvited(player);

						// find the game the userWhoInvitedYou want play with you
						Game game = Server.getActiveGame(userWhoInvitedYou);
						// add your player in the game
						game.addPlayer(player);
					} catch (NoInvitationReceivedException e) {
						message.append("You can't confirm to ").append(FormatService.colorizeString(userWhoInvitedYou.getColor(), userWhoInvitedYou.getUsername())).append(" because you have not received his invitation.");
					} catch (InvitationAlreadySentException e) {
						message.append("You can't confirm to ").append(FormatService.colorizeString(userWhoInvitedYou.getColor(), userWhoInvitedYou.getUsername())).append(" because you already sent an invitation.;Please wait his confirmation to play with.");
					}

					break;
				}
			}
			
			if (! playerMatch) {
				message.append("This username does not exist.");
			}
		}

		return message.toString();
	}
}
