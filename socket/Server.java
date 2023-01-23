package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import game.Player;
import services.FormatService;
import socket.commands.ServerCommandHandler;

public class Server extends SocketUser implements ISocket {
	public static enum Log {
		INFO,
		DEBUG,
		WARNING,
		ERROR,
	}
	
	private ArrayList<Client> clients = new ArrayList<Client>();
	private static Log logLevel = Log.DEBUG;

	public static void setLogLevel(Log value) 	 { logLevel = value; }
	public static void logDebug(String value)    { if (logLevel == Log.DEBUG)    System.out.print(value); }
	public static void logInfo(String value)     { if (logLevel == Log.INFO)     System.out.print(value); }
	public static void logWarning(String value)  { if (logLevel == Log.WARNING)  System.out.print(value); }
	public static void logError(String value)    { if (logLevel == Log.ERROR)    System.out.print(value); }

	@Override
	public Thread buildSender(SocketUser user) {
		// create a new thread with a callback function
		// "Runnable" must implement run()
		return new Thread(new Runnable() {
			String msg;

			@Override
			public void run() {
				msg = "Welcome ! Use the command /signup to create a new account or /signin you're already registered.;;(?)──┤\n";
				user.getPrintWriter().print(msg);
				user.getPrintWriter().flush();

				while(true) {
					// wait for the user input
					msg = user.getScanner().nextLine();
					// send the message on the client host:port
					user.getPrintWriter().println(msg);
					// clear the memory cell
					user.getPrintWriter().flush();
				}
			}
		});
	}

	public Thread checkConnection() {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					for (Client client : clients) {
						if (client.isLogged() && !client.getLastConnection().plus(5, ChronoUnit.MINUTES).isAfter(FormatService.getCurrentTime())) {
							client.toggleLog();
							System.out.println(FormatService.serverLogPrefix(client) + FormatService.ANSI_YELLOW + "timeout connection, kick user." + FormatService.ANSI_RESET);
						}
					}
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		});
	}

	@Override
	public Thread buildReceiver(SocketUser user) {
		Client client = (Client)user;
		
		// create a new thread that receives sockets
		return new Thread(new Runnable() {
			String msg;
			ServerCommandHandler commandHandler = new ServerCommandHandler();
			
			@Override
			public void run() {
				client.refreshColor();

				try {
					// catch the message from the remote host
					msg = client.getBufferedReader().readLine();

					// while the buffer is not empty, there is still a message inside
					while (msg != null) {
						String[] args = msg.split(" ");
						String messageToSend = "";
						String logMessage = "";
						boolean closeClient = false;

						if (msg.length() > 0 && args[0].compareTo("/q!") != 0) {
							logMessage += args[0];
						}

						if (args[0].compareTo("/q!") == 0) {
							if (client.isLogged()) {
								commandHandler.signOut(client, clients);
							}

							closeClient = true;
							logMessage += FormatService.ANSI_RED + "has left the room." + FormatService.ANSI_RESET;
						}
						
						boolean allowedCommand = false;

						if (!client.isLogged()) {
							for (String cmd : new String[]{"/signin", "/signup", "/help"}) {						
								if (args[0].compareTo(cmd) == 0) {
									if (cmd.compareTo("/signin") == 0 || cmd.compareToIgnoreCase("signup") == 0) client.refreshColor();
									allowedCommand = true;
									break;
								}
							}
						}

						Server.logDebug("username = " + client.getUsername() + "\n");
							
						if (client.isLogged() || allowedCommand) {
							switch (args[0]) {
								case "/ping"	: messageToSend += commandHandler.pong();								break;
								case "/signin"	: messageToSend += commandHandler.signIn(args, client, clients);		break;
								case "/signup"	: messageToSend += commandHandler.signUp(args, client, clients);		break;
								case "/signout"	: messageToSend += commandHandler.signOut(client, clients);				break;
								case "/users"	: messageToSend += commandHandler.users(client, clients);				break;
								case "/help"	: messageToSend += commandHandler.help();								break;
								case "/invite"	: messageToSend += commandHandler.invite(args, client, clients);		break;
								case "/confirm"	: messageToSend += commandHandler.confirm(args, client, clients);		break;
								case "/info"	: messageToSend += commandHandler.info((Player) client);				break;
								default			: messageToSend += commandHandler.notFound();							break;
							}
						} else {
							messageToSend += "Not connected, please /signin or /signup. 🔒";
						}

						messageToSend = client.isLogged()
							? client.getColor() + "▓ " + FormatService.ANSI_RESET + messageToSend.replace(";", ";" + client.getColor() + "▓ " + FormatService.ANSI_RESET)
							: messageToSend;
						
						if (client.isLogged()) {
							client.refreshLastConnection();
							messageToSend += ";;" + client.getColor() + "(" + client.getUsername() + ")──┤" + FormatService.ANSI_RESET;
						} else {
							messageToSend += ";;(?)──┤";
						}

						client.getPrintWriter().println(messageToSend);
						client.getPrintWriter().flush();

						logMessage = FormatService.serverLogPrefix(client.isLogged() ? client : null) + logMessage;
						
						Server.logInfo(logMessage + "\n");

						if (closeClient) {
							try {
								client.close();
								return;
							} catch (IOException e) {
								System.out.print(" can't be closed.");
							}
						}

						msg = client.getBufferedReader().readLine();
					}

					System.out.println(client.getAddress() + ":" + client.getPort() +  " --> closed");
				
					// close the connection between the local host and the remote host
					client.getPrintWriter().close();
					client.close();
				} catch (IOException e) {
					try {
						client.close();
					} catch (IOException er) {
						System.out.println(er.getMessage());
					}
				}
			}
		});
	}

	public void start(int port) {
		final ServerSocket serverSocket;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Listening on " + serverSocket.getLocalSocketAddress());
		checkConnection().start();
		
		while (true) {
			final Socket socket;
			final BufferedReader bufferedReader;
			final PrintWriter printWriter;
			
			try {
				// if the host does not send a message, it can not create this object
				// and can not send messages to this host
				socket = serverSocket.accept();
				printWriter = new PrintWriter(socket.getOutputStream());
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// addHost(socket, bufferedReader, printWriter);
			} catch (IOException e) {
				System.out.println("Client not accepted.");
				break;
			}
			
			
			Scanner scanner = new Scanner(System.in);
			Client client = new Player();
			client.setSocket(socket);
			client.setPrintWriter(printWriter);
			client.setBufferedReader(bufferedReader);
			client.setScanner(scanner);

			System.out.print(FormatService.serverLogPrefix(client) + FormatService.ANSI_GREEN + "has joined the room." + FormatService.ANSI_RESET + "\n");

			// builds and starts threads
			buildSender(client).start();
			buildReceiver(client).start();
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println();
		}
	}
}
