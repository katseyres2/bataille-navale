package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
	private Socket socket;
	private BufferedReader bufferedReader;
	private Scanner scanner;
	private PrintWriter printWriter;
	private boolean logged;

	public SocketClient(Socket s, PrintWriter pw, BufferedReader br) {
		logged = false;
		scanner = new Scanner(System.in);
		socket = s;
		printWriter = pw;
		bufferedReader = br;
	}

	
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
}
