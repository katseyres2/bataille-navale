package services;

import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatService {
	public static String getActualTime() {
		return DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
	}

	public static String fromMessage(Socket socket, String msg) {
		String output = "";

		output += "["+ FormatService.getActualTime() +"] FROM=\"" + socket.getLocalAddress().getHostAddress() + ":" + socket.getPort() + "\", ";
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

	public static String toMessage(Socket socket, String msg) {
		String output = "";

		output += "["+ FormatService.getActualTime() +"]   TO=\"" + socket.getLocalAddress().getHostAddress() + ":" + socket.getPort() + "\", ";
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
