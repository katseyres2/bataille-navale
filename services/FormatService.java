package services;

import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FormatService {
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
