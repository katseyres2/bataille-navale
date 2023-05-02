package socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

import interfaces.ISocketBuilder;

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
					message = bufferedReader.readLine();
					
					while (message != null) {
						String[] lines = message.split(";");
						System.out.print("\n" + String.join("\n", lines) + " ");
						message = bufferedReader.readLine();
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

	public Thread buildSender(PrintWriter pw) {
		return new Thread(new Runnable() {
			String message;

			@Override
			public void run() {
				while (true) {

					try {
						message = scanner.nextLine();
						printWriter.println(message);
						printWriter.flush();
					} catch (Exception e) {
						printWriter.println("/q!");
						printWriter.flush();
						break;
					}
				}

				boolean isClosed = close();
				System.out.println(isClosed ? "\n\nGoodbye!\n" : "Unable to close the socket.");
			}
		});
	}

	public void refreshConnection(Socket s, PrintWriter pw, BufferedReader br) {
		setSocket(s);
		setPrintWriter(pw);
		setBufferedReader(br);
	}

	public LocalDateTime getLastConnection()			{ return lastConnectionAt; }
}
