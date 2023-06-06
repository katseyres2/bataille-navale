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

/**
 * The ConfirmCommand class represents a command for confirming an invitation.
 * It extends the Command class.
 */
public class ConfirmCommand extends Command {

	/**
	 * Constructs a ConfirmCommand object.
	 */
	public ConfirmCommand() {
		super("/confirm",
				null,
				new String[]{"username"},
				Command.Role.AUTHENTICATED,
				"Confirm the invitation."
		);
	}

	/**
	 * Executes the confirm command.
	 *
	 * @param args    The command arguments.
	 * @param client  The SocketClient object associated with the command.
	 * @param players The list of players in the game.
	 * @return The response message after executing the command.
	 */
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

						// Find the game the userWhoInvitedYou wants to play with you
						Game game = Server.getActiveGame(userWhoInvitedYou);
						// Add your player to the game
						game.addPlayer(player);
					} catch (NoInvitationReceivedException e) {
						message.append("You can't confirm to ")
								.append(FormatService.colorizeString(userWhoInvitedYou.getColor(), userWhoInvitedYou.getUsername()))
								.append(" because you have not received his invitation.");
					} catch (InvitationAlreadySentException e) {
						message.append("You can't confirm to ")
								.append(FormatService.colorizeString(userWhoInvitedYou.getColor(), userWhoInvitedYou.getUsername()))
								.append(" because you already sent an invitation. Please wait for their confirmation to play with you.");
					}

					break;
				}
			}

			if (!playerMatch) {
				message.append("This username does not exist.");
			}
		}

		return message.toString();
	}
}