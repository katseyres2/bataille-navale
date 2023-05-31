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
import org.jetbrains.annotations.Nullable;
import services.DiscoveryService;
import services.FormatService;
import services.LogService;
import socket.client.SocketClient;

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
		System.out.println("GAME START");
		if (games.contains(game) || game == null) return;
		games.add(game);
		System.out.println("GAME END");
	}

	public static @Nullable Game getActiveGame(Player player) {
		for (Game g : games) {
			System.out.println("HasPlayer : " + g.hasPlayer(player) + " " + g.getPlayers().toString());
			// if (g.isPlaying() && g.hasPlayer(player)) return g;
			if (g.hasPlayer(player)) return g;
		}

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
						pw.println(msg);            // Send the message on the player host:port.
						pw.flush();                    // Clear the memory cell.
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
					for (Player player : players) {
						if (player.isLogged() && !player.getLastConnection().plus(5, ChronoUnit.MINUTES).isAfter(FormatService.getCurrentTime())) {
							player.clear();
							appendFile(LEVEL.INFO, FormatService.serverLogPrefix(player) + "timeout connection, kick user.");
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
		return new Thread(new Runnable() {                    // It creates a new thread that receives sockets.
			String messageReceived;

			@Override
			public void run() {
				final SocketClient client = new SocketClient(s, pw, br);

				try {
					messageReceived = br.readLine();        // It catches the message from the remote host.
					Player player;

					while (messageReceived != null) {        // While the buffer is not empty, there is still a message inside.
						String messageToSend = "";            // The server will send this message to the player.
						String logMessage = "";                // It will be printed in the server logs.
						player = DiscoveryService.findOneBy(s, players);

						if (messageReceived.compareTo("/q!") == 0) {
							if (player != null && player.isLogged()) {
								player.close();
							}
						} else {
							messageToSend += ServerCommandHandler.executeCommand(messageReceived, client, players);
						}

						player = null;

						for (Player p : players) {
							if (p.getSocket() == s) {
								player = p;
								break;
							}
						}

						if (player == null) {
							messageToSend += ";;(?)--|";
						} else {
							player.refreshLastConnection();
							messageToSend = player.getColor() + "# " + FormatService.ANSI_RESET + messageToSend.replace(";", ";" + player.getColor() +  "# " + FormatService.ANSI_RESET) +  ";;" + player.getColor() + "(" + player.getUsername() + ")--|" + FormatService.ANSI_RESET;
						}

						pw.println(messageToSend);
						pw.flush();
						messageReceived = br.readLine();
					}

					pw.close(); // Closes the connection between the local host and the remote host.
				} catch (IOException e) {}
			}
		});
	}

	public void start(int port) {
		final ServerSocket serverSocket;
		ServerCommandHandler.populateCommands();
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
				socket = serverSocket.accept();                                    // If the host does not send a message, it can not create this object and can not send messages to this host.
				printWriter = new PrintWriter(socket.getOutputStream());
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				appendFile(LEVEL.INFO, "player not accepted.");
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
