package socket.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import services.expections.NotConnectedException;
import socket.Client;

public class ServerCommandHandler {
	private PrintWriter printWriter;
	
	public ServerCommandHandler(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	public void users(ArrayList<Client> clients) {
		String message = "";
		
		for (int i = 0; i < clients.size(); i++) {
			message += clients.get(i).getAddress() + ":" + clients.get(i).getPort() + ", ";
		}

		printWriter.println(message);
		printWriter.flush();
	}

	public void pong() {
		String message = "pong";
		printWriter.println(message);
		printWriter.flush();
	}

	public void invite() {
		String message = "TODO";
		printWriter.println(message);
		printWriter.flush();
	}

	public void signIn(String[] args, Client client, ArrayList<Client> clients) {
		if (client.isLogged()) {
			client.getPrintWriter().println("You're already connected.");
			return;
		}

		if (args.length != 3) {
			client.getPrintWriter().println("You must specify <username> <password>.");
			return;
		}

		for (Client registeredClient : clients) {
			if (registeredClient.checkCredentials(args[1], args[2]) && !registeredClient.isLogged()) {
				registeredClient.toggleLog();
				registeredClient.setBufferedReader(client.getBufferedReader());										
				registeredClient.setPrintWriter(client.getPrintWriter());										
				registeredClient.setScanner(client.getScanner());
				registeredClient.setSocket(client.getSocket());

				client.getPrintWriter().println("Welcome " + registeredClient.getUsername());
				return;
			}
		}

		client.getPrintWriter().println("Invalid credentials.");
	}

	public void signUp(String[] args, Client client, ArrayList<Client> clients) {
		if (client.isLogged()) {
			client.getPrintWriter().println("You're already connected.");
			return;
		}

		if (args.length != 3) {
			client.getPrintWriter().println("You must specify <username> <password>.");
			return;
		}

		boolean usernameExists = false;

		for (Client registeredClient : clients) {
			if (registeredClient.getUsername().compareTo(args[1]) == 0) {
				usernameExists = true;
				break;
			}
		}

		if (usernameExists) {
			client.getPrintWriter().println("This username already exists.");
			return;
		}

		client.setUsername(args[1]);
		client.setPassword(args[2]);
		client.toggleLog();
		clients.add(client);

		client.getPrintWriter().println("Your have created a new account, welcome " + client.getUsername() + ".");
	}

	public void signOut(Client client) {
		try {
			client.signOut();
		} catch (NotConnectedException e) {
			System.out.println(e.getMessage());
		}

		client.getPrintWriter().println("You're disconnected.");
	}

	public void notFound() {
		String message = "Command not found, please send the command /help for more information.";
		printWriter.println(message);
		printWriter.flush();
	}

	public void help() {
		String message = "List Of Commands;─┬───────────────; │;";

		for (Command command : ClientCommandHandler.COMMANDS) {
			int totalLength = 30;
			int fillWithSpace = totalLength - command.getName().length() - command.getParameters().length();
			message += " ├─ /" + command.getName() + " " + command.getParameters() + " ".repeat(fillWithSpace) + " : " + command.help() + ";";
		}

		message += " │;";
		message += " └───────────────";

		printWriter.println(message);
		printWriter.flush();
	}
}
