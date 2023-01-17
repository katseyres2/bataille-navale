package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import game.Game;

public class Server extends SocketUser implements ISocket {
	private final int PORT = 5000;
	
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
	public Thread buildSender(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter, Scanner scanner) {
		// create a new thread with a callback function
		// "Runnable" must implement run()
		return new Thread(new Runnable() {
			String msg;

			@Override
			public void run() {
				while(true) {
					// wait for the user input
					msg = scanner.nextLine();
					// send the message on the client host:port
					printWriter.println(msg);
					// clear the memory cell
					printWriter.flush();
				}
			}
		});
	}

	@Override
	public Thread buildReceiver(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter, Scanner scanner) {
		// create a new thread that receives sockets
		return new Thread(new Runnable() {
			String msg;
			String output = "";

			@Override
			public void run() {
				try {
					// catch the message from the remote host
					msg = bufferedReader.readLine();
					
					// while the buffer is not empty, there is still a message inside
					while (msg != null) {
						output = "";
						System.out.println(socket.getInetAddress().getHostAddress() + ":" + socket.getPort() +" : " + msg);
						
						if (msg.contentEquals("/ping")) {
							output = "pong";
						}
						
						if (msg.contains("/users")) {
							for (int i = 0; i < hosts.size(); i++) {
								output += hosts.get(i).getInetAddress().getHostAddress() + ":" + hosts.get(i).getPort() + ", ";
							}
						}
						
						if (msg.contains("/invite")) {
							String args[] = msg.split(" ");

							if (args.length != 2) {
								output = "require one connected host as argument";
							} else {
								String address = args[1];
								Pattern pattern = Pattern.compile("^([0-9]{1,3}.){3}[0-9]{1,3}:[0-9]{1,5}$");
								Matcher matcher = pattern.matcher(args[1]);
								boolean hasMatch = matcher.matches();

								if (!hasMatch) {
									output = "host:port not found";
								} else {
									String elements[] = address.split(":");
									String ip = elements[0];
									int port = Integer.parseInt(elements[1]);
									
									for (int i = 0; i < hosts.size(); i++) {
										Socket remoteHost = hosts.get(i);
										PrintWriter remoteWriter = writers.get(i);
										
										if (!remoteHost.getInetAddress().getHostAddress().contentEquals(ip)) continue;
										if (remoteHost.getPort() != port) continue;

										// start a new game
																					
										// Game game = new Game(clientSocket, remoteHost);
										// games.add(game);

										// remoteWriter.println(game.showGrids(remoteHost));
										// remoteWriter.flush();
									}
								}
							}

						}
						
						if (output.length() == 0) {
							output = "unsupported message";
						}
						
						// prepare message
						printWriter.println(output);
						// send the message to the remote host
						printWriter.flush();

						msg = bufferedReader.readLine();
					}

					System.out.println(socket.getInetAddress() + ":" + socket.getPort() +  " --> closed");
				
					// close the connection between the local host and the remote host
					printWriter.close();
					socket.close();
					scanner.close();
					removeHost(socket, bufferedReader, printWriter);
				} catch (IOException e) {
					removeHost(socket, bufferedReader, printWriter);
					System.out.println(socket.getInetAddress().getHostAddress() + ":" + socket.getPort() +  " (has left the server)");
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
			final Socket clientSocket;
			final BufferedReader bufferedReader;
			final PrintWriter printWriter;
			
			try {
				// if the host does not send a message, it can not create this object
				// and can not send messages to this host
				clientSocket = serverSocket.accept();
				printWriter = new PrintWriter(clientSocket.getOutputStream());
				bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				addHost(clientSocket, bufferedReader, printWriter);
			} catch (IOException e) {
				System.out.println("Client not accepted.");
				break;
			}
			
			final Scanner scanner = new Scanner(System.in);
			System.out.println("" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " (has joined the server)");

			// builds and starts threads
			buildSender(clientSocket, bufferedReader, printWriter, scanner).start();
			buildReceiver(clientSocket, bufferedReader, printWriter, scanner).start();
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		
	}
}
