package socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Bots.Bot;
import game.Game;
import interfaces.IServer;
import interfaces.ISocketBuilder;
import services.FormatService;
import services.LogService;

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

	public static void logInfo(String value)     { if (logLevel == LEVEL.INFO)     System.out.print(value); }
	public static void setLogLevel(LEVEL value)  { logLevel = value; }
	public static void logDebug(String value)    { if (logLevel == LEVEL.DEBUG)    System.out.print(value); }
	public static void logError(String value)    { if (logLevel == LEVEL.ERROR)    System.out.print(value); }
	public static void logWarning(String value)  { if (logLevel == LEVEL.WARNING)  System.out.print(value); }

	public static void pushGame(Game game) {
		if (games.contains(game) || game == null) return;
		games.add(game);
	}

	public static Game getActiveGame(Player player) {
		// System.out.println("Player : " + player.getUsername());
		for (Game g : games) {
			// System.out.println("Is playing : " + g.isPlaying() + ", hasPlayer : " + g.hasPlayer(player));
			// if (g.isPlaying() && g.hasPlayer(player)) return g;
			if (g.hasPlayer(player)) return g;
		}

		Player player1 = new Bot("test", Bot.Difficulty.Easy);

		return null;
	}

	public Thread buildSender(PrintWriter pw) {
		return new Thread(new Runnable() {		// Create a new thread with a callback function. "Runnable" must implement run().
			@Override
			public void run() {
				String msg = "Welcome ! Use the command /signup to create a new account or /signin you're already registered.;;(?)--|\n";
				Scanner scan = new Scanner(System.in);

				pw.print(msg);
				pw.flush();

				while(true) {
					try {
						msg = scan.nextLine();		// Wait for the user input.
						pw.println(msg);			// Send the message on the client host:port.
						pw.flush();					// Clear the memory cell.
					} catch (NoSuchElementException e) {
						System.out.println(e.getMessage());
						break;
					} catch (IllegalStateException e) {
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

	public Thread buildReceiver(Socket s, PrintWriter pw, BufferedReader br) {
		return new Thread(new Runnable() {					// It creates a new thread that receives sockets.
			String messageReceived;

			@Override
			public void run() {
				try {
					messageReceived = br.readLine();		// It catches the message from the remote host.
					Player client;

					while (messageReceived != null) {		// While the buffer is not empty, there is still a message inside.
						String messageToSend = "";			// The server will send this message to the client.
						String logMessage = "";				// It will be printed in the server logs.
						client = null;

						for (Player p : players) {
							if (p.getSocket() == s) {
								client = p;
								break;
							}
						}

						if (messageReceived.compareTo("/q!") == 0) {
							if (client != null && client.isLogged()) {
								client.close();
								client.toggleLog();
							}
							
							logMessage += "has left the room.";
						} else {
							logMessage += messageReceived.split(" ")[0]; // Add some logs.
							messageToSend += ServerCommandHandler.executeCommand(messageReceived, s, pw, br, players);								
						}

						client = null;

						for (Player p : players) {
							if (p.getSocket() == s) {
								client = p;
								break;
							}
						}
						
						if (client == null) {
							messageToSend += ";;(?)--|";
						} else {
							client.refreshLastConnection();
							messageToSend = client.getColor() + "# " + FormatService.ANSI_RESET + messageToSend.replace(";", ";" + client.getColor() +  "# " + FormatService.ANSI_RESET) +  ";;" + client.getColor() + "(" + client.getUsername() + ")--|" + FormatService.ANSI_RESET;
						}

						pw.println(messageToSend);
						pw.flush();
						appendFile(LEVEL.INFO, FormatService.serverLogPrefix(client) + logMessage);
						messageReceived = br.readLine();
					}
				
					pw.close(); // Closes the connection between the local host and the remote host.
				} catch (IOException e) {}
			}
		});
	}

	public void start(int port) {
		final ServerSocket serverSocket;

		String data = readFile(LEVEL.DEBUG);
		if (data.length() > 0) {
			for (String credentials : data.split("\n")) {
				String username = credentials.split(" ")[0];
				String password = credentials.split(" ")[1];
				Player p = new Player(null, null, null);
				p.setUsername(username);
				p.setPassword(password);
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
				socket = serverSocket.accept();									// If the host does not send a message, it can not create this object and can not send messages to this host.
				printWriter = new PrintWriter(socket.getOutputStream());
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				appendFile(LEVEL.INFO, "Client not accepted.");
				break;
			}

			appendFile(LEVEL.INFO, FormatService.serverLogPrefix(null) + "has joined the room.");
			buildSender(printWriter).start();									// Builds and starts threads.
			buildReceiver(socket, printWriter, bufferedReader).start();
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println();
		}
	}
}
