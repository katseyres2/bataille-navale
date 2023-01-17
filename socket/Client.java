package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import services.FormatService;

public class Client extends SocketUser implements ISocket {
	public Thread buildSender(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter, Scanner scanner) {
		return new Thread(new Runnable() {
			String msg;

			@Override
			public void run() {
				while (true) {
					try {
						msg = scanner.nextLine();
						printWriter.println(msg);
						printWriter.flush();
						// System.out.println(FormatService.toMessage(socket, msg));
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
					System.out.print("\n>>> ");
					msg = bufferedReader.readLine();

					while (msg != null) {
						System.out.println(FormatService.fromMessage(socket, msg));
						System.out.print("\n>>> ");
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
