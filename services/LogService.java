package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service for logging messages to a file.
 */
public class LogService {

	/**
	 * Enumeration representing log levels.
	 */
	public static enum LEVEL {
		INFO,
		DEBUG,
		WARNING,
		ERROR,
	}

	private Path path;

	/**
	 * Constructs a LogService object with the specified file path.
	 *
	 * @param path The path to the log file.
	 */
	public LogService(Path path) {
		this.path = path;
	}

	/**
	 * Appends the specified text to the log file.
	 *
	 * @param level The log level.
	 * @param text  The text to be appended.
	 */
	public void appendFile(LEVEL level, String text) {
		if (!Files.exists(path)) {
			try {
				Files.createFile(path);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		try {
			String old = Files.readString(path);
			Files.writeString(path, old + text + "\n");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Reads the contents of the log file.
	 *
	 * @param type The log level to filter the output.
	 * @return The contents of the log file as a string.
	 */
	public String readFile(LEVEL type) {
		String output = "";

		if (Files.exists(path)) {
			try {
				output = Files.readString(path);
			} catch (Exception e) {
				System.out.println("No file.");
			}
		}

		return output;
	}
}