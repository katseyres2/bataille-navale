package game;
import java.lang.Thread.State;
import java.util.*;

import game.grid.Grid;
import socket.Player;

public class Game {
	private ArrayList<Player> bots = new ArrayList<Player>();
	private ArrayList<Action> actions = new ArrayList<Action>();
	private ArrayList<Grid> grids = new ArrayList<Grid>();
	private Thread thread = null;
	private Player firstPlayer;
	private Player playerTurn;
	private int turnCount = 0;

	public boolean hasPlayer(Player player) {
		for (Grid g : grids) {
			if (g.getPlayer() == player) return true;
		}
		return false;
	}

	public void sendAction(Player player, int column, int row) {
		if (playerTurn != player) return;
		Grid grid = findGridByPlayer(player);
		//TODO FIREEEEEEE
		if (player == firstPlayer) turnCount++;
		actions.add(new Action(player, column, row, turnCount));
		playerTurn = getNextPlayer();
	}

	public Player getNextPlayer() {
		int currentIndex = grids.indexOf(findGridByPlayer(playerTurn));
		if (currentIndex == grids.size() - 1) currentIndex = 0;
		Player p =  findPlayerByGrid(grids.get(++currentIndex));
		return p;
	}

	public boolean isPlaying() {
		return thread.getState() == State.RUNNABLE;
	}

	public void askActionToPlayer(Player player) {
		if (!player.receiver.isLogged()) return;
		player.receiver.getPrintWriter().println("/sendaction");
		player.receiver.getPrintWriter().flush();
	}

	private void addGrid(Player player) {
		for (Grid grid : grids) {
			if (grid.getPlayer() == player) return;
		}

		Grid grid = new Grid(player);
		grids.add(grid);
	}

	private Grid findGridByPlayer(Player player) {
		for (Grid g : grids) {
			if (g.getPlayer() == player) {
				return g;
			}
		}
		return null;
	}

	private Player findPlayerByGrid(Grid grid) {
		for (Grid g : grids) {
			if (g == grid) {
				return g.getPlayer();
			}
		}
		return null;
	}

	private void removeGrid(Player player) {
		Grid grid = findGridByPlayer(player);
		if (grid == null) return;
		grids.remove(grid);
	}

	public void removeGrid(Grid grid) {
		if (grid == null) return;
		grids.remove(grid);
	}

	public void addPlayer(Player player) {
		if (player == null) return;
		addGrid(player);
	}

	public void removePlayer(Player player) {
		if (player == null) return;
		removeGrid(player);
	}

	public void addBot(Player bot) {
		if (bot == null || bots.contains(bot)) return;
		bots.add(bot);
	}

	public void removeBot(Player bot) {
		if (bot == null) return;
		bots.remove(bot);
	}

	private void pushAction(Action action) {
		if (actions.contains(action) || action == null) return;
		actions.add(action);
	}

	public void run() {
		if (grids.size() < 2) return;
		int index = (new Random()).nextInt(grids.size());
		firstPlayer = playerTurn = findPlayerByGrid(grids.get(index));

		if (thread != null) return;

		thread = new Thread() {
			public void run() {
				for (Grid grid : grids) {
					//TODO code more...
				}
			}
		};

		thread.start();
	}

	public void resume() {
		if (thread.getState() != State.WAITING) return;
		thread.notify();
	}

