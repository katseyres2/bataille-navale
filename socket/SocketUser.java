package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SocketUser {
	public void close(Socket socket, BufferedReader bufferedReader, Scanner scanner, PrintWriter printWriter) {
		try {
			bufferedReader.close();
			socket.close();
			scanner.close();
		} catch (IOException e) {
			System.out.println("Unable to close the socket.");
		}
	}
}
