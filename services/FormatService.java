package services;

import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import game.grid.Grid;
import socket.server.Player;

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

	public static final String[] colors = {
		ANSI_RED, ANSI_GREEN, ANSI_YELLOW,
		ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN,
	};

	public static final int USERNAME_MAX_LENGTH = 10;
	public static final int PASSWORD_MAX_LENGTH = 10;

	public static String getRandomColor() {
		return  colors[(new Random()).nextInt(colors.length - 1)];
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

	public static String colorizeString(String color, String value) {
		String output = value;
		
		for (String c : colors) {
			if (c.contains(color)) {
				output = color + value + ANSI_RESET;
			}
		}

		return output;
	}

	public static String serverLogPrefix(Player user) {
		String username;
		
		if (user instanceof Player && user != null && ((Player)user).getUsername() != null) {
			username = ((Player)user).getUsername();
		} else {
			username = "?";
		}

		String formatUsername = FormatService.formatSpace(FormatService.USERNAME_MAX_LENGTH, username, true);
		return "[" + FormatService.LocalDateTimeToString(FormatService.getCurrentTime()) + " " + formatUsername + "] ";
	}

	public static String concatenateGrids(Grid grid1, Grid grid2) {
		String output = "";

		String[] g1 = grid1.toString(false).split("\n");
		String[] g2 = grid2.toString(true).split("\n");

		for (int i=0; i<g1.length; i++) {
			output += g1[i] + "  |  " + g2[i] + "\n";
		}

		return output + "\n";
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

	/**
	 * Convert the first coord ( letter A to J ) into a int
	 *
	 * @param letter
	 * @return the letter convert in integer
	 */
	public static int convertCoordLetter(String letter) {
		int coord = 0;
		for (int i = 0; i < Grid.POSITIONS.length; i++) {
			if (Grid.POSITIONS[i].equals(letter.toUpperCase())) {
				coord = i;
				break;
			}
		}
		return coord;
	}
}