	public void stop() {
		if (thread == null || !thread.isAlive()) return;
		
		try {
			thread.wait();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	public void kill() {
		if (thread == null) return;
		thread.interrupt();
	}



	public void test() {
		System.out.println("TEST");
	}

	// public void start() {
	// 	System.out.print(
	// 			"\n###################################################################################################################################################\n");
	// 	System.out.print("##" + FormatService.ANSI_BLUE
	// 			+ "		 __    __   ______   __     __   ______   __              _______    ______   ________  ________  __        ________             "
	// 			+ FormatService.ANSI_RESET + "##\n");
	// 	System.out.print("##" + FormatService.ANSI_BLUE
	// 			+ "		/  \\  /  | /      \\ /  |   /  | /      \\ /  |            /       \\  /      \\ /        |/        |/  |      /        |            "
	// 			+ FormatService.ANSI_RESET + "##\n");
	// 	System.out.print("##" + FormatService.ANSI_BLUE
	// 			+ "		$$  \\ $$ |/$$$$$$  |$$ |   $$ |/$$$$$$  |$$ |            $$$$$$$  |/$$$$$$  |$$$$$$$$/ $$$$$$$$/ $$ |      $$$$$$$$/             "
	// 			+ FormatService.ANSI_RESET + "##\n");
	// 	System.out.print("##" + FormatService.ANSI_BLUE
	// 			+ "		$$$  \\$$ |$$ |__$$ |$$ |   $$ |$$ |__$$ |$$ |            $$ |__$$ |$$ |__$$ |   $$ |      $$ |   $$ |      $$ |__                "
	// 			+ FormatService.ANSI_RESET + "##\n");
	// 	System.out.print("##" + FormatService.ANSI_BLUE
	// 			+ "		$$$$  $$ |$$    $$ |$$  \\ /$$/ $$    $$ |$$ |            $$    $$< $$    $$ |   $$ |      $$ |   $$ |      $$    |               "
	// 			+ FormatService.ANSI_RESET + "##\n");
	// 	System.out.print("##" + FormatService.ANSI_BLUE
	// 			+ "		$$ $$ $$ |$$$$$$$$ | $$  /$$/  $$$$$$$$ |$$ |            $$$$$$$  |$$$$$$$$ |   $$ |      $$ |   $$ |      $$$$$/                "
	// 			+ FormatService.ANSI_RESET + "##\n");
	// 	System.out.print("##" + FormatService.ANSI_BLUE
	// 			+ "		$$ |$$$$ |$$ |  $$ |  $$ $$/   $$ |  $$ |$$ |_____       $$ |__$$ |$$ |  $$ |   $$ |      $$ |   $$ |_____ $$ |_____             "
	// 			+ FormatService.ANSI_RESET + "##\n");
	// 	System.out.print("##" + FormatService.ANSI_BLUE
	// 			+ "		$$ | $$$ |$$ |  $$ |   $$$/    $$ |  $$ |$$       |      $$    $$/ $$ |  $$ |   $$ |      $$ |   $$       |$$       |            "
	// 			+ FormatService.ANSI_RESET + "##\n");
	// 	System.out.print("##" + FormatService.ANSI_BLUE
	// 			+ "		$$/   $$/ $$/   $$/     $/     $$/   $$/ $$$$$$$$/       $$$$$$$/  $$/   $$/    $$/       $$/    $$$$$$$$/ $$$$$$$$/             "
	// 			+ FormatService.ANSI_RESET + "##\n");
	// 	System.out.print(
	// 			"##                                                                                                                                               ##");
	// 	System.out.print(
	// 			"\n###################################################################################################################################################\n");

	// 	animateMessage("\n- Hi, welcome to our Naval Battle Game.", 0);
	// 	animateMessage("\n- What's your name : ", 0);

	// 	Scanner scanner = new Scanner(System.in);
	// 	String answer = scanner.nextLine();

	// 	animateMessage("- Hi " + answer + ", nice to meet you !", 0);
	// 	animateMessage("\n\n- We are going to build a new grid for you...", 1000);

	// 	// Grid grid = new Grid();
	// 	// grid.placeBoatRandom(Grid.AIRCRAFT_CARRIER_LENGTH, "A");
	// 	// grid.placeBoatRandom(Grid.BATTLESHIP_LENGTH, "B");
	// 	// grid.placeBoatRandom(Grid.CRUISER_LENGTH, "C");
	// 	// grid.placeBoatRandom(Grid.DESTROYER_LENGTH, "D");
	// 	// grid.placeBoatRandom(Grid.SUBMARINE_LENGTH, "S");

	// 	Grid grid = new Grid();
	// 	grid.placeAllBoat();

	// 	for (int i = 0; i <= 20; i++)
	// 		grid.fire();

	// 	animateMessage("\n- Done, there is your grid !", 0);

	// 	animateMessage("\n", 0);
	// 	grid.show();
	// 	animateMessage("\n", 0);

	// 	scanner.close();
	// }

	public void animateMessage(String message, int pauseMs) {
		for (String letter : message.split("")) {
			System.out.print(letter);

			try {
				Thread.sleep(20);
			} catch (Exception e) {
				System.out.println("ERROR");
			}
		}

		try {
			Thread.sleep(pauseMs);
		} catch (Exception e) {
			System.out.println("ERROR");
		}
	}

}
