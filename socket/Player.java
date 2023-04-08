package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

import services.FormatService;
import services.expections.InvitationAlreadySentException;
import services.expections.NoInvitationReceivedException;
import services.expections.NotConnectedException;
import services.expections.UserAlreadyInvitedYouException;
import socket.commands.Command.Role;

public class Player {
	private String username;
	private String password;
	private String color;
	private int victories;
	private int defeats;
	private LocalDateTime lastConnectionAt;
	private ArrayList<Player> usersYouInvited;
	private ArrayList<Player> usersWhoInvitedYou;
	private Role role;
	public final SocketClient sender;
	public final SocketClient receiver;

	public Player(Socket sSender, PrintWriter pwSender, BufferedReader brSender, Socket sReceiver, PrintWriter pwReceiver, BufferedReader brReceiver) {
		receiver = new SocketClient(sReceiver, pwReceiver, brReceiver);
		sender = new SocketClient(sSender, pwSender, brSender);

		lastConnectionAt = LocalDateTime.now();
		usersYouInvited = new ArrayList<Player>();
		usersWhoInvitedYou = new ArrayList<Player>();
		defeats = 0;
		victories = 0;
		color = FormatService.getRandomColor();
		role = Role.ADMIN;
	}
	
	public void refreshConnection(Socket s, PrintWriter pw, BufferedReader br) {
		sender.setSocket(s);
		sender.setPrintWriter(pw);
		sender.setBufferedReader(br);
	}
	
	public void setColor(String value) {
		for (String color : FormatService.colors) {
			if (value.compareTo(color) == 0) {
				color = value;
				break;
			}
		}
	}
	
	public Role getRole() 								{ return role; }
	public int getDefeats() 							{ return defeats; }
	public String getColor()							{ return color; }
	public int getVictories() 							{ return victories; }
	private String getPassword()						{ return password; }
	public LocalDateTime getLastConnection()			{ return lastConnectionAt; }
	public ArrayList<Player> getUsersYouInvited()		{ return usersYouInvited; }
	public ArrayList<Player> getUsersWhoInvitedYou()	{ return usersWhoInvitedYou; }
	
	public void addDefeat()  								{ defeats++; }
	public void addVictory() 								{ victories++; }
	public void setRole(Role value)							{ role = value; }
	public void refreshLastConnection() 					{ lastConnectionAt = LocalDateTime.now(); }
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
		if (!sender.isLogged()) throw new NotConnectedException();
		sender.toggleLog();
		sender.clear();
	}

	public String getUsername() { return username; }
	public void setUsername(String value) { username = value; }
	public void setPassword(String value) { password = value; }

	public Thread buildSender() {
		return new Thread(new Runnable() {
			String message;

			@Override
			public void run() {
				while (true) {

					try {
						message = sender.getScanner().nextLine();
						sender.getPrintWriter().println(message);
						sender.getPrintWriter().flush();
					} catch (Exception e) {
						sender.getPrintWriter().println("/q!");
						sender.getPrintWriter().flush();
						break;
					}
				}

				boolean isClosed = sender.close();
				System.out.println(isClosed ? "\n\nGoodbye!\n" : "Unable to close the socket.");
			}
		});
	}

	public Thread buildReceiver() {
		return new Thread(new Runnable() {
			String message;

			@Override
			public void run() {
				try {
					message = sender.getBufferedReader().readLine();
					
					while (message != null) {
						String[] lines = message.split(";");
						System.out.print("\n" + String.join("\n", lines) + " ");
						message = sender.getBufferedReader().readLine();
					}

					System.out.println("Server out of service");
					sender.close();
				} catch (IOException e) {
					Thread.currentThread().interrupt();
					System.out.println("\n\nServer out of service ! Please contact the administrator.\n");
					System.exit(1);
				}
			}
		});
	}

	public void start(int port) {
		System.out.print("\nYour address is " + sender.getAddress() + ":" + Integer.toString(sender.getPort()));
		buildSender().start();
		buildReceiver().start();
	}
}
