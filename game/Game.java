package game;
import java.lang.Thread.State;
import java.util.*;

import Bots.Bot;
import game.boat.Boat;
import game.grid.Cell;
import game.grid.Grid;
import org.jetbrains.annotations.NotNull;
import services.DirectionService;
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
	private Grid firstGrid;
	private Grid currentGrid;
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
	 * @return true if the action is successful, otherwise false.
	 */
	public @NotNull String sendAction(Player player, Player target, int row, int column) {
		boolean actionSuccessful = true;

		if (player == target) return "Hey, you target yourself.";
		if (currentGrid.getPlayer() != player) return "That's not your turn.";

		// Fetch the target grid.
		Grid targetGrid = DiscoveryService.findGrid(target, grids);
		if (targetGrid == null) return "The grid does not exist";

		Cell cell = DiscoveryService.findCellInGrid(row, column, targetGrid);

		if (player == firstGrid.getPlayer()) turnCount++;
		Action action = new Action(player, targetGrid, column, row, turnCount);
		actions.add(action);

		if (!DirectionService.isCellInGrid(cell, targetGrid)) return "You are out of the grid" + FormatService.showGrid(false, targetGrid);
		if (cell.isDiscovered()) return "You already hit this position" + FormatService.showGrid(false, targetGrid);
		cell.discover();

		Boat boat = DiscoveryService.findBoatWhichHasCell(cell, targetGrid.getBoats());
		if (boat == null) return "Sadly, it's only water..." + FormatService.showGrid(false, targetGrid);

		for (Cell c : boat.getCoordinates()) {
			if (! c.isDiscovered()) {
				return "You hit the boat " + boat.getName() + FormatService.showGrid(false, targetGrid);
			}
		}

		return "You just sink the boat " + boat.getName() + FormatService.showGrid(false, targetGrid);
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

	/**
	 * return true if is turn of the current player
	 * @param player
	 * @return
	 */
	public boolean isPlayerTurn(Player player) {
		return currentGrid != null && currentGrid.getPlayer() == player;
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
		String output = "";

		for (Grid grid : grids) {
			output += FormatService.showGrid( grid.getPlayer() == player , grid);
		}

		return output;
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

	public Grid getNextGrid() {
		int currentIndex = grids.indexOf(currentGrid);
		int nextIndex = currentIndex += 1;
		if (currentIndex == grids.size()) nextIndex = 0;

		// Fetch the next grid to play.
		return grids.get(nextIndex);
	}

	public void run() {
		grids.clear();

		if (grids.size() < 2) return;
		int index = (new Random()).nextInt(grids.size());
		firstGrid = currentGrid = grids.get(index);

		if (thread != null) return;

		thread = new Thread() {
			@Override
			public void run() {
				for (Grid grid : grids) {
					System.out.println("-- NEW GRID (" + grid.getPlayer().getUsername() + ") --");
					grid.populateRandomly();
				}

				while (winner == null) {
					System.out.println("TURN : " + currentGrid.getPlayer());
					try {
						Thread.sleep(50); // decrease loop time
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
						break;
					}

					if (currentGrid.getPlayer().isBot()) {
						Action action = ((Bot) currentGrid.getPlayer()).run();
						String response = sendAction(currentGrid.getPlayer(), action.getTarget().getPlayer(), action.getCell().getRow(), action.getCell().getColumn());
					}

					// there is no action
					if (actions.size() == 0) continue;
					Action lastAction = actions.get(actions.size() - 1);
					// there are actions but the last one is not from the current player
					if (lastAction.getPlayer() != currentGrid.getPlayer()) continue;

					if (currentGrid.allBoatAreSunk()) {
						// the game has a winner
						winner = Game.this.currentGrid.getPlayer();
						System.out.println("And the winner is " + currentGrid.getPlayer().getUsername() + " !!!");
						winner.addVictory();

						for (Player p : getPlayers()) {
							sendToClient(currentGrid.getPlayer(), p == winner ? "You win!" : "You loose");
						}
					} else {
						// go to the next grid
						currentGrid = getNextGrid();
						sendToClient(
							currentGrid.getPlayer(),
							"That's your turn.;"
								+ displayPlayerGrids(currentGrid.getPlayer())
								+ FormatService.colorizeString(Game.this.currentGrid.getPlayer().getColor(),
							"(" + Game.this.currentGrid.getPlayer().getUsername() + ")--|")
						);
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
