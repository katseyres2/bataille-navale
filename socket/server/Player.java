package socket.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import services.FormatService;
import services.exceptions.InvitationAlreadySentException;
import services.exceptions.NoInvitationReceivedException;
import services.exceptions.NotConnectedException;
import services.exceptions.UserAlreadyInvitedYouException;
import socket.Command.Role;
import socket.client.SocketClient;

public class Player extends SocketClient {
	private String username;
	private String password;
	private String color;
	private int victories;
	private int defeats;
	private ArrayList<Player> usersYouInvited;
	private ArrayList<Player> usersWhoInvitedYou;
	private Role role;
	private boolean isBot;

	/**
	 * Constructs a Player object.
	 *
	 * @param sSender   The socket associated with the player.
	 * @param pwSender  The PrintWriter associated with the player.
	 * @param brSender  The BufferedReader associated with the player.
	 * @param username  The username of the player.
	 * @param password  The password of the player.
	 * @param isBot     Indicates whether the player is a bot or not.
	 */
	public Player(Socket sSender, PrintWriter pwSender, BufferedReader brSender, String username, String password,
				  boolean isBot) {
		super(sSender, pwSender, brSender);

		this.username = username;
		this.password = password;
		this.isBot = isBot;

		usersYouInvited = new ArrayList<Player>();
		usersWhoInvitedYou = new ArrayList<Player>();
		defeats = 0;
		victories = 0;
		color = FormatService.getRandomColor();
		role = Role.ADMIN;
	}

	/**
	 * Constructs a Player object using an existing SocketClient object.
	 *
	 * @param sc        The SocketClient object associated with the player.
	 * @param username  The username of the player.
	 * @param password  The password of the player.
	 */
	public Player(@NotNull SocketClient sc, String username, String password) {
		super(sc.getSocket(), sc.getPrintWriter(), sc.getBufferedReader());

		this.username = username;
		this.password = password;

		usersYouInvited = new ArrayList<Player>();
		usersWhoInvitedYou = new ArrayList<Player>();
		defeats = 0;
		victories = 0;
		color = FormatService.getRandomColor();
		role = Role.ADMIN;
	}

	/**
	 * Returns a string representation of the Player object.
	 *
	 * @return The string representation of the Player object.
	 */
	public String toString() {
		return "(" + username + ",V=" + victories + ",D=" + defeats + ")";
	}

	/**
	 * Sets the color of the player.
	 *
	 * @param value The color value to set.
	 */
	public void setColor(String value) {
		for (String color : FormatService.colors) {
			if (value.compareTo(color) == 0) {
				color = value; // Set the color value
				break;
			}
		}
	}

	/**
	 * Checks if the player is a bot.
	 *
	 * @return True if the player is a bot, false otherwise.
	 */
	public boolean isBot() {
		return isBot;
	}

	/**
	 * Retrieves the role of the player.
	 *
	 * @return The role of the player.
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Retrieves the number of defeats of the player.
	 *
	 * @return The number of defeats.
	 */
	public int getDefeats() {
		return defeats;
	}

	/**
	 * Retrieves the color of the player.
	 *
	 * @return The color of the player.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Retrieves the number of victories of the player.
	 *
	 * @return The number of victories.
	 */
	public int getVictories() {
		return victories;
	}

	/**
	 * Retrieves the password of the player.
	 *
	 * @return The password of the player.
	 */
	private String getPassword() {
		return password;
	}

	/**
	 * Retrieves the list of players invited by this player.
	 *
	 * @return The list of players invited by this player.
	 */
	public ArrayList<Player> getUsersYouInvited() {
		return usersYouInvited;
	}

	/**
	 * Retrieves the list of players who invited this player.
	 *
	 * @return The list of players who invited this player.
	 */
	public ArrayList<Player> getUsersWhoInvitedYou() {
		return usersWhoInvitedYou;
	}

	/**
	 * Increases the number of defeats for the player by 1.
	 */
	public void addDefeat() {
		defeats++;
	}

	/**
	 * Increases the number of victories for the player by 1.
	 */
	public void addVictory() {
		victories++;
	}

