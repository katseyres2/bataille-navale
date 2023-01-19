package services;

import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import socket.Client;

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

	public static final int USERNAME_MAX_LENGTH = 10;
	public static final int PASSWORD_MAX_LENGTH = 10;

	public static String getRandomColor() {
		String[] colors = {
			ANSI_RED, ANSI_GREEN, ANSI_YELLOW,
			ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN,
		};

		return colors[(new Random()).nextInt(colors.length - 1)];
	}

	public static String LocalDateTimeToString(LocalDateTime dateTime) {
		return DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").format(dateTime);
	}

	public static LocalDateTime getCurrentTime() {
		return LocalDateTime.now();
	}

	public static String formatSpace(int length, String word, boolean alignLeft) {
		return (alignLeft == true ? word : "") + " ".repeat(length - word.length()) + (alignLeft == true ? "" : word);
	}

	public static String serverLogPrefix(Client client) {
		String username = FormatService.formatSpace(
			FormatService.USERNAME_MAX_LENGTH,
			client != null && client.getUsername() != null
				? client.getUsername()
				: "?",
			true
		);

		return "[" + FormatService.LocalDateTimeToString(FormatService.getCurrentTime()) + " " + username + "] ";
	}

	public static String toMessage(Socket socket, String msg) {
		String output = "";

		output += "["+ FormatService.LocalDateTimeToString(FormatService.getCurrentTime()) +"]   TO=\"" + socket.getLocalAddress().getHostAddress() + ":" + socket.getPort() + "\", ";
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
