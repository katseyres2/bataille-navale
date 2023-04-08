package socket.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import game.Game;
import services.FormatService;
import services.expections.InvitationAlreadySentException;
import services.expections.NoInvitationReceivedException;
import services.expections.UserAlreadyInvitedYouException;
import socket.Player;
import socket.Server;
import socket.commands.Command.Role;

public class ServerCommandHandler {
	private static final ArrayList<Command> COMMANDS = new ArrayList<Command>(Arrays.asList(
		new Command(
			"/help",
			null,
			null,
			Command.Role.UNDEFINED,
			"Display all available commands."
		),
		new Command("/ping",
			null,
			null,
			Command.Role.UNDEFINED,
			"Send a ping request the server to check if you're connected."
		),
		new Command("/invite",
			new String[]{"username"},
			null,
			Command.Role.AUTHENTICATED,
			"Invite your friends to play a new game."
		),
		new Command("/confirm",
			null,
			new String[]{"username"},
			Command.Role.AUTHENTICATED,
			"Confirm the invitation."
		),
		new Command("/users",
			null,
			null,
			Command.Role.AUTHENTICATED,
			"List all connected users."
		),
		new Command("/signin",
			null,
			new String[]{"username",
			"password"},
			Command.Role.UNDEFINED,
			"Connect to you account."
		),
		new Command(
			"/signup",
		 	null,
		 	new String[]{"username", "password"},
		 	Command.Role.UNDEFINED,
		 	"Create a new account."
		),
		new Command("/signout",
		 	null,
		 	null,
		 	Command.Role.AUTHENTICATED,
		 	"Disconnect from your account."
		),
		new Command("/info",
		 	null,
		 	null,
		 	Command.Role.AUTHENTICATED,
		 	"Get all information about the player.")
	));

	//----------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param line
	 * @param player
	 * @param players
	 * @return
	 */
	public static String executeCommand(String line, Socket s, PrintWriter pw, BufferedReader br, ArrayList<Player> players) {
		if (line == null || s == null || pw == null || br == null || players == null) return "";

		String messageToSend = "";
		Player player = null;
		String[] args = line.split(" ");
		Role role = Role.UNDEFINED;

		for (Player p : players) {
			if (p.sender.getSocket() == s) {
				player = p;
				break;
			}
		}

		if (player != null) {
			role = player.getRole();
		}

		for (Command c : COMMANDS) {
			if (c.getName().contains(args[0])) {
				if (c.hasPermission(role)) {
					switch (args[0]) {
						case "/signout"	: messageToSend += signOut(player, players);			break;
						case "/users"	: messageToSend += users(player, players);				break;
						case "/invite"	: messageToSend += invite(args, player, players);		break;
						case "/confirm"	: messageToSend += confirm(args, player, players);		break;
						case "/info"	: messageToSend += info(player);						break;
						case "/help"	: messageToSend += help(role);							break;
						case "/signin"	: messageToSend += signIn(args, s, pw, br, players);	break;
						case "/signup"	: messageToSend += signUp(args, s, pw, br, players);	break;
						case "/ping"	: messageToSend += pong();								break;
						default			: messageToSend += notFound();							break;
					}
				} else {
					messageToSend += notFound();
				}

				return  messageToSend;
			}
		}

		return notFound();
	}

	//----------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------

