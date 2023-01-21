package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import services.FormatService;
import services.expections.InvitationAlreadySentException;
import services.expections.NoInvitationReceivedException;
import services.expections.NotConnectedException;
import services.expections.UserAlreadyInvitedYouException;

public class Client extends SocketUser implements ISocket {
	private boolean logged;
	private String username;
	private String password;
	private String color;
	private LocalDateTime lastConnectionAt;
	private ArrayList<Client> usersYouInvited;
	private ArrayList<Client> usersWhoInvitedYou;

	public Client() {
		logged = false;
		lastConnectionAt = LocalDateTime.now();
		usersYouInvited = new ArrayList<Client>();
		usersWhoInvitedYou = new ArrayList<Client>();
		refreshColor();
	}

	public String getColor() {
		return color;
	}

	public String getPassword() {
		return password;
	}

	public ArrayList<Client> getUsersYouInvited() {
		return usersYouInvited;
	}

	public ArrayList<Client> getUsersWhoInvitedYou() {
		return usersWhoInvitedYou;
	}

	/**
	 * Checks if "this" can invite the parameter client. 
	 * @param client
	 * @throws InvitationAlreadySentException
	 * @throws UserAlreadyInvitedYouException
	 */
	public void tryInvite(Client client) throws InvitationAlreadySentException, UserAlreadyInvitedYouException {
		Server.logDebug("You    username : " + this.getUsername());
		Server.logDebug("Client username : " + client.getUsername());
		
		for (Client cli : this.getUsersYouInvited()) {
			Server.logDebug("You invited " + cli.getUsername());
			if (cli.compareTo(client)) {
				Server.logDebug("MATCH");
				throw new InvitationAlreadySentException();
			}
		}
		
		Server.logDebug("You did not invite " + client.getUsername());
		
		for (Client cli : this.usersWhoInvitedYou) {
			Server.logDebug("Invited you " + cli.getUsername());
			
			if (cli.compareTo(client)) {
				Server.logDebug("MATCH");
				throw new UserAlreadyInvitedYouException();
			}
		}

		Server.logDebug(client.getUsername() + " did not invited you.");
	}

	/**
	 * Checks if this can confirm the invitation sent by the client parameter.
	 * @param client
	 * @throws InvitationAlreadySentException
	 * @throws NoInvitationReceivedException
	 */
	public void tryConfirm(Client client) throws InvitationAlreadySentException, NoInvitationReceivedException {
		// iterates on each user you invited
		for (Client cli : this.usersYouInvited) {
			Server.logDebug("YOU INVITED : " + cli.getUsername());
			
			// checks if the client you invited matches with this client
			if (cli.compareTo(client)) {
				Server.logDebug("MATCH");
				// you sent an invitation thus you can not confirm
				throw new InvitationAlreadySentException();
			}
		}

		Server.logDebug("you did not invite " + client.getUsername());

		boolean invitationReceived = false;

		Server.logDebug("Your username : " + this.getUsername());
		Server.logDebug("The user you can confirm the invitation : " + client.getUsername());

		// iterates on each user who invited you
		for (Client cli : this.usersWhoInvitedYou) {
			Server.logDebug("INVITED YOU : " + cli.getUsername());

			// checks if this client invited you 
			if (cli.compareTo(client)) {
				// you received an invitation from the client parameter
				invitationReceived = true;
				Server.logDebug("MATCH");
				break;
			}
		}

		// if the client parameter did not send you an invitation, throws an exception
		if (! invitationReceived) throw new NoInvitationReceivedException();
		
		Server.logDebug(client.getUsername() + " did not send you an invitation.");
	}

	public void addInUsersYouInvited(Client client) {
		usersYouInvited.add(client);
	}

	public void removeFromUsersYouInvited(Client client) {
		usersYouInvited.remove(client);
	}

	public void addInUsersWhoInvitedYou(Client client) {
		usersWhoInvitedYou.add(client);
	}

