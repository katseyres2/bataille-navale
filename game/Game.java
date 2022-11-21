package game;
import java.util.*;

import grid.Grid;  

public class Game {
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_BLACK = "\u001B[30m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";

	public void start() {
		System.out.print("\n###################################################################################################################################################\n");
		System.out.print("##" + ANSI_BLUE + "		 __    __   ______   __     __   ______   __              _______    ______   ________  ________  __        ________             " + ANSI_RESET + "##\n");
		System.out.print("##" + ANSI_BLUE + "		/  \\  /  | /      \\ /  |   /  | /      \\ /  |            /       \\  /      \\ /        |/        |/  |      /        |            " + ANSI_RESET + "##\n");
		System.out.print("##" + ANSI_BLUE + "		$$  \\ $$ |/$$$$$$  |$$ |   $$ |/$$$$$$  |$$ |            $$$$$$$  |/$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$ |      $$$$$$$$/             " + ANSI_RESET + "##\n");
		System.out.print("##" + ANSI_BLUE + "		$$$  \\$$ |$$ |__$$ |$$ |   $$ |$$ |__$$ |$$ |            $$ |__$$ |$$ |__$$ |   $$ |      $$ |   $$ |      $$ |__                " + ANSI_RESET + "##\n");
		System.out.print("##" + ANSI_BLUE + "		$$$$  $$ |$$    $$ |$$  \\ /$$/ $$    $$ |$$ |            $$    $$< $$    $$ |   $$ |      $$ |   $$ |      $$    |               " + ANSI_RESET + "##\n");
		System.out.print("##" + ANSI_BLUE + "		$$ $$ $$ |$$$$$$$$ | $$  /$$/  $$$$$$$$ |$$ |            $$$$$$$  |$$$$$$$$ |   $$ |      $$ |   $$ |      $$$$$/                " + ANSI_RESET + "##\n");
		System.out.print("##" + ANSI_BLUE + "		$$ |$$$$ |$$ |  $$ |  $$ $$/   $$ |  $$ |$$ |_____       $$ |__$$ |$$ |  $$ |   $$ |      $$ |   $$ |_____ $$ |_____             " + ANSI_RESET + "##\n");
		System.out.print("##" + ANSI_BLUE + "		$$ | $$$ |$$ |  $$ |   $$$/    $$ |  $$ |$$       |      $$    $$/ $$ |  $$ |   $$ |      $$ |   $$       |$$       |            " + ANSI_RESET + "##\n");
		System.out.print("##" + ANSI_BLUE + "		$$/   $$/ $$/   $$/     $/     $$/   $$/ $$$$$$$$/       $$$$$$$/  $$/   $$/    $$/       $$/    $$$$$$$$/ $$$$$$$$/             " + ANSI_RESET + "##\n");
		System.out.print("##                                                                                                                                               ##");
		System.out.print("\n###################################################################################################################################################\n");

		animateMessage("\n- Hi, welcome to our Naval Battle Game.", 0);
		animateMessage("\n- What's your name : ", 0);

		Scanner scanner = new Scanner(System.in);
		String answer = scanner.nextLine();
		
		animateMessage("- Hi " + answer + ", nice to meet you !", 0);
		animateMessage("\n\n- We are going to build a new grid for you...", 1000);
		
		Grid grid = new Grid();
		grid.saveBoat(Grid.AIRCRAFT_CARRIER_LENGTH, "A");
		grid.saveBoat(Grid.BATTLESHIP_LENGTH, "B");
		grid.saveBoat(Grid.CRUISER_LENGTH, "C");
		grid.saveBoat(Grid.DESTROYER_LENGTH, "D");
		grid.saveBoat(Grid.SUBMARINE_LENGTH, "S");

		animateMessage("\n- Done, there is your grid !", 0);

		animateMessage("\n", 0);
		grid.show();
		animateMessage("\n", 0);
		
		scanner.close();
	}

	public void animateMessage(String message, int pauseMs) {
		for (String letter : message.split("")) {
			System.out.print(letter);
			
			try { Thread.sleep(20);	}
			catch (Exception e) { System.out.println("ERROR");	}
		}

		try { Thread.sleep(pauseMs);	}
		catch (Exception e) { System.out.println("ERROR");	}
	}
}
