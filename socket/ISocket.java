package socket;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

interface ISocket {
	void start(int port);
	Thread buildSender(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter, Scanner scanner);
	Thread buildReceiver(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter, Scanner scanner);
}
