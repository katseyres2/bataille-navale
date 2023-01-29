package game;
import java.util.*;

import game.grid.Grid;
import services.FormatService;  

public class Game {
	public void start() {
		System.out.print("\n###################################################################################################################################################\n");
		System.out.print("##" + FormatService.ANSI_BLUE + "		 __    __   ______   __     __   ______   __              _______    ______   ________  ________  __        ________             " + FormatService.ANSI_RESET + "##\n");
		System.out.print("##" + FormatService.ANSI_BLUE + "		/  \\  /  | /      \\ /  |   /  | /      \\ /  |            /       \\  /      \\ /        |/        |/  |      /        |            " + FormatService.ANSI_RESET + "##\n");
		System.out.print("##" + FormatService.ANSI_BLUE + "		$$  \\ $$ |/$$$$$$  |$$ |   $$ |/$$$$$$  |$$ |            $$$$$$$  |/$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$ |      $$$$$$$$/             " + FormatService.ANSI_RESET + "##\n");
		System.out.print("##" + FormatService.ANSI_BLUE + "		$$$  \\$$ |$$ |__$$ |$$ |   $$ |$$ |__$$ |$$ |            $$ |__$$ |$$ |__$$ |   $$ |      $$ |   $$ |      $$ |__                " + FormatService.ANSI_RESET + "##\n");
		System.out.print("##" + FormatService.ANSI_BLUE + "		$$$$  $$ |$$    $$ |$$  \\ /$$/ $$    $$ |$$ |            $$    $$< $$    $$ |   $$ |      $$ |   $$ |      $$    |               " + FormatService.ANSI_RESET + "##\n");
		System.out.print("##" + FormatService.ANSI_BLUE + "		$$ $$ $$ |$$$$$$$$ | $$  /$$/  $$$$$$$$ |$$ |            $$$$$$$  |$$$$$$$$ |   $$ |      $$ |   $$ |      $$$$$/                " + FormatService.ANSI_RESET + "##\n");
		System.out.print("##" + FormatService.ANSI_BLUE + "		$$ |$$$$ |$$ |  $$ |  $$ $$/   $$ |  $$ |$$ |_____       $$ |__$$ |$$ |  $$ |   $$ |      $$ |   $$ |_____ $$ |_____             " + FormatService.ANSI_RESET + "##\n");
		System.out.print("##" + FormatService.ANSI_BLUE + "		$$ | $$$ |$$ |  $$ |   $$$/    $$ |  $$ |$$       |      $$    $$/ $$ |  $$ |   $$ |      $$ |   $$       |$$       |            " + FormatService.ANSI_RESET + "##\n");
		System.out.print("##" + FormatService.ANSI_BLUE + "		$$/   $$/ $$/   $$/     $/     $$/   $$/ $$$$$$$$/       $$$$$$$/  $$/   $$/    $$/       $$/    $$$$$$$$/ $$$$$$$$/             " + FormatService.ANSI_RESET + "##\n");
		System.out.print("##                                                                                                                                               ##");
		System.out.print("\n###################################################################################################################################################\n");

		animateMessage("\n- Hi, welcome to our Naval Battle Game.", 0);
		animateMessage("\n- What's your name : ", 0);

		Scanner scanner = new Scanner(System.in);
		String answer = scanner.nextLine();
		
		animateMessage("- Hi " + answer + ", nice to meet you !", 0);
		animateMessage("\n\n- We are going to build a new grid for you...", 1000);

		/*
		Grid grid = new Grid();
		grid.placeBoatRandom(Grid.AIRCRAFT_CARRIER_LENGTH, "A");
		grid.placeBoatRandom(Grid.BATTLESHIP_LENGTH, "B");
		grid.placeBoatRandom(Grid.CRUISER_LENGTH, "C");
		grid.placeBoatRandom(Grid.DESTROYER_LENGTH, "D");
		grid.placeBoatRandom(Grid.SUBMARINE_LENGTH, "S");

		 */

		Grid grid = new Grid();
		grid.placeBoat(Grid.AIRCRAFT_CARRIER_LENGTH, "A", 7, 4, "SOUTH");

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
