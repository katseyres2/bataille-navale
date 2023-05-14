package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Game;
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

	public String execute(String[] args, Player player, ArrayList<Player> players) {
		String message = "";

		if (args.length != 2) {
			message += "You must specify <username>.";
		} else if (Server.getActiveGame(player) != null) {
			message += "You already play a game.";
		} else {
			String username = args[1];
			boolean playerMatch = false;

			for (Player userWhoInvitedYou : players) {
				if (userWhoInvitedYou.isLogged() && userWhoInvitedYou.getUsername().compareTo(username) == 0) {
					playerMatch = true;

					try {
						player.tryConfirm(userWhoInvitedYou);

						userWhoInvitedYou.getPrintWriter().println(";"
							+ FormatService.colorizeString(userWhoInvitedYou.getColor(), "#")
							+ " The user "
							+ FormatService.colorizeString(player.getColor(), player.getUsername())
							+ " accepts your invitation, start the game.;;"
							+ FormatService.colorizeString(userWhoInvitedYou.getColor(), "(" + userWhoInvitedYou.getUsername() + ")--|")
						);

						userWhoInvitedYou.getPrintWriter().flush();
						message += "You confirmed the invitation of " + FormatService.colorizeString(userWhoInvitedYou.getColor(), userWhoInvitedYou.getUsername()) + ".";
						
						for (Player cli : players) {
							if (cli.compareTo(player)) {
								cli.removeFromUserWhoInvitedYou(userWhoInvitedYou);
								Server.logDebug(cli.getUsername() + ", the user who invited you " + userWhoInvitedYou.getUsername() + " is removed from your list.\n");
							}
						}
						
						userWhoInvitedYou.removeFromUsersYouInvited(player);

						// find the game the userWhoInvitedYou want play with you
						Game game = Server.getActiveGame(userWhoInvitedYou);
						// add your player in the game
						game.addPlayer(player);
						// start the game
						game.run();
					} catch (NoInvitationReceivedException e) {
						message += "You can't confirm to " + FormatService.colorizeString(userWhoInvitedYou.getColor(), userWhoInvitedYou.getUsername()) + " because you have not received his invitation.";
					} catch (InvitationAlreadySentException e) {
						message += "You can't confirm to " + FormatService.colorizeString(userWhoInvitedYou.getColor(), userWhoInvitedYou.getUsername()) + " because you already sent an invitation.;Please wait his confirmation to play with.";
					}

					break;
				}
			}
			
			if (! playerMatch) {
				message += "This username does not exist.";
			}
		}

		return message;
	}
}
