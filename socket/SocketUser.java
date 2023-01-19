package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketUser {
	private Socket socket;
	private BufferedReader bufferedReader;
	private Scanner scanner;
	private PrintWriter printWriter;

	public Socket getSocket() 				  { return socket; }
	public String getAddress() 				  { return socket != null ? socket.getLocalAddress().getHostAddress() : ""; }
	public int getPort()					  { return socket != null ? socket.getPort() : null; }
	public BufferedReader getBufferedReader() { return bufferedReader; }
	public Scanner getScanner() 			  { return scanner; }
	public PrintWriter getPrintWriter() 	  { return printWriter; }

	public void setSocket(Socket socket) 						 { this.socket = socket; }
	public void setBufferedReader(BufferedReader bufferedReader) { this.bufferedReader = bufferedReader; }
	public void setScanner(Scanner scanner) 					 { this.scanner = scanner; }
	public void setPrintWriter(PrintWriter printWriter) 		 { this.printWriter = printWriter; }

	public void clear() {
		socket = null;
		bufferedReader = null;
		scanner = null;
		printWriter = null;
	}

	public void close() throws IOException {
		try {
			bufferedReader.close();
			socket.close();
			printWriter.close();
			clear();
		} catch (IOException e) {
			throw new IOException("Unable to close the socket.");
		}
	}
}
