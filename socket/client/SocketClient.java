package socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import interfaces.ISocketBuilder;
import org.jetbrains.annotations.NotNull;
import socket.Message;

/**
 * The socket client instance is only used for the client side application.<br>
 * The server can use the client method by the Player inheritance.
 */
public class SocketClient implements ISocketBuilder {
	private Socket socket;
	private BufferedReader bufferedReader;
	private Scanner scanner;
	private PrintWriter printWriter;
	private boolean logged;
	private LocalDateTime lastConnectionAt;

	public SocketClient(Socket s, PrintWriter pw, BufferedReader br) {
		logged = false;
		scanner = new Scanner(System.in);
		socket = s;
		printWriter = pw;
		bufferedReader = br;
		lastConnectionAt = LocalDateTime.now();
	}

	public void start(int port) {
		System.out.print("\nYour address is " + getAddress() + ":" + Integer.toString(getPort()));
		buildSender(getPrintWriter()).start();
		buildReceiver(getSocket(), getPrintWriter(), getBufferedReader()).start();
	}

	public void refreshLastConnection() { lastConnectionAt = LocalDateTime.now(); }

	public int getPort()					  	{ return socket != null ? socket.getPort() : null; }
	public Socket getSocket()				  	{ return socket; }
	public boolean isLogged() 					{ return logged; }
	public String getAddress()		 		  	{ return socket != null ? socket.getLocalAddress().getHostAddress() : ""; }
	public Scanner getScanner() 			  	{ return scanner; }
	public PrintWriter getPrintWriter()		  	{ return printWriter; }
	public BufferedReader getBufferedReader() 	{ return bufferedReader; }
	
	public void clear() 								{ socket = null; bufferedReader = null; scanner = null; printWriter = null; }
	public void toggleLog()   							{ logged = !logged; }
	public void setSocket(Socket s) 					{ socket = s; }
	public void setScanner(Scanner s) 					{ scanner = s; }
	public void setPrintWriter(PrintWriter pw) 		 	{ printWriter = pw; }
	public void setBufferedReader(BufferedReader br)	{ bufferedReader = br; }

	/**
	 * @return
	 */
	public boolean close() {
		boolean output = true;
		
		try {
			bufferedReader.close();
			socket.close();
			printWriter.close();
			clear();
		} catch (IOException e) {
			output = false;
		}

		return output;
	}

	public Thread buildReceiver(Socket s, PrintWriter pw, BufferedReader br) {
		return new Thread(new Runnable() {
			String message;

			@Override
			public void run() {
				try {
					do {
						message = bufferedReader.readLine();
						receiveMessage(message);
					} while (message != null);

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

	public void receiveMessage(@NotNull String message) {
		String[] lines = message.split(";");
		String formattedMessage = String.join("\n", lines);
		System.out.print("\n" + formattedMessage + " ");
	}

	public Thread buildSender(PrintWriter pw) {
		return new Thread(new Runnable() {
			String message;

			@Override
			public void run() {
				while (true) {
					try {
						message = scanner.nextLine();
						sendMessage(message);
					} catch (Exception e) {
						leaveChat();
						break;
					}
				}

				boolean isClosed = close();
				System.out.println(isClosed ? "\n\nGoodbye!\n" : "Unable to close the socket.");
			}
		});
	}

	public void sendMessage(String message) {
		printWriter.println(message);
		printWriter.flush();
	}

	public void leaveChat() {
		sendMessage("/q!");
	}

	public void refreshConnection(Socket s, PrintWriter pw, BufferedReader br) {
		setSocket(s);
		setPrintWriter(pw);
		setBufferedReader(br);
	}

	public LocalDateTime getLastConnection()			{ return lastConnectionAt; }
}
