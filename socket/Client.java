package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client extends SocketUser implements ISocket {
	public Thread buildSender(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter, Scanner scanner) {
		return new Thread(new Runnable() {
			String msg;

			@Override
			public void run() {
				System.out.print(">>> ");
				
				while (true) {
					try {
						msg = scanner.nextLine();
						printWriter.println(msg);
						printWriter.flush();
					} catch (Exception e) {
						Thread.currentThread().interrupt();
						System.out.println("\n\nGoodbye!");
						break;
					}
				}

				close(socket, bufferedReader, scanner, printWriter);
			}
		});
	}

	public Thread buildReceiver(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter, Scanner scanner) {
		return new Thread(new Runnable() {
			String msg;

			@Override
			public void run() {
				try {
					msg = bufferedReader.readLine();
					
					while (msg != null) {
						// String line = FormatService.fromMessage(msg);
						String message = "";
						String[] lines = msg.split(";");

						if (msg.contains(";")) {
							for (String line : lines) {
								message += line + "\n";
							}
						}
						else {
							message = msg + "\n";
						}

						System.out.print("\n" + message + "\n>>> ");
						msg = bufferedReader.readLine();
					}

					System.out.println("Server out of service");
					close(socket, bufferedReader, scanner, printWriter);
				} catch (IOException e) {
					Thread.currentThread().interrupt();
					System.out.println("\n\nServer out of service ! Please contact the administrator.");
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
		System.out.println("Your address is " + socket.getInetAddress().getHostAddress() + ":" + Integer.toString(socket.getLocalPort()));

		buildSender(socket, bufferedReader, printWriter, scanner).start();
		buildReceiver(socket, bufferedReader, printWriter, scanner).start();
	}
}
