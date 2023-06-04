package game;
import java.lang.Thread.State;
import java.util.*;

import Bots.Bot;
import game.boat.Boat;
import game.grid.Cell;
import game.grid.Grid;
import org.jetbrains.annotations.NotNull;
import services.DiscoveryService;
import services.FormatService;
import services.exceptions.OnlyOneActiveGameByPlayer;
import socket.server.Player;
import socket.server.Server;

public class Game {
	private final ArrayList<Player> bots = new ArrayList<>();
	private final ArrayList<Action> actions = new ArrayList<>();
	private final ArrayList<Grid> grids = new ArrayList<>();
	private Thread thread = null;
	private Player firstPlayer;
	private Player playerTurn;
	private int turnCount = 0;
	private Player winner;

	public ArrayList<Grid> getGrids() {
		return grids;
	}

	public int getTurnCount() {
		return turnCount;
	}


	public boolean hasPlayer(Player player) {
		for (Grid g : grids) {
			if (g.getPlayer() == player) return true;
		}
		return false;
	}

	/**
	 * The action is successful if the player played its turned successfully.
	 * A wrong action example is an overflow position like (22,5) in a grid 10x10.
	 * @param player the player who sent the coordinates.
	 * @param target the player to target.
	 * @param cell
	 * @return true if the action is successful, otherwise false.
	 */
	public String sendAction(Player player, Player target, Cell cell) {
		boolean actionSuccessful = true;

		if (player == target) return "Hey, you target yourself.";
		if (playerTurn != player) return "That's not your turn.";

		// Fetch the target grid.
		Grid targetGrid = DiscoveryService.findGrid(target, grids);
		// get the message from the fire position : you hit, you miss, ...
		String message = targetGrid.fire(cell);
		if (message == null){
			actionSuccessful = false;
		}

		if (!actionSuccessful) return "Action failed.";

		if (player == firstPlayer) turnCount++;

		Action action = new Action(player, targetGrid, cell.getColumn(), cell.getRow(), turnCount);
		actions.add(action);
//		return message;
		return "DEBUG";
	}

	/**
	 * place boat of the current player in his grid
	 * @param player
	 * @param length
	 * @param column
	 * @param row
	 * @param vector
	 * @return
	 */
	public String placePlayerBoat(Player player, int length , int column, int row, String vector) {
		boolean placedBoat = false;
		// Fetch the player grid.
		Grid playerGrid = DiscoveryService.findGrid(player, grids);
		Boat.Model playerBoat = DiscoveryService.findModelByLength(length);

		if(playerBoat != null){
			// get the boat with length
//			placedBoat = playerGrid.placeBoat(playerBoat, column, row, vector);
		}

		if (!placedBoat) return "Action failed.";
		else return "Your boat" + playerBoat.getName()  + "has been placed";
	}

	public void nextPlayer() {
		int currentIndex = grids.indexOf(DiscoveryService.findGrid(playerTurn, grids));
		System.out.println("Current index1 : " + currentIndex);
		if (currentIndex == grids.size()) currentIndex = 0;
		System.out.println("Current index2 : " + currentIndex);
		Player p =  findPlayerByGrid(grids.get(currentIndex++));
		System.out.println("New player : " + p.getUsername());
		playerTurn = p;
	}

	/**
	 * return true if is turn of the current player
	 * @param player
	 * @return
	 */
	public boolean isPlayerTurn(Player player) {
		return playerTurn == player;
	}

	public void askActionToPlayer(Player player) {
		if (!player.isLogged()) return;
		player.getPrintWriter().println("/sendaction");
		player.getPrintWriter().flush();
	}


	private void addGrid(Player player) throws OnlyOneActiveGameByPlayer {
		System.out.println("ADDGRID START");
		if (Server.getActiveGame(player) != null) throw new OnlyOneActiveGameByPlayer();

		for (Grid grid : grids) {
			if (grid.getPlayer() == player) return;
		}

		Grid grid = new Grid(player,10,10);
		grids.add(grid);
		System.out.println("ADDGRID END");
	}


	/**
	 * Return the player with his grid as parameter
	 * @param grid
	 * @return
	 */
	private Player findPlayerByGrid(Grid grid) {
		for (Grid g : grids) {
			if (g == grid) {
				return g.getPlayer();
			}
		}
		return null;
	}

	private void removeGrid(Player player) {
		Grid grid = DiscoveryService.findGrid(player, grids);
		if (grid == null) return;
		grids.remove(grid);
	}

	public void removeGrid(Grid grid) {
		if (grid == null) return;
		grids.remove(grid);
	}

	public void addBot(Player bot) {
		System.out.println("ADD BOT START");
		if (bot == null || bots.contains(bot)) return;

		try {
			addGrid(bot);
		} catch (OnlyOneActiveGameByPlayer e) {
			System.out.println(e.getMessage());
		}

		bots.add(bot);
		System.out.println("ADD BOT END");
	}

	/**
	 *
	 * @param player player
	 */
	public void addPlayer(Player player) {
		System.out.println("ADD PLAYER START");
		if (player == null) return;

		try {
			addGrid(player);
		} catch (OnlyOneActiveGameByPlayer e) {
			System.out.println(e.getMessage());
		}

		System.out.println("ADD PLAYER END");
	}

