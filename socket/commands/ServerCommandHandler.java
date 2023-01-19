package socket.commands;

import java.util.ArrayList;

import services.FormatService;
import services.expections.NotConnectedException;
import socket.Client;

public class ServerCommandHandler {
	public String users(Client client, ArrayList<Client> clients) {
		String message = "List Of Users;"
			.concat("─┬────────────; │;");

		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getUsername().compareTo(client.getUsername()) == 0) continue;
			message += " ├─ " + (clients.get(i).isLogged() ? "🟢 " : "🔴 ") + clients.get(i).getUsername()
			         + " is " + (clients.get(i).isLogged() ? "online" : "offline") + " (last message sent : "
					 + FormatService.LocalDateTimeToString(clients.get(i).getLastConnection()) + ") "
					//  + "victories (" + 
					 + ";";
		}

		return message += " │; └────────────";
	}

	public String pong() {
		String message = "pong";
		return message;
	}

	public String signIn(String[] args, Client client, ArrayList<Client> clients) {
		String message = "";
		
		if (args.length != 3) {
			message += "You must specify <username> <password>.";
		} else {
			if (client.isLogged()) {
				message += "You're already connected.";
			} else {
				boolean userMatch = false;

				for (Client registeredClient : clients) {
					String username = args[1];
					String password = args[2];
					
					if (registeredClient.getUsername().compareTo(username) == 0) {
						userMatch = true;

						if (registeredClient.checkCredentials(username, password)) {
							if (registeredClient.isLogged()) {
								message += "You're connected on another device.";
							} else {
								clients.remove(registeredClient);
								client.setUsername(username);
								client.setPassword(password);
								client.toggleLog();
								clients.add(client);				

								message += "Welcome back " + registeredClient.getUsername() + " ! 😎;;";
								message += users(registeredClient, clients);
							}
						} else {
							message += "Invalid credentials.";
						}

						break;
					}
				}
				
				if (!userMatch) {
					message += "The user does not exist.";
				}
			}
		}

		return message;
	}

	public String signUp(String[] args, Client client, ArrayList<Client> clients) {
		String message = "";
		
		if (client.isLogged()) {
			message += "You're already connected.";
		} else if (args.length != 3) {
			message += "You must specify <username> <password>.";
		} else if (args[1].length() > FormatService.USERNAME_MAX_LENGTH) {
			message += "The username must have up to "+ FormatService.USERNAME_MAX_LENGTH +" characters.";
		} else if (args[2].length() > FormatService.PASSWORD_MAX_LENGTH) {
			message += "The password must have up to "+ FormatService.PASSWORD_MAX_LENGTH +" characters.";
		} else {
			boolean usernameExists = false;
			
			for (Client registeredClient : clients) {
				if (registeredClient.getUsername().compareTo(args[1]) == 0) {
					usernameExists = true;
					break;
				}
			}

			if (usernameExists) {
				message += "This username already exists.";
			} else {
				client.setUsername(args[1]);
				client.setPassword(args[2]);
				client.toggleLog();
				clients.add(client);
				
				message += "Your have created a new account, welcome " + client.getUsername() + ".;;";
				message += users(client, clients);
			}
		}

		return message;
	}

	public String signOut(Client client, ArrayList<Client> clients) {
		try {
			client.signOut();
		} catch (NotConnectedException e) {
			System.out.println(e.getMessage());
		}

		return "You're disconnected, see you soon. 🙂";
	}

	public String notFound() {
		return "Command not found, please send the command /help for more information.";
	}

	public String help() {
		String message = "List Of Commands;─┬───────────────; │;";

		for (Command command : ClientCommandHandler.COMMANDS) {
			int totalLength = 30;
			int fillWithSpace = totalLength - command.getName().length() - command.getParameters().length();
			message += " ├─ /" + command.getName() + " " + command.getParameters() + " ".repeat(fillWithSpace) + " : " + command.help() + ";";
		}

		message += " │;";
		message += " └───────────────";

		return message;
	}

	public String invite(String[] args, Client client, ArrayList<Client> clients) {
		String message = "";
		
		if (args.length != 2) {
			message += "You must specify <username>.";
		} else if (args[1].compareTo(client.getUsername()) == 0) {
			message += "You can't invite yourself. 🤪";
		} else {
			String username = args[1];
			boolean clientMatch = false;
			
			for (Client registeredClient : clients) {
				if (registeredClient.isLogged() && registeredClient.getUsername().compareTo(username) == 0) {
					registeredClient.getPrintWriter().println(";The user " + client.getColor() + client.getUsername() + FormatService.ANSI_RESET + " sent you an invitation.;;" + registeredClient.getColor() + "(" + registeredClient.getUsername() + ")──┤" + FormatService.ANSI_RESET);
					registeredClient.getPrintWriter().flush();
					message += "Invitation sent to " + registeredClient.getColor() + registeredClient.getUsername() + FormatService.ANSI_RESET + ".";
					clientMatch = true;
					break;
				}
			}

			if (!clientMatch) {
				message += "There is no user with this username.";
			}
		}

		return message;
	}
}
