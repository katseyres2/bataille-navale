package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private int port;
	private String HOST = "127.0.0.1";

	public Client(int port) {
		this.port = port;
	}

	public void start() {
		final Socket socket;
		final BufferedReader bufferedReader;
		final PrintWriter printWriter;
		final Scanner scanner = new Scanner(System.in);

		try {
			// create a new socket on localhost
			socket = new Socket(HOST, port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("New client on " + socket.getInetAddress().getHostAddress() + ":" + Integer.toString(socket.getLocalPort()));

		try {			
			printWriter = new PrintWriter(socket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
			Thread sender = new Thread(new Runnable() {
				String msg;

				@Override
				public void run() {
					while (true) {
						msg = scanner.nextLine();
						printWriter.println(msg);
						printWriter.flush();
					}
				}
			});

			Thread receiver = new Thread(new Runnable() {
				String msg;

				@Override
				public void run() {
					try {
						System.out.print("\nMessage: ");
						msg = bufferedReader.readLine();

						while (msg != null) {
							System.out.println("\n\nFrom: " + socket.getInetAddress() + ":" + socket.getPort() + "");
							
							if (msg.contains(";")) {
								System.out.println("Reply:");
								String rows[] = msg.split(";");
								
								for (String row : rows) {
									System.out.println(row);
								}
							} else {
								System.out.println("Reply: " + msg);
							}
							
							System.out.print("\nMessage: ");
							msg = bufferedReader.readLine();
						}

						System.out.println("Server out of service");
						bufferedReader.close();
						socket.close();
						scanner.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			sender.start();
			receiver.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// try {
		// 	socket.close();
		// } catch (IOException e) {
		// 	e.printStackTrace();
		// }
	}
}
