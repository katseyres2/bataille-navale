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

/**
 * The InviteCommand class represents a command to invite friends to play a new game.
 * It extends the Command class.
 */
public class InviteCommand extends Command {

	/**
	 * Constructs an InviteCommand object.
	 */
	public InviteCommand() {
		super(
				"/invite",
				null,
				new String[]{"username"},
				Command.Role.AUTHENTICATED,
				"Invite your friends to play a new game."
		);
	}

	/**
	 * Executes the invite command to invite a friend to play a game.
	 *
	 * @param args    The command arguments.
	 * @param client  The SocketClient object associated with the command.
	 * @param players The list of players in the game.
	 * @return The result message of the command execution.
	 */
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

			// Try to find the user you want to invite
			for (Player userToInvite : players) {
				// If you find the user and if they're connected
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
							// Try to invite the user, if you can't invite, it throws an exception
							player.tryInvite(userToInvite);
							userToInvite.getPrintWriter().println(ServerResponse.receiveAnInvitationFrom(player, userToInvite));
							userToInvite.getPrintWriter().flush();

							message.append(ServerResponse.invitationSentTo(userToInvite));

							player.addInUsersYouInvited(userToInvite);    // Add the invited user to your list of invitations
							userToInvite.addInUsersWhoInvitedYou(player); // Add the inviter to the invited user's list of invitations

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