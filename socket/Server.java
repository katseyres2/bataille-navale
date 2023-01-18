package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import game.Game;
import services.FormatService;
import socket.commands.ServerCommandHandler;

public class Server extends SocketUser implements ISocket {
	private ArrayList<Socket> hosts = new ArrayList<>();
	private ArrayList<BufferedReader> readers = new ArrayList<>();
	private ArrayList<PrintWriter> writers = new ArrayList<>();
	private ArrayList<Game> games = new ArrayList<>();

	private void addHost(Socket host, BufferedReader reader, PrintWriter writer) {
		hosts.add(host);
		readers.add(reader);
		writers.add(writer);
	}

	private void removeHost(Socket host, BufferedReader reader, PrintWriter writer) {
		hosts.remove(host);
		readers.remove(reader);
		writers.add(writer);
	}

	@Override
	public Thread buildSender(SocketUser user) {
		// create a new thread with a callback function
		// "Runnable" must implement run()
		return new Thread(new Runnable() {
			String msg;

			@Override
			public void run() {
				msg = "Welcome ! Use the command /signup to create a new account or /signin you're already registered 🙂";
				user.getPrintWriter().println(msg);
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

	@Override
	public Thread buildReceiver(SocketUser user) {
		Client client = (Client)user;

		// create a new thread that receives sockets
		return new Thread(new Runnable() {
			String msg;
			ServerCommandHandler commandHandler = new ServerCommandHandler(client.getPrintWriter());

			@Override
			public void run() {
				try {
					// catch the message from the remote host
					msg = client.getBufferedReader().readLine();

					// while the buffer is not empty, there is still a message inside
					while (msg != null) {
						if (msg.length() > 0) {
							System.out.println("[" + FormatService.getCurrentTime() + "] " + client.getAddress() + ":" + client.getPort() +" : " + msg);
						}

						String[] args = msg.split(" ");

						if (! client.isLogged()) {
							boolean allowedCommand = false;

							for (String cmd : new String[]{"/help", "/signin", "/signup"}) {
								if (args[0].compareTo(cmd) == 0) {
									allowedCommand = true;
									break;
								}
							}

							if (! allowedCommand) {
								client.getPrintWriter().println("Not connected, please /signin or /signup. 🔒");
								client.getPrintWriter().flush();
								msg = client.getBufferedReader().readLine();
								continue;
							}
						}

						switch (args[0]) {
							case "/ping"	: commandHandler.pong();			break;
							case "/signin"	: commandHandler.signIn();			break;
							case "/signup"	: commandHandler.signUp();			break;
							case "/signout"	: commandHandler.signOut();			break;
							case "/users"	: commandHandler.users(hosts);		break;
							case "/help"	: commandHandler.help();			break;
							case "/invite"	: commandHandler.invite();			break;
							default			: commandHandler.notFound();		break;
						}
						
						msg = client.getBufferedReader().readLine();
					}

					System.out.println(client.getAddress() + ":" + client.getPort() +  " --> closed");
				
					// close the connection between the local host and the remote host
					client.getPrintWriter().close();
					client.close();
					//TODO
					// removeHost(socket, bufferedReader, client.getPrintWriter());
				} catch (IOException e) {
					try {
						client.close();
						//TODO
						// removeHost(socket, bufferedReader, client.getPrintWriter());
						System.out.println(client.getAddress() + ":" + client.getPort() +  " (has left the server)");
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
				addHost(socket, bufferedReader, printWriter);
			} catch (IOException e) {
				System.out.println("Client not accepted.");
				break;
			}
			
			
			Scanner scanner = new Scanner(System.in);
			Client client = new Client();
			client.setSocket(socket);
			client.setPrintWriter(printWriter);
			client.setBufferedReader(bufferedReader);
			client.setScanner(scanner);

			System.out.println("" + client.getAddress() + ":" + client.getPort() + " (has joined the server)");

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
