package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

import services.expections.NotConnectedException;

public class Client extends SocketUser implements ISocket {
	private boolean logged;
	private String username;
	private String password;
	private LocalDateTime lastConnectionAt;

	public Client() {
		logged = false;
		lastConnectionAt = LocalDateTime.now();
	}

	public LocalDateTime getLastConnection() {
		return lastConnectionAt;
	}

	public void refreshLastConnection() {
		lastConnectionAt = LocalDateTime.now();
	}

	public boolean checkCredentials(String username, String password) {
		return this.username.compareTo(username) == 0 && this.password.compareTo(password) == 0;
	}

	public boolean compareTo(Client client) {
		return client.checkCredentials(username, password);
	}

	public void signOut() throws NotConnectedException {
		if (!logged) throw new NotConnectedException();
		logged = false;
	}

	public boolean isLogged() { return logged; }
	public String getUsername() { return username; }
	public void setUsername(String value) { username = value; }
	public void setPassword(String value) { password = value; }
	public void toggleLog() { logged = !logged; }

	public Thread buildSender(SocketUser user) {
		return new Thread(new Runnable() {
			String msg;

			@Override
			public void run() {
				while (true) {
					try {
						msg = user.getScanner().nextLine();
						user.getPrintWriter().println(msg);
						user.getPrintWriter().flush();
					} catch (Exception e) {
						user.getPrintWriter().println("/q!");
						user.getPrintWriter().flush();

						try {
							user.close();
						} catch (IOException er) {
							System.out.println(er.getMessage());
						}

						Thread.currentThread().interrupt();
						System.out.println("\n\nGoodbye!\n");
						break;
					}
				}

				try {
					close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		});
	}

	public Thread buildReceiver(SocketUser user) {
		return new Thread(new Runnable() {
			String msg;

			@Override
			public void run() {
				try {
					msg = user.getBufferedReader().readLine();
					
					while (msg != null) {
						String message = "";
						String[] lines = msg.split(";");

						if (msg.contains(";")) {
							for (String line : lines) {
								message += line;
								if (line.compareTo(lines[lines.length - 1]) != 0) {
									message += "\n";
								}
							}
						}
						else {
							message = msg + "\n";
						}

						System.out.print("\n" + message + "$ ");
						msg = user.getBufferedReader().readLine();
					}

					System.out.println("Server out of service");
					close();
				} catch (IOException e) {
					Thread.currentThread().interrupt();
					System.out.println("\n\nServer out of service ! Please contact the administrator.\n");
					System.exit(1);
				}
			}
		});
	}

	public void start(int port) {
		final Socket socket;
		final BufferedReader bufferedReader;
		final PrintWriter printWriter;
		
		try {
			socket = new Socket("127.0.0.1", port);
			printWriter = new PrintWriter(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch(Exception e) {
			System.out.println("Unable to create the server socket");
			return;
		}

		final Scanner scanner = new Scanner(System.in);

		super.setScanner(scanner);
		super.setSocket(socket);
		super.setBufferedReader(bufferedReader);
		super.setPrintWriter(printWriter);
		
		System.out.print("\nYour address is " + super.getSocket().getInetAddress().getHostAddress() + ":" + Integer.toString(socket.getLocalPort()));

		buildSender(this).start();
		buildReceiver(this).start();
	}
}