	private static String users(Player client, ArrayList<Player> clients) {
		String message = "List Of Users;"
			.concat("─┬────────────; │;");

		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getUsername().compareTo(client.getUsername()) == 0) continue;
			message += " ├─ " + (clients.get(i).sender.isLogged() ? "🟢 " : "🔴 ") + clients.get(i).getUsername()
			         + " is " + (clients.get(i).sender.isLogged() ? "online" : "offline") + ", last message sent "
					 + FormatService.LocalDateTimeToString(clients.get(i).getLastConnection())
					 + ", victories: " + clients.get(i).getVictories() + ", defeats:" + clients.get(i).getDefeats()
					 + ";";
		}

		return message += " │; └────────────";
	}

	private static String pong() {
		String message = "pong";
		return message;
	}

	private static String signIn(String[] args, Socket s, PrintWriter pw, BufferedReader br, ArrayList<Player> players) {
		String message = "";
		String username;
		String password;
		boolean usernameMatched = false;

		for (Player p : players) {
			if (p.sender.getSocket() == s) {
				message += "You're already connected.";
				return message;
			}
		}

		if (args.length != 3) {
			message += "You must specify <username> <password>.";
		} else {
			username = args[1];
			password = args[2];

			for (Player p : players) {
				if (p.getUsername().compareTo(username) == 0) {
					usernameMatched = true;

					if (p.checkCredentials(username, password)) {
						if (p.sender.isLogged()) {
							message += "You're connected on another device.";
						} else {
							p.sender.toggleLog();
							p.refreshConnection(s, pw, br);
							message += "Welcome back " + p.getUsername() + ".";
						}
					} else {
						message += "Invalid credentials.";
					}

					break;
				}
			}

			if (! usernameMatched) {
				message += "The user " + username + " does not exist.";
			}
		}

		return message;
	}

	/**
	 * Create a new Client with new credentials
	 * @param args
	 * @param client
	 * @param clients
	 * @return
	 */
	private static String signUp(String[] args, Socket s, PrintWriter pw, BufferedReader br, ArrayList<Player> players) {
		String message = "";
		Player player = null;
		boolean usernameAlreadyExists = false;
		String username;
		String password;

		for (Player p : players) {
			if (p.sender.getSocket() == s) {
				player = p;
				break;
			}
		}

		
		if (args.length != 3) {
			message += "You must specify <username> <password>.";
		} else if (args[1].length() > FormatService.USERNAME_MAX_LENGTH) {
			message += "The username must have up to "+ FormatService.USERNAME_MAX_LENGTH +" characters.";
		} else if (args[2].length() > FormatService.PASSWORD_MAX_LENGTH) {
			message += "The password must have up to "+ FormatService.PASSWORD_MAX_LENGTH +" characters.";
		} else if (player != null) {
			message += "You're already connected.";
		} else {
			username = args[1];
			password = args[2];
			
			for (Player p : players) {
				if (p.getUsername().compareTo(username) == 0) {
					usernameAlreadyExists = true;
					message += "This username already exists.";
					break;
				}
			}
			
			if (! usernameAlreadyExists) {
				player = new Player(s, pw, br, null, null, null);
				player.setUsername(username);
				player.setPassword(password);
				player.sender.toggleLog();
				players.add(player);
				
				message += "Your have created a new account, welcome " + player.getUsername() + ".;;";
				message += users(player, players);
				Server.appendFile(Server.CREDENTIALS_PATH, username + " " + password + " ");
			}

		}

		return message;
	}

	private static String signOut(Player player, ArrayList<Player> players) {
		for (Player p : players) {
			if (p.sender.getSocket() == player.sender.getSocket()) {
				p.sender.clear();													// Assigns null to all socket parameters.
				if (p.sender.isLogged()) p.sender.toggleLog();							// Sets to isLogged to false.
				break;
			}
		}

		return "You're disconnected, see you soon. 🙂";
	}

	private static String notFound() {
		return "Command not found, please send the command /help for more information.";
	}

	private static String help(Role role) {
		String message = "List Of Commands;─┬───────────────; │;";

		for (Command command : COMMANDS) {
			if (command.hasPermission(role)) {
				int totalLength = 30;
				int fillWithSpace = totalLength - command.getName().length() - command.getParameters().length();
				message += " ├─ " + command.getName() + " " + command.getParameters() + " ".repeat(fillWithSpace) + " : " + command.help() + ";";
			}
		}

		message += " │;";
		message += " └───────────────";

		return message;
	}

	private static String info(Player player) {
		String message = "";

		message += "Username    : " + player.getUsername();
		message += ";Victories   : " + player.getVictories();
		message += ";Defeats     : " + player.getDefeats();

		return message;
	}

	private static String confirm(String[] args, Player client, ArrayList<Player> clients) {
		String message = "";

		if (args.length != 2) {
			message += "You must specify <username>.";
		} else {
			String username = args[1];
			boolean clientMatch = false;

			for (Player userWhoInvitedYou : clients) {
				if (userWhoInvitedYou.sender.isLogged() && userWhoInvitedYou.getUsername().compareTo(username) == 0) {
					clientMatch = true;

					try {
						client.tryConfirm(userWhoInvitedYou);

						userWhoInvitedYou.sender.getPrintWriter().println(";"
							+ FormatService.colorizeString(userWhoInvitedYou.getColor(), "▓")
							+ " The user "
							+ FormatService.colorizeString(client.getColor(), client.getUsername())
							+ " sent you a /confirm, create a <LINK BETWEEN THEM>;;"
							+ FormatService.colorizeString(userWhoInvitedYou.getColor(), "(" + userWhoInvitedYou.getUsername() + ")──┤")
						);

						userWhoInvitedYou.sender.getPrintWriter().flush();
						message += "You confirmed the invitation of " + FormatService.colorizeString(userWhoInvitedYou.getColor(), userWhoInvitedYou.getUsername()) + ".";
						
						for (Player cli : clients) {
							if (cli.compareTo(client)) {
								cli.removeFromUserWhoInvitedYou(userWhoInvitedYou);
								Server.logDebug(cli.getUsername() + ", the user who invited you " + userWhoInvitedYou.getUsername() + " is removed from your list.\n");
							}
						}
						
						userWhoInvitedYou.removeFromUsersYouInvited(client);
						
						Game game = new Game();
						game.addPlayer(client);
						game.addPlayer(userWhoInvitedYou);
						Server.pushGame(game);
						game.run();
					} catch (NoInvitationReceivedException e) {
						message += "You can't confirm to " + FormatService.colorizeString(userWhoInvitedYou.getColor(), userWhoInvitedYou.getUsername()) + " because you have not received his invitation.";
					} catch (InvitationAlreadySentException e) {
						message += "You can't confirm to " + FormatService.colorizeString(userWhoInvitedYou.getColor(), userWhoInvitedYou.getUsername()) + " because you already sent an invitation.;Please wait his confirmation to play with.";
					}

					break;
				}
			}
			
			if (! clientMatch) {
				message += "This username does not exist.";
			}
		}

		return message;
	}

	private static String invite(String[] args, Player client, ArrayList<Player> clients) {
		String message = "";
		
		if (args.length != 2) {
			message += "You must specify <username>.";
		} else if (args[1].compareTo(client.getUsername()) == 0) {
			message += "You can't invite yourself. 🤪";
		} else {
			String username = args[1];
			boolean clientMatch = false;
			
			for (Player userToInvite : clients) {
				if (userToInvite.sender.isLogged() && userToInvite.getUsername().compareTo(username) == 0) {
					clientMatch = true;

					try {
						client.tryInvite(userToInvite);

						userToInvite.sender.getPrintWriter().println(
							";" + FormatService.colorizeString(userToInvite.getColor(), "▓")
							+ " The user " + FormatService.colorizeString(client.getColor(), client.getUsername())
							+ " sent you a invitation, confirm by sending the command \"/confirm " + FormatService.colorizeString(client.getColor(), client.getUsername()) + "\".;;"
							+ FormatService.colorizeString(userToInvite.getColor(), "(" + userToInvite.getUsername() + ")──┤")
						);
						
						userToInvite.sender.getPrintWriter().flush();
						message += "Invitation sent to " + FormatService.colorizeString(userToInvite.getColor(), userToInvite.getUsername()) + ".";

						for (Player cli : clients) {
							if (cli.compareTo(client)) {
								cli.addInUsersYouInvited(userToInvite);
							}
						}

						userToInvite.addInUsersWhoInvitedYou(client);
					} catch (UserAlreadyInvitedYouException e) {
						message += "You can't invite " + FormatService.colorizeString(userToInvite.getColor() ,userToInvite.getUsername()) + " because you already received an invitation. Please send \"/confirm " + FormatService.colorizeString(userToInvite.getColor() ,userToInvite.getUsername()) + "\" to play with.";
					} catch (InvitationAlreadySentException e) {
						message += "You can't invite " + FormatService.colorizeString(userToInvite.getColor() ,userToInvite.getUsername()) + " because you already sent an invitation. Please wait for the \"/confirm\" to play with.";
					}

					break;
				}
			}

			if (! clientMatch) {
				message += "This username does not exist.";
			}
		}

		return message;
	}
}