	/**
	 * Sets the role of the player.
	 *
	 * @param value The role to set.
	 */
	public void setRole(Role value) {
		role = value;
	}

	/**
	 * Adds the specified player to the list of players invited by this player.
	 *
	 * @param client The player to add to the list.
	 */
	public void addInUsersYouInvited(Player client) {
		usersYouInvited.add(client);
	}

	/**
	 * Adds the specified player to the list of players who invited this player.
	 *
	 * @param client The player to add to the list.
	 */
	public void addInUsersWhoInvitedYou(Player client) {
		usersWhoInvitedYou.add(client);
	}

	/**
	 * Removes the specified player from the list of players invited by this player.
	 *
	 * @param client The player to remove from the list.
	 */
	public void removeFromUsersYouInvited(Player client) {
		usersYouInvited.remove(client);
	}

	/**
	 * Removes the specified player from the list of players who invited this player.
	 *
	 * @param client The player to remove from the list.
	 */
	public void removeFromUserWhoInvitedYou(Player client) {
		usersWhoInvitedYou.remove(client);
	}

	/**
	 * Checks if "this" can invite the parameter client. 
	 * @param client
	 * @throws InvitationAlreadySentException
	 * @throws UserAlreadyInvitedYouException
	 */
	public void tryInvite(Player client) throws InvitationAlreadySentException, UserAlreadyInvitedYouException {
		for (Player cli : this.getUsersYouInvited()) {
			if (cli.compareTo(client)) {
				throw new InvitationAlreadySentException();
			}
		}
		
		for (Player cli : this.usersWhoInvitedYou) {
			if (cli.compareTo(client)) {
				throw new UserAlreadyInvitedYouException();
			}
		}
	}

	/**
	 * Checks if this can confirm the invitation sent by the client parameter.
	 * @param client
	 * @throws InvitationAlreadySentException
	 * @throws NoInvitationReceivedException
	 */
	public void tryConfirm(Player client) throws InvitationAlreadySentException, NoInvitationReceivedException {
		for (Player cli : this.usersYouInvited) {												// Iterates on each user you invited.
			if (cli.compareTo(client)) {														// Checks if the client you invited matches with this client.
				throw new InvitationAlreadySentException();										// You sent an invitation thus you can not confirm.
			}
		}

		boolean invitationReceived = false;

		for (Player cli : this.usersWhoInvitedYou) {											// Iterates on each user who invited you.
			if (cli.compareTo(client)) {														// Checks if this client invited you.
				invitationReceived = true;														// You received an invitation from the client parameter.
				break;
			}
		}

		if (! invitationReceived) throw new NoInvitationReceivedException();					// If the client parameter did not send you an invitation, throws an exception.
	}


	/**
	 * Checks if the provided credentials match the player's username and password.
	 *
	 * @param username The username to check.
	 * @param password The password to check.
	 * @return True if the credentials are valid, false otherwise.
	 */
	public boolean checkCredentials(String username, String password) {
		return (this.getUsername() != null && this.password != null) &&
				(this.getUsername().compareTo(username) == 0 && this.getPassword().compareTo(password) == 0);
	}

	/**
	 * Compares the player's username with the username of the specified client.
	 *
	 * @param client The player to compare with.
	 * @return True if the usernames match, false otherwise.
	 */
	public boolean compareTo(Player client) {
		return this.getUsername() != null && this.getUsername().compareTo(client.getUsername()) == 0;
	}

	/**
	 * Signs out the player from the system.
	 *
	 * @throws NotConnectedException If the player is not logged in.
	 */
	public void signOut() throws NotConnectedException {
		if (!super.isLogged()) {
			throw new NotConnectedException();
		}
		super.clear();
	}

	/**
	 * Retrieves the username of the player.
	 *
	 * @return The username of the player.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username of the player.
	 *
	 * @param value The username to set.
	 */
	public void setUsername(String value) {
		username = value;
	}

	/**
	 * Sets the password of the player.
	 *
	 * @param value The password to set.
	 */
	public void setPassword(String value) {
		password = value;
	}
}
