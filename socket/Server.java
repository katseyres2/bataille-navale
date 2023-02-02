package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import services.FormatService;
import socket.commands.ServerCommandHandler;

public class Server {
	public static enum Log {
		INFO,
		DEBUG,
		WARNING,
		ERROR,
	}

	private ArrayList<Player> players = new ArrayList<Player>();
	private static Log logLevel = Log.INFO;
	private static final Path LOG_PATH = Path.of("../data/server.log");
	public static final Path CREDENTIALS_PATH = Path.of("../data/credentials.txt");

	public static void logInfo(String value)     { if (logLevel == Log.INFO)     System.out.print(value); }
	public static void setLogLevel(Log value) 	 { logLevel = value; }
	public static void logDebug(String value)    { if (logLevel == Log.DEBUG)    System.out.print(value); }
	public static void logError(String value)    { if (logLevel == Log.ERROR)    System.out.print(value); }
	public static void logWarning(String value)  { if (logLevel == Log.WARNING)  System.out.print(value); }

	public Thread buildSender(PrintWriter pw) {
		return new Thread(new Runnable() {		// Create a new thread with a callback function. "Runnable" must implement run().
			@Override
			public void run() {
				String msg = "Welcome ! Use the command /signup to create a new account or /signin you're already registered.;;(?)──┤\n";
				Scanner scan = new Scanner(System.in);

				pw.print(msg);
				pw.flush();

				while(true) {
					msg = scan.nextLine();		// Wait for the user input.
					pw.println(msg);			// Send the message on the client host:port.
					pw.flush();					// Clear the memory cell.
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
							appendFile(LOG_PATH, FormatService.serverLogPrefix(client) + "timeout connection, kick user.");
						}
					}
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						appendFile(LOG_PATH, e.getMessage());
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
							messageToSend += ";;(?)──┤";
						} else {
							client.refreshLastConnection();
							messageToSend = client.getColor() + "▓ " + FormatService.ANSI_RESET + messageToSend.replace(";", ";" + client.getColor() +  "▓ " + FormatService.ANSI_RESET) +  ";;" + client.getColor() + "(" + client.getUsername() + ")──┤" + FormatService.ANSI_RESET;
						}

						pw.println(messageToSend);
						pw.flush();
						appendFile(LOG_PATH, FormatService.serverLogPrefix(client) + logMessage);
						messageReceived = br.readLine();
					}
				
					pw.close(); // Closes the connection between the local host and the remote host.
				} catch (IOException e) {}
			}
		});
	}

	public static void appendFile(Path path, String text) {
		if (! Files.exists(path)) {
			try {
				Files.createFile(path);			
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		try {
			String old = Files.readString(path);
			Files.writeString(path, old + text + "\n");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static String readFile(Path path) {
		String output = "";
		
		if (Files.exists(path)) {
			try {
				output = Files.readString(path);
			} catch (Exception e) {
				System.out.println("No file.");
			}
		}

		return output;
	}

	public void start(int port) {
		final ServerSocket serverSocket;

		String data = readFile(CREDENTIALS_PATH);
		if (data.length() > 0) {
			for (String credentials : data.split("\n")) {
				String username = credentials.split(" ")[0];
				String password = credentials.split(" ")[1];
				Player p = new Player(null, null, null);
				p.setUsername(username);
				p.setPassword(password);
				players.add(p);
				appendFile(LOG_PATH, "Credentials loaded");
			}
		}

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		appendFile(LOG_PATH, "Listening on " + serverSocket.getLocalSocketAddress());
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
				appendFile(LOG_PATH, "Client not accepted.");
				break;
			}

			appendFile(LOG_PATH, FormatService.serverLogPrefix(null) + "has joined the room.");
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
