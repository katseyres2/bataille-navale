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
	private LocalDateTime lastConnectionAt;

	/**
	 * Constructs a new SocketClient with the provided Socket, PrintWriter, and BufferedReader.
	 * Initializes the scanner and sets the last connection timestamp to the current time.
	 *
	 * @param socket         The Socket object representing the client's connection to the server.
	 * @param printWriter    The PrintWriter object for writing output to the server.
	 * @param bufferedReader The BufferedReader object for reading input from the server.
	 */
	public SocketClient(Socket socket, PrintWriter printWriter, BufferedReader bufferedReader) {
		this.scanner = new Scanner(System.in);
		this.socket = socket;
		this.printWriter = printWriter;
		this.bufferedReader = bufferedReader;
		this.lastConnectionAt = LocalDateTime.now();
	}


	/**
	 * Starts the server on the specified port.
	 *
	 * @param port The port number on which the server will listen.
	 */
	public void start(int port) {
		System.out.print("\nYour address is " + getAddress() + ":" + Integer.toString(getPort()));
		buildSender(getPrintWriter()).start();
		buildReceiver(getSocket(), getPrintWriter(), getBufferedReader()).start();
	}

	/**
	 * Refreshes the last connection timestamp to the current time.
	 */
	public void refreshLastConnection() {
		lastConnectionAt = LocalDateTime.now();
	}

	/**
	 * Returns the port number of the server connection.
	 *
	 * @return The port number if the socket is not null; otherwise, null.
	 */
	public Integer getPort() {
		return socket != null ? socket.getPort() : null;
	}

	/**
	 * Returns the Socket object representing the server connection.
	 *
	 * @return The Socket object.
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Checks if the server connection is logged in.
	 *
	 * @return true if the socket, printWriter, and bufferedReader are not null; otherwise, false.
	 */
	public boolean isLogged() {
		return socket != null && printWriter != null && bufferedReader != null;
	}

	/**
	 * Returns the IP address of the server connection.
	 *
	 * @return The IP address as a string if the socket is not null; otherwise, an empty string.
	 */
	public String getAddress() {
		return socket != null ? socket.getLocalAddress().getHostAddress() : "";
	}

	/**
	 * Returns the Scanner object associated with the server connection.
	 *
	 * @return The Scanner object.
	 */
	public Scanner getScanner() {
		return scanner;
	}

	/**
	 * Returns the PrintWriter object associated with the server connection.
	 *
	 * @return The PrintWriter object.
	 */
	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	/**
	 * Returns the BufferedReader object associated with the server connection.
	 *
	 * @return The BufferedReader object.
	 */
	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	/**
	 * Clears the server connection properties by setting them to null.
	 */
	public void clear() {
		socket = null;
		bufferedReader = null;
		scanner = null;
		printWriter = null;
	}

	/**
	 * Sets the Socket object for the server connection.
	 *
	 * @param socket The Socket object to be set.
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Sets the Scanner object for the server connection.
	 *
	 * @param scanner The Scanner object to be set.
	 */
	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	/**
	 * Sets the PrintWriter object for the server connection.
	 *
	 * @param printWriter The PrintWriter object to be set.
	 */
	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	/**
	 * Sets the BufferedReader object for the server connection.
	 *
	 * @param bufferedReader The BufferedReader object to be set.
	 */
	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	/**
	 * Closes the server connection by closing the BufferedReader, Socket, and PrintWriter objects.
	 *
	 * @return true if the close operation is successful; otherwise, false.
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

	/**
	 * Builds a receiver thread that listens for incoming messages from the server.
	 *
	 * @param socket         The socket for communication with the server.
	 * @param printWriter    The print writer for sending messages to the server.
	 * @param bufferedReader The buffered reader for receiving messages from the server.
	 * @return The receiver thread.
	 */
	public Thread buildReceiver(Socket socket, PrintWriter printWriter, BufferedReader bufferedReader) {
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
					System.out.println("\n\nServer out of service! Please contact the administrator.\n");
					System.exit(1);
				}
			}
		});
	}

	/**
	 * Receives a message from the server and prints it to the console.
	 *
	 * @param message The message received from the server.
	 */
	public void receiveMessage(@NotNull String message) {
		String[] lines = message.split(";");
		String formattedMessage = String.join("\n", lines);
		System.out.print("\n" + formattedMessage + " ");
	}

	/**
	 * Builds a sender thread that sends messages to the server.
	 *
	 * @param printWriter The print writer for sending messages to the server.
	 * @return The sender thread.
	 */
	public Thread buildSender(PrintWriter printWriter) {
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

	/**
	 * Sends a message to the chat server.
	 *
	 * @param message The message to be sent.
	 */
	public void sendMessage(String message) {
		printWriter.println(message);
		printWriter.flush();
	}

	/**
	 * Leaves the chat by sending a termination command to the server.
	 */
	public void leaveChat() {
		sendMessage("/q!");
	}

	/**
	 * Refreshes the connection with a new socket, print writer, and buffered reader.
	 *
	 * @param socket         The new socket to be set.
	 * @param printWriter    The new print writer to be set.
	 * @param bufferedReader The new buffered reader to be set.
	 */
	public void refreshConnection(Socket socket, PrintWriter printWriter, BufferedReader bufferedReader) {
		setSocket(socket);
		setPrintWriter(printWriter);
		setBufferedReader(bufferedReader);
	}

	/**
	 * Returns the timestamp of the last connection.
	 *
	 * @return The LocalDateTime representing the last connection timestamp.
	 */
	public LocalDateTime getLastConnection() {
		return lastConnectionAt;
	}
}
