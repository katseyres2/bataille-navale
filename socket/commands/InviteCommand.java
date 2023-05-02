package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import game.Player;
import services.FormatService;
import services.expections.InvitationAlreadySentException;
import services.expections.UserAlreadyInvitedYouException;
import socket.Command;

public class InviteCommand extends Command {
    public InviteCommand() {
        super(
            "/invite",
            new String[]{"username"},
            null,
            Command.Role.AUTHENTICATED,
            "Invite your friends to play a new game."
        );
    }

    public String execute(String[] args, Player player, ArrayList<Player> players, Socket socket, PrintWriter pw, BufferedReader br) {
		String message = "";
		
		if (args.length != 2) {
			message += "You must specify <username>.";
		} else if (args[1].compareTo(player.getUsername()) == 0) {
			message += "You can't invite yourself. 🤪";
		} else {
			String username = args[1];
			boolean playerMatch = false;
			
			for (Player userToInvite : players) {
				if (userToInvite.isLogged() && userToInvite.getUsername().compareTo(username) == 0) {
					playerMatch = true;

					try {
						player.tryInvite(userToInvite);

						userToInvite.getPrintWriter().println(
							";" + FormatService.colorizeString(userToInvite.getColor(), "▓")
							+ " The user " + FormatService.colorizeString(player.getColor(), player.getUsername())
							+ " sent you a invitation, confirm by sending the command \"/confirm " + FormatService.colorizeString(player.getColor(), player.getUsername()) + "\".;;"
							+ FormatService.colorizeString(userToInvite.getColor(), "(" + userToInvite.getUsername() + ")──┤")
						);
						
						userToInvite.getPrintWriter().flush();
						message += "Invitation sent to " + FormatService.colorizeString(userToInvite.getColor(), userToInvite.getUsername()) + ".";

						for (Player cli : players) {
							if (cli.compareTo(player)) {
								cli.addInUsersYouInvited(userToInvite);
							}
						}

						userToInvite.addInUsersWhoInvitedYou(player);
					} catch (UserAlreadyInvitedYouException e) {
						message += "You can't invite " + FormatService.colorizeString(userToInvite.getColor() ,userToInvite.getUsername()) + " because you already received an invitation. Please send \"/confirm " + FormatService.colorizeString(userToInvite.getColor() ,userToInvite.getUsername()) + "\" to play with.";
					} catch (InvitationAlreadySentException e) {
						message += "You can't invite " + FormatService.colorizeString(userToInvite.getColor() ,userToInvite.getUsername()) + " because you already sent an invitation. Please wait for the \"/confirm\" to play with.";
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
