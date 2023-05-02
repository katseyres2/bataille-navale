package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogService {
	public static enum LEVEL {
		INFO,
		DEBUG,
		WARNING,
		ERROR,
	}

    private Path path;
    
    public LogService(Path path) {
        this.path = path;
    }

    public void appendFile(LEVEL level, String text) {
		if (! Files.exists(path)) {
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