	public void removeFromUserWhoInvitedYou(Client client) {
		usersWhoInvitedYou.remove(client);
	}

	public void refreshColor() {
		String[] colors = FormatService.colors;
		color =  colors[(new Random()).nextInt(colors.length - 1)];
	}

	public LocalDateTime getLastConnection() {
		return lastConnectionAt;
	}

	public void refreshLastConnection() {
		lastConnectionAt = LocalDateTime.now();
	}

	public boolean checkCredentials(String username, String password) {
		Server.logDebug("username not null : " + (username != null));
		Server.logDebug("password not null : " + (password != null));
		Server.logDebug("this username not null : " + (this.getUsername() != null));
		Server.logDebug("this password not null : " + (this.getPassword() != null));
		Server.logDebug("username match : " + (this.getUsername().compareTo(username) == 0));
		Server.logDebug("password match : " + (this.getPassword().compareTo(password) == 0));
		
		return (this.getUsername() != null && this.password != null) &&
			   (this.getUsername().compareTo(username) == 0 && this.getPassword().compareTo(password) == 0);
	}

	public boolean compareTo(Client client) {
		return this.getUsername() != null & this.getUsername().compareTo(client.getUsername()) == 0;
	}

	public void signOut() throws NotConnectedException {
		if (!logged) throw new NotConnectedException();
		logged = false;
	}

	public boolean isLogged() { return logged; }
	public String getUsername() { return username; }
	public void setUsername(String value) { username = value; }
	public void setPassword(String value) { password = value; }
	public void toggleLog() { logged = !logged; }

	public Thread buildSender(SocketUser user) {
		return new Thread(new Runnable() {
			String msg;

			@Override
			public void run() {
				while (true) {
					try {
						msg = user.getScanner().nextLine();
						user.getPrintWriter().println(msg);
						user.getPrintWriter().flush();
					} catch (Exception e) {
						user.getPrintWriter().println("/q!");
						user.getPrintWriter().flush();

						try {
							user.close();
						} catch (IOException er) {
							System.out.println(er.getMessage());
						}

						Thread.currentThread().interrupt();
						System.out.println("\n\nGoodbye!\n");
						break;
					}
				}

				try {
					close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		});
	}

	public Thread buildReceiver(SocketUser user) {
		return new Thread(new Runnable() {
			String msg;

			@Override
			public void run() {
				try {
					msg = user.getBufferedReader().readLine();
					
					while (msg != null) {
						String message = "";
						String[] lines = msg.split(";");

						if (msg.contains(";")) {
							for (String line : lines) {
								message += line;
								if (line.compareTo(lines[lines.length - 1]) != 0) {
									message += "\n";
								}
							}
						} else {
							message = msg + "\n";
						}

						System.out.print("\n" + message + "$ ");
						msg = user.getBufferedReader().readLine();
					}

					System.out.println("Server out of service");
					close();
				} catch (IOException e) {
					Thread.currentThread().interrupt();
					System.out.println("\n\nServer out of service ! Please contact the administrator.\n");
					System.exit(1);
				}
			}
		});
	}

	public void start(int port) {
		final Socket socket;
		final BufferedReader bufferedReader;
		final PrintWriter printWriter;
		
		try {
			String host = "127.0.0.1";
			// String host = "45.147.97.136";
			System.out.println(host + ":" + port);
			socket = new Socket(host, port);
			printWriter = new PrintWriter(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch(Exception e) {
			System.out.println("Unable to create the server socket");
			return;
		}

		final Scanner scanner = new Scanner(System.in);

		super.setScanner(scanner);
		super.setSocket(socket);
		super.setBufferedReader(bufferedReader);
		super.setPrintWriter(printWriter);
		
		System.out.print("\nYour address is " + super.getSocket().getInetAddress().getHostAddress() + ":" + Integer.toString(socket.getLocalPort()));

		buildSender(this).start();
		buildReceiver(this).start();
	}
}
