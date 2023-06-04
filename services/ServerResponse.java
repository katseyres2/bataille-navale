package services;

import socket.server.Player;

public class ServerResponse {
    public static final String notConnected = "Not connected";
    public static final String wrongNumberOfParameters = "Wrong number of parameters";
    public static final String wrongParameterFormat = "Wrong parameter format";
    public static final String playerNotFound = "Player not found";
    public static final String actionSuccessful = "Action successful";
    public static final String alreadyInGame = "You are already in a game";
    public static final String noGame = "No game";
    public static final String yourTurn = "That's your turn";
    public static final String inviteYourself = "You can't invite yourself";
    public static final String alreadyConnected = "You're already connected";
    public static final String connectedOnAnotherDevice = "You're connected on another device";
    public static final String invalidCredentials = "Invalid credentials";
    public static final String seeYouSoon = "You're disconnected, see you soon";
    public static final String youCantSurrend = "You can't surrend yet";
    public static final String notYouTurn = "It's not your turn";
    public static final String notPlayingGame = "You are not playing a game";
    public static final String pong = "pong";

    public static String welcome(Player player) {
        return "Welcome back " + player.getUsername() + ".";
    }

    public static String sendInvitationMessage(Player from, Player to) {
        return ";" + FormatService.colorizeString(to.getColor(), "#")
                + " The user "
                + FormatService.colorizeString(from.getColor(), from.getUsername())
                + " accepts your invitation, start the game.;;"
                + FormatService.colorizeString(to.getColor(), "(" + to.getUsername() + ")--|");
    }

    public static String confirmationInvitationSent(Player to) {
        return "You confirmed the invitation of " + FormatService.colorizeString(to.getColor(), to.getUsername()) + ".";
    }

    public static String youAlreadySentAnInvitation(Player to) {
        return "You can't invite " + FormatService.colorizeString(to.getColor() ,to.getUsername()) + " because you already sent an invitation. Please wait for the \"/confirm\" to play with.";
    }

    public static String youAlreadyReceivedAnInvitation(Player from) {
        return "You can't invite " + FormatService.colorizeString(from.getColor() ,from.getUsername()) + " because you already received an invitation. Please send \"/confirm " + FormatService.colorizeString(from.getColor() ,from.getUsername()) + "\" to play with.";
    }

    public static String receiveAnInvitationFrom(Player current, Player from) {
        return ";" + FormatService.colorizeString(from.getColor(), "|")
            + " The user " + FormatService.colorizeString(current.getColor(), current.getUsername())
            + " sent you a invitation, confirm by sending the command \"/confirm " + FormatService.colorizeString(current.getColor(), current.getUsername()) + "\".;;"
            + FormatService.colorizeString(from.getColor(), "(" + from.getUsername() + ")--|");
    }

    public static String invitationSentTo(Player to) {
        return "Invitation sent to " + FormatService.colorizeString(to.getColor(), to.getUsername()) + ".";
    }
}
