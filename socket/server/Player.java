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

	public Player(Socket sSender, PrintWriter pwSender, BufferedReader brSender, String username, String password, boolean isBot) {
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
	
	public void setColor(String value) {
		for (String color : FormatService.colors) {
			if (value.compareTo(color) == 0) {
				color = value;
				break;
			}
		}
	}

	public boolean isBot() {
		return false;
	}

	public Role getRole() 								{ return role; }
	public int getDefeats() 							{ return defeats; }
	public String getColor()							{ return color; }
	public int getVictories() 							{ return victories; }
	public String getPassword()						{ return password; }
	public ArrayList<Player> getUsersYouInvited()		{ return usersYouInvited; }
	public ArrayList<Player> getUsersWhoInvitedYou()	{ return usersWhoInvitedYou; }
	
	public void addDefeat()  								{ defeats++; }
	public void addVictory() 								{ victories++; }
	public void setRole(Role value)							{ role = value; }
	public void addInUsersYouInvited(Player client) 		{ usersYouInvited.add(client); }
	public void addInUsersWhoInvitedYou(Player client) 		{ usersWhoInvitedYou.add(client); }
	public void removeFromUsersYouInvited(Player client) 	{ usersYouInvited.remove(client); }
	public void removeFromUserWhoInvitedYou(Player client) 	{ usersWhoInvitedYou.remove(client); }

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


	public boolean checkCredentials(String username, String password) {
		return (this.getUsername() != null && this.password != null) && (this.getUsername().compareTo(username) == 0 && this.getPassword().compareTo(password) == 0);
	}

	public boolean compareTo(Player client) {
		return this.getUsername() != null & this.getUsername().compareTo(client.getUsername()) == 0;
	}

	public void signOut() throws NotConnectedException {
		if (!super.isLogged()) throw new NotConnectedException();
		super.clear();
	}

	public String getUsername() { return username; }
	public void setUsername(String value) { username = value; }
	public void setPassword(String value) { password = value; }
}
