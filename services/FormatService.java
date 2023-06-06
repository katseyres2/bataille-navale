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
		StringBuilder output = new StringBuilder();

		String[] g1 = showGrid(false, grid1).split("\n");
		String[] g2 = showGrid(true, grid2).split("\n");

		for (int i=0; i<g1.length; i++) {
			output.append(g1[i]).append("  |  ").append(g2[i]).append("\n");
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

	public static String showGrid(boolean showBoats, Grid grid) {
		String username = FormatService.colorizeString(grid.getPlayer().getColor() ,grid.getPlayer().getUsername());
		int horizontalLengthSpaces = 1 + grid.getColumns() * 3;
		String divider = "-".repeat(horizontalLengthSpaces + 2*3) + "\n";
		String title = "|       " + username + " ".repeat(horizontalLengthSpaces - username.length()) + "      |\n";

		StringBuilder output = new StringBuilder("\n" + divider);
		output.append(title);
		output.append(divider);

		output.append("|  /");
		for (int i=0; i<grid.getColumns(); i++) output.append("  ").append(i);
		output.append("  |\n");

		for (Cell c : grid.getAllCells()) {
//			if (c.getColumn() == 0) output.append(Grid.POSITIONS[c.getRow()]);
			if (c.getColumn() == 0) output.append("|  " + c.getRow());
			Boat boat = DiscoveryService.findBoatWhichHasCell(c, grid.getBoats());
			String label;

			/*
			* variables = discovered, boat, visible
			*
			* !discovered !boat !visible  -> ?
			* !discovered !boat  visible  -> ?
			* !discovered  boat !visible  -> ?
			* !discovered  boat  visible  -> LETTER
			*  discovered !boat !visible  -> .
			*  discovered !boat  visible  -> X
			*  discovered  boat !visible  -> o
			*  discovered  boat  visible  -> o
			* */

			if (!c.isDiscovered()) {
				label = boat != null && showBoats ? boat.getLabel() : ".";
			} else {
				if (boat == null) {
					label = FormatService.colorizeString(colors[3], "o");
				} else {
					label = FormatService.colorizeString(colors[0], boat.getLabel());
				}
			}

			output.append("  " + label);
			if (c.getColumn() == grid.getColumns() - 1) output.append("  |\n");
		}

		output.append(divider);
//
//		for (int i = 0; i < grid.getRows() + 1; i++) {
//			for (int j = 0; j < grid.getColumns(); j++) {
//				// Display the letter on the first column
//				if (j == 1 && i >= 1) output.append(Grid.POSITIONS[i - 1]).append(" ");
//
//				// Display position on the first row
//				if (i == 0) {
//					if (j == 0) output.append("\\ ");
//					if (j >= 1) {
//						output.append(j > 9 ? " " : "  ");
//						output.append(j).append(" ");
//					}
//				} else if (j >= 1) {
//					Boat boat = DiscoveryService.findBoatWhichHasCell(grid.getGrid().get(i-1).get(j-1), grid.getBoats());
//
//					// "?" if not discovered, not visible
//					// "." if discovered, not visible, no boat
//					// "o" if discovered, not visible + visible, boat
//					// "X" if discovered, visible, not boat
//					// "<Letter>" if not discovered, visible, boat
//
//					String label = "^";
//					if (cell != null && !cell.isDiscovered() && !showBoats) label = "  ? ";
//					else if (cell != null && cell.isDiscovered() && !showBoats && boat == null) label = "  . ";
//					else if (cell != null && cell.isDiscovered() && boat != null) label = "  o ";
//					else if (cell != null && cell.isDiscovered() && showBoats && boat == null) label = "  X ";
//					else if (cell != null && !cell.isDiscovered() && showBoats && boat != null) label = "  " + boat.getLabel() + " ";
//
//					output.append(label);
//				}
//			}
//
//			output.append("\n");
//		}

		return output.toString();
	}

	public static String showPlayers(Game game) {
		StringBuilder output = new StringBuilder();

		for (Player p : game.getPlayers()) {
			output.append(p).append(" ");
		}

		return output.toString();
	}
}
