package socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import game.Game;
import interfaces.IServer;
import interfaces.ISocketBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import services.DiscoveryService;
import services.FormatService;
import services.LogService;
import socket.Command;
import socket.Message;

public class Server extends LogService implements IServer,ISocketBuilder {
	public static int PORT = 5000;

	public Server() {
		super(LOG_PATH);
	}

	private ArrayList<Player> players = new ArrayList<Player>();
	private static ArrayList<Game> games = new ArrayList<Game>();
	private static LEVEL logLevel = LEVEL.INFO;
	private static final Path LOG_PATH = Path.of("../data/server.log");
	public static final Path CREDENTIALS_PATH = Path.of("../data/credentials.txt");
	public static ArrayList<Message> messages = new ArrayList<>();

	public static @NotNull ArrayList<Message> findMessagesFrom(String username, int limit) {
		var messageList = findMessagesFrom(username);
		if (messageList.size() < limit) return messageList;

		ArrayList<Message> output = new ArrayList<>();
		for (int i = 0; i < limit; i++)
			output.add(messageList.get(i));

		return output;
	}

	public static @NotNull ArrayList<Message> findMessagesFrom(String username) {
		ArrayList<Message> output = new ArrayList<>();
		for (Message m : messages) {
			if (m.getFrom().getUsername().compareTo(username) == 0) output.add(m);
		}
		return output;
	}

	public static @Nullable Message findLastMessageFrom(String username) {
		var messagesFrom = findMessagesFrom(username);
		if (messagesFrom.size() == 0) return null;
		return messagesFrom.get(messagesFrom.size() - 1);
	}

	public static void saveMessage(@NotNull Message message) {
		System.out.println("Message saved : " + message.getText() + " from " + message.getFrom().getUsername());
		messages.add(message);
	}

	public static void logInfo(String value)     { if (logLevel == LEVEL.INFO)     System.out.print(value); }

	public static void setLogLevel(LEVEL value)  { logLevel = value; }

	public static void logDebug(String value)    { if (logLevel == LEVEL.DEBUG)    System.out.print(value); }

	public static void logError(String value)    { if (logLevel == LEVEL.ERROR)    System.out.print(value); }

	public static void logWarning(String value)  { if (logLevel == LEVEL.WARNING)  System.out.print(value); }

	public static void pushGame(Game game) {
		if (games.contains(game) || game == null) return;
		games.add(game);
	}

	public static @Nullable Game getActiveGame(Player player) {
		for (Game g : games)
			if (g.hasPlayer(player))
				return g;
		return null;
	}

	public Thread buildSender(PrintWriter pw) {
		return new Thread(new Runnable() {        // Create a new thread with a callback function. "Runnable" must implement run().
			@Override
			public void run() {
				String msg = "Welcome ! Use the command /signup to create a new account or /signin you're already registered.;;(?)--|\n";
				Scanner scan = new Scanner(System.in);

				pw.print(msg);
				pw.flush();

				while(true) {
					try {
						msg = scan.nextLine();        // Wait for the user input.
						pw.println(msg);            // Send the message on the client host:port.
						pw.flush();                    // Clear the memory cell.
					} catch (NoSuchElementException | IllegalStateException e) {
						System.out.println(e.getMessage());
						break;
					}
				}
			}
		});
	}

	public Thread checkConnection() {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					for (Player client : players) {
						if (client.isLogged() && !client.getLastConnection().plus(5, ChronoUnit.MINUTES).isAfter(FormatService.getCurrentTime())) {
							client.clear();
							client.toggleLog();
							appendFile(LEVEL.INFO, FormatService.serverLogPrefix(client) + "timeout connection, kick user.");
						}
					}

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						appendFile(LEVEL.INFO, e.getMessage());
					}
				}
			}
		});
	}

	private @Nullable Player findPlayerBySocket(Socket socket) {
		for (Player p : players) {
			if (p.getSocket() == socket) {
				return p;
			}
		}

		return null;
	}

	public Thread buildReceiver(Socket s, PrintWriter pw, BufferedReader br) {
		return new Thread(new Runnable() {                    // It creates a new thread that receives sockets.
			String messageReceived;
			String commandResponse;
			String messageToSend;
			String logMessage;
			Player client;

			@Override
			public void run() {
				try {
					messageReceived = br.readLine();        // It catches the message from the remote host.

					while (messageReceived != null) {        // While the buffer is not empty, there is still a message inside.
						messageToSend = "";            // The server will send this message to the client.
						logMessage = "";                // It will be printed in the server logs.
						commandResponse = "";

						client = findPlayerBySocket(s);

						if (messageReceived.compareTo("/q!") == 0) {
							if (client != null && client.isLogged()) {
								client.close();
								client.toggleLog();
							}

							logMessage += "has left the room.";
						} else {
							logMessage += messageReceived.split(" ")[0]; // Add some logs.
							commandResponse = ServerCommandHandler.executeCommand(messageReceived, s, pw, br, players);
							messageToSend += commandResponse;
						}

						client = findPlayerBySocket(s);

						if (client == null) {
							messageToSend += ";;(?)--|";
						} else {
							client.refreshLastConnection();
							messageToSend = client.getColor() + "# " + FormatService.ANSI_RESET + messageToSend.replace(";", ";" + client.getColor() +  "# " + FormatService.ANSI_RESET) +  ";;" + client.getColor() + "(" + client.getUsername() + ")--|" + FormatService.ANSI_RESET;

							saveMessage(new Message(messageReceived, client));
						}

						pw.println(messageToSend);
						pw.flush();
						appendFile(LEVEL.INFO, FormatService.serverLogPrefix(client) + logMessage);
						messageReceived = br.readLine();
					}

					pw.close(); // Closes the connection between the local host and the remote host.
				} catch (IOException ignored) {}
			}
		});
	}

	public void start(int port) {
		ServerCommandHandler.populateCommands();
		final ServerSocket serverSocket;

		String data = readFile(LEVEL.DEBUG);
		if (data.length() > 0) {
			for (String credentials : data.split("\n")) {
				String username = credentials.split(" ")[0];
				String password = credentials.split(" ")[1];
				Player p = new Player(null, null, null, username, password);
				players.add(p);
				appendFile(LEVEL.INFO, "Credentials loaded");
			}
		}

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		appendFile(LEVEL.INFO, "Listening on " + serverSocket.getLocalSocketAddress());
		checkConnection().start();

		while (true) {
			final Socket socket;
			final BufferedReader bufferedReader;
			final PrintWriter printWriter;

			try {
				socket = serverSocket.accept();                                    // If the host does not send a message, it can not create this object and can not send messages to this host.
				printWriter = new PrintWriter(socket.getOutputStream());
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				appendFile(LEVEL.INFO, "Client not accepted.");
				break;
			}

			appendFile(LEVEL.INFO, FormatService.serverLogPrefix(null) + "has joined the room.");
			buildSender(printWriter).start();                                    // Builds and starts threads.
			buildReceiver(socket, printWriter, bufferedReader).start();
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println();
		}
	}
}
