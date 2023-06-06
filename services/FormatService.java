package services;

import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import game.Game;
import game.boat.Boat;
import game.grid.Cell;
import game.grid.Grid;
import socket.server.Player;

public class FormatService {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String[] colors = {
			ANSI_RED, ANSI_GREEN, ANSI_YELLOW,
			ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN,
	};

	public static final int USERNAME_MAX_LENGTH = 20;
	public static final int PASSWORD_MAX_LENGTH = 20;

	/**
	 * Generates a random color from the available colors.
	 *
	 * @return The randomly generated color.
	 */
	public static String getRandomColor() {
		return colors[(new Random()).nextInt(colors.length - 1)];
	}

	/**
	 * Converts a LocalDateTime object to a formatted string representation.
	 *
	 * @param dateTime The LocalDateTime object to convert.
	 * @return The formatted string representation of the LocalDateTime.
	 */
	public static String LocalDateTimeToString(LocalDateTime dateTime) {
		return DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").format(dateTime);
	}

	/**
	 * Retrieves the current system time as a LocalDateTime object.
	 *
	 * @return The current system time.
	 */
	public static LocalDateTime getCurrentTime() {
		return LocalDateTime.now();
	}

	/**
	 * Formats a word with leading or trailing spaces to achieve a desired length.
	 *
	 * @param length    The desired length of the formatted string.
	 * @param word      The word to format.
	 * @param alignLeft Determines if the word should be left-aligned or right-aligned.
	 * @return The formatted string.
	 */
	public static String formatSpace(int length, String word, boolean alignLeft) {
		return (alignLeft == true ? word : "") + " ".repeat(length - word.length()) + (alignLeft == true ? "" : word);
	}

	/**
	 * Applies color to a string value.
	 *
	 * @param color The color to apply to the value.
	 * @param value The value to be colorized.
	 * @return The colorized string.
	 */
	public static String colorizeString(String color, String value) {
		String output = value;

		for (String c : colors) {
			if (c.contains(color)) {
				output = color + value + ANSI_RESET;
			}
		}

		return output;
	}

	/**
	 * Generates the log prefix for a given player.
	 *
	 * @param user The player for which to generate the log prefix.
	 * @return The log prefix string.
	 */
	public static String serverLogPrefix(Player user) {
		String username;

		if (user instanceof Player && user != null && ((Player) user).getUsername() != null) {
			username = ((Player) user).getUsername();
		} else {
			username = "?";
		}

		String formatUsername = FormatService.formatSpace(FormatService.USERNAME_MAX_LENGTH, username, true);
		return "[" + FormatService.LocalDateTimeToString(FormatService.getCurrentTime()) + " " + formatUsername + "] ";
	}

	/**
	 * Concatenates the string representations of two grids side by side.
	 *
	 * @param grid1 The first grid to concatenate.
	 * @param grid2 The second grid to concatenate.
	 * @return The concatenated grid string.
	 */
	public static String concatenateGrids(Grid grid1, Grid grid2) {
		StringBuilder output = new StringBuilder();

		String[] g1 = showGrid(false, grid1).split("\n");
		String[] g2 = showGrid(true, grid2).split("\n");

		for (int i = 0; i < g1.length; i++) {
			output.append(g1[i]).append("  |  ").append(g2[i]).append("\n");
		}

		return output + "\n";
	}

	/**
	 * Converts a message to a formatted log string for a specific socket.
	 *
	 * @param socket The socket to which the message is sent.
	 * @param msg    The message to convert.
	 * @return The formatted log string.
	 */
	public static String toMessage(Socket socket, String msg) {
		String output = "";

		output += "[" + FormatService.LocalDateTimeToString(FormatService.getCurrentTime()) + "]   TO=\"" +
				socket.getLocalAddress().getHostAddress() + ":" + socket.getPort() + "\", ";
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

	/**
	 * Generates a string representation of the grid.
	 *
	 * @param showBoats Indicates whether to show boats on the grid.
	 * @param grid      The grid to display.
	 * @return The string representation of the grid.
	 */
	public static String showGrid(boolean showBoats, Grid grid) {
		String username = FormatService.colorizeString(grid.getPlayer().getColor(), grid.getPlayer().getUsername());
		int horizontalLengthSpaces = 1 + grid.getColumns() * 3;
		String divider = "-".repeat(horizontalLengthSpaces + 2 * 3) + "\n";
		String title = "|       " + username + " ".repeat(horizontalLengthSpaces - username.length()) + "      |\n";

		StringBuilder output = new StringBuilder("\n" + divider);
		output.append(title);
		output.append(divider);

		output.append("|  /");
		for (int i = 0; i < grid.getColumns(); i++) {
			output.append("  ").append(i);
		}
		output.append("  |\n");

		for (Cell c : grid.getAllCells()) {
			if (c.getColumn() == 0) {
				output.append("|  ").append(c.getRow());
			}
			Boat boat = DiscoveryService.findBoatWhichHasCell(c, grid.getBoats());
			String label;

			/*
			 * Variables: discovered, boat, visible
			 *
			 * !discovered !boat !visible  -> ?
			 * !discovered !boat  visible  -> ?
			 * !discovered  boat !visible  -> ?
			 * !discovered  boat  visible  -> LETTER
			 *  discovered !boat !visible  -> .
			 *  discovered !boat  visible  -> X
			 *  discovered  boat !visible  -> o
			 *  discovered  boat  visible  -> o
			 */
			if (!c.isDiscovered()) {
				label = boat != null && showBoats ? boat.getLabel() : ".";
			} else {
				if (boat == null) {
					label = FormatService.colorizeString(colors[3], "o");
				} else {
					label = FormatService.colorizeString(colors[0], boat.getLabel());
				}
			}

			output.append("  ").append(label);
			if (c.getColumn() == grid.getColumns() - 1) {
				output.append("  |\n");
			}
		}

		output.append(divider);

		return output.toString();
	}

	/**
	 * Generates a string representation of the players in the game.
	 *
	 * @param game The game containing the players.
	 * @return The string representation of the players.
	 */
	public static String showPlayers(Game game) {
		StringBuilder output = new StringBuilder();

		for (Player p : game.getPlayers()) {
			output.append(p).append(" ");
		}

		return output.toString();
	}
}
