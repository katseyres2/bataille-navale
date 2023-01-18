package services;

import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FormatService {
	public static final String ANSI_RESET	= "\u001B[0m";
	public static final String ANSI_BLACK	= "\u001B[30m";
	public static final String ANSI_RED 	= "\u001B[31m";
	public static final String ANSI_GREEN	= "\u001B[32m";
	public static final String ANSI_YELLOW	= "\u001B[33m";
	public static final String ANSI_BLUE	= "\u001B[34m";
	public static final String ANSI_PURPLE	= "\u001B[35m";
	public static final String ANSI_CYAN	= "\u001B[36m";
	public static final String ANSI_WHITE	= "\u001B[37m";

	public static String getCurrentTime() {
		return DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
	}

	public static String fromMessage(String msg) {
		String output = "SERVER | ";

		if (msg.contains(";")) {
			for (String row : msg.split(";")) {
				output += row + "::";
			}
		}
		else {
			output += msg;
		}

		return "hi ";
	}

	public static String toMessage(Socket socket, String msg) {
		String output = "";

		output += "["+ FormatService.getCurrentTime() +"]   TO=\"" + socket.getLocalAddress().getHostAddress() + ":" + socket.getPort() + "\", ";
		output += "MESSAGE=\"";
		
		if (msg.contains(";")) {
			String rows[] = msg.split(";");
			
			for (String row : rows) {
				output += row;
			}

			output += "\"";
		} else {
			output += msg + "\"";
		}

		return output;
	}
}