	public void removePlayer(Player player) {
		if (player == null) return;
		removeGrid(player);
	}

	public void removeBot(Player bot) {
		if (bot == null) return;
		bots.remove(bot);
	}

	public String displayPlayerGrids(Player player) {
		for (Grid grid : grids) {
			if (grid.getPlayer() != player) {
				return grid.toString(true);
			}
		}

		return "No grid";
	}

	/**
	 * get a list of grids where the boats are not all placed yet
	 * @return
	 */
	public List<Grid> getGridsNotConfigured(){
		List<Grid> notReady  = new ArrayList<>();
		System.out.println("grids = " + grids.size());

		for ( Grid grid : grids) {
			if(!grid.isConfigured()){
				notReady.add(grid);
			}
		}

		return notReady;
	}

	public void run() {
		if (grids.size() < 2) return;
		int index = (new Random()).nextInt(grids.size());
		firstPlayer = playerTurn = findPlayerByGrid(grids.get(index));

		if (thread != null) return;

		thread = new Thread() {
			@Override
			public void run() {
//				List<Grid> gridsNotConfigured;
//
//				do {
//					gridsNotConfigured = getGridsNotConfigured();
//					System.out.println("gridsNotConfigured = " + gridsNotConfigured);
//
//					for (Grid notConfiguredYet : gridsNotConfigured) {
//						sendToClient(notConfiguredYet.getPlayer(), "Place your Boats" + displayPlayerGrids(notConfiguredYet.getPlayer()));
//					}
//
//					try {
//						Thread.sleep(3000); // decrease loop time
//					} catch (InterruptedException e) {
//						System.out.println(e.getMessage());
//						break;
//					}
//				} while(!gridsNotConfigured.isEmpty());

				addBot(new Bot("BotLeBricoleur", Bot.Difficulty.EASY));
				addBot(new Bot("BotLer", Bot.Difficulty.EASY));

				for (Grid grid : grids) {
					System.out.println("-- NEW GRID (" + grid.getPlayer().getUsername() + ") --");
					grid.populateRandomly();
				}

				System.out.println("SEND TO " + playerTurn.getUsername());
				sendToClient(playerTurn, "That's your turn.;" + displayPlayerGrids(playerTurn) + FormatService.colorizeString(playerTurn.getColor(), "(" + playerTurn.getUsername() + ")--|"));

				while (winner == null) {
					try {
						Thread.sleep(5000); // decrease loop time
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
						break;
					}

					//System.out.println(playerTurn.getUsername() + " turn.");
					System.out.println(playerTurn.isBot());
					if(playerTurn.isBot()){

						Bot bot = (Bot) playerTurn;
						System.out.println("c'est le tour du bot : " + bot.getUsername());
						bot.run();
					}else {
						System.out.println("déchet : " + playerTurn.getUsername());

						// Fetch the grid of the player who must play.
						Grid currentGrid = DiscoveryService.findGrid(playerTurn,grids);
						int currentIndex = grids.indexOf(currentGrid);
						int nextIndex = currentIndex += 1;

						if (currentIndex == grids.size()) nextIndex = 0;

						// Fetch the next grid to play.
						Grid nextGrid = grids.get(nextIndex);
						playerTurn = nextGrid.getPlayer();

						continue;
					}

					Action lastAction = null;
					if (actions.size() > 0) lastAction = actions.get(actions.size() - 1);
					if (lastAction == null || lastAction.getPlayer() != playerTurn){
						System.out.println("pas d'action");
						continue;
					}

					// Fetch the grid of the player who must play.
					Grid currentGrid = DiscoveryService.findGrid(playerTurn,grids);
					int currentIndex = grids.indexOf(currentGrid);
					int nextIndex = currentIndex += 1;

					if (currentIndex == grids.size()) nextIndex = 0;

					// Fetch the next grid to play.
					Grid nextGrid = grids.get(nextIndex);
					playerTurn = nextGrid.getPlayer();
					sendToClient(playerTurn, "That's your turn.;" + displayPlayerGrids(playerTurn) + FormatService.colorizeString(playerTurn.getColor(), "(" + playerTurn.getUsername() + ")--|"));

					System.out.println("Sent grid to player " + playerTurn.getUsername());

					if (currentGrid.allBoatAreSink()) {
						winner = playerTurn;
						sendToClient(playerTurn, "You WIN !!!!");
						playerTurn.addVictory();
						nextPlayer();
						playerTurn.addDefeat();
						sendToClient(playerTurn, "You LOOSE");
					}
				}
			}
		};

		thread.start();
	}

	public ArrayList<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<>(){};

		for (Grid grid : grids) {
			players.add(grid.getPlayer());
		}

		return players;
	}

	private void sendToClient(@NotNull Player player, String message) {
		if(player.isBot()){
			return;
		}
		while (player.getPrintWriter() == null) {
			System.out.println("waiting for " + player.getUsername() + " reconnection.");

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

		player.getPrintWriter().println(message);
		player.getPrintWriter().flush();
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
