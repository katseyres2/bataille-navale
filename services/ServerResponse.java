package services;

import Bots.Bot;
import socket.server.Player;
/**
 * Class that provides server response messages.
 */
public class ServerResponse {
    /**
     * Error message: Not connected.
     */
    public static final String notConnected = "Not connected";

    /**
     * Error message: Wrong number of parameters.
     */
    public static final String wrongNumberOfParameters = "Wrong number of parameters";

    /**
     * Error message: Wrong parameter format.
     */
    public static final String wrongParameterFormat = "Wrong parameter format";

    /**
     * Error message: Player not found.
     */
    public static final String playerNotFound = "Player not found";

    public static String playerNotFound(String username) {
        return playerNotFound + " : " + username;
    }

    public static String isNotBot(Player player) {
        return "Is not bot : " + player.getUsername();
    }

    /**
     * Success message: Action successful.
     */
    public static final String actionSuccessful = "Action successful";

    /**
     * Error message: You are already in a game.
     */
    public static final String alreadyInGame = "You are already in a game";

    /**
     * Error message: No game.
     */
    public static final String noGame = "No game";

    /**
     * Info message: That's your turn.
     */
    public static final String yourTurn = "That's your turn";

    /**
     * Error message: You can't invite yourself.
     */
    public static final String inviteYourself = "You can't invite yourself";

    /**
     * Error message: You're already connected.
     */
    public static final String alreadyConnected = "You're already connected";

    /**
     * Error message: You're connected on another device.
     */
    public static final String connectedOnAnotherDevice = "You're connected on another device";

    /**
     * Error message: Invalid credentials.
     */
    public static final String invalidCredentials = "Invalid credentials";

    /**
     * Info message: You're disconnected, see you soon.
     */
    public static final String seeYouSoon = "You're disconnected, see you soon";

    /**
     * Error message: You can't surrender yet.
     */
    public static final String youCantSurrender = "You can't surrender yet";

    /**
     * Error message: It's not your turn.
     */
    public static final String notYourTurn = "It's not your turn";

    /**
     * Error message: You are not playing a game.
     */
    public static final String notPlayingGame = "You are not playing a game";

    /**
     * Info message: Pong.
     */
    public static final String pong = "Pong";

    /**
     * Error message: Unsupported command.
     */
    public static final String unsupportedCommand = "Unsupported command";

    /**
     * Info message: Bot created.
     */
    public static final String botCreated = "Bot created";

    /**
     * Error message: Not enough players.
     */
    public static final String notEnoughPlayers = "Not enough players";

    /**
     * Info message: The game starts.
     */
    public static final String gameStarted = "The game starts";

    /**
     * Error message: The player is not connected.
     */
    public static final String playerNotConnected = "The player is not connected";
    public static final String notYouTurn = "Is not your turn";

    /**
     * Generates the message when a bot is invited.
     *
     * @param bot The bot being invited.
     * @return The invitation message.
     */
    public static String botInvited(Bot bot) {
        return "Bot " + bot.getUsername() + " invited";
    }

    /**
     * Message: Welcome back {username}.
     *
     * @param player The player.
     * @return The welcome message.
     */
    public static String welcome(Player player) {
        return "Welcome back " + player.getUsername() + ".";
    }

    /**
     * Generates the invitation message to be sent to another player.
     *
     * @param from The player who sent the invitation.
     * @param to   The player who receives the invitation.
     * @return The invitation message.
     */
    public static String sendInvitationMessage(Player from, Player to) {
        return ";" + FormatService.colorizeString(to.getColor(), "#")
                + " The user "
                + FormatService.colorizeString(from.getColor(), from.getUsername())
                + " accepts your invitation, start the game.;;"
                + FormatService.colorizeString(to.getColor(), "(" + to.getUsername() + ")--|");
    }

    /**
     * Generates the confirmation message for accepting an invitation.
     *
     * @param to The player who sent the invitation.
     * @return The confirmation message.
     */
    public static String confirmationInvitationSent(Player to) {
        return "You confirmed the invitation of " + FormatService.colorizeString(to.getColor(), to.getUsername()) + ".";
    }

    /**
     * Generates the error message when trying to invite a player who already received an invitation.
     *
     * @param to The player who is already invited.
     * @return The error message.
     */
    public static String youAlreadySentAnInvitation(Player to) {
        return "You can't invite " + FormatService.colorizeString(to.getColor(), to.getUsername()) +
                " because you already sent an invitation. Please wait for the \"/confirm\" to play with.";
    }

    /**
     * Generates the error message when trying to invite a player who already sent an invitation.
     *
     * @param from The player who already sent an invitation.
     * @return The error message.
     */
    public static String youAlreadyReceivedAnInvitation(Player from) {
        return "You can't invite " + FormatService.colorizeString(from.getColor(), from.getUsername()) +
                " because you already received an invitation. Please send \"/confirm " +
                FormatService.colorizeString(from.getColor(), from.getUsername()) + "\" to play with.";
    }

    /**
     * Generates the invitation message received from another player.
     *
     * @param current The current player.
     * @param from    The player who sent the invitation.
     * @return The invitation message.
     */
    public static String receiveAnInvitationFrom(Player current, Player from) {
        return ";" + FormatService.colorizeString(from.getColor(), "|")
                + " The user " + FormatService.colorizeString(current.getColor(), current.getUsername())
                + " sent you an invitation, confirm by sending the command \"/confirm " +
                FormatService.colorizeString(current.getColor(), current.getUsername()) + "\".;;" +
                FormatService.colorizeString(from.getColor(), "(" + from.getUsername() + ")--|");
    }

    /**
     * Generates the message indicating that an invitation has been sent to a player.
     *
     * @param to The player who received the invitation.
     * @return The message.
     */
    public static String invitationSentTo(Player to) {
        return "Invitation sent to " + FormatService.colorizeString(to.getColor(), to.getUsername()) + ".";
    }
}
