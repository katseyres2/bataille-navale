package game;
import java.io.PrintWriter;
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

	/**
	 * Get the list of grids in the game.
	 *
	 * @return The list of grids.
	 */
	public ArrayList<Grid> getGrids() {
		return grids;
	}

	/**
	 * Get the turn count of the game.
	 *
	 * @return The turn count.
	 */
	public int getTurnCount() {
		return turnCount;
	}

	/**
	 * Check if the specified player is part of the game.
	 *
	 * @param player The player to check.
	 * @return true if the player is in the game, false otherwise.
	 */
	public boolean hasPlayer(Player player) {
		for (Grid g : grids) {
			if (g.getPlayer() == player) {
				return true;
			}
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
	 * Check if it is the turn of the specified player.
	 *
	 * @param player The player to check.
	 * @return true if it is the turn of the player, false otherwise.
	 */
	public boolean isPlayerTurn(Player player) {
		return currentGrid != null && currentGrid.getPlayer() == player;
	}

	/**
	 * Ask the specified player for an action.
	 *
	 * @param player The player to ask for an action.
	 */
	public void askActionToPlayer(Player player) {
		if (!player.isLogged()) {
			return;
		}
		PrintWriter writer = player.getPrintWriter();
		writer.println("/sendaction");
		writer.flush();
	}

	private void addGrid(Player player) throws OnlyOneActiveGameByPlayer {
		System.out.println("ADDGRID START");
		if (Server.getActiveGame(player) != null) {
			throw new OnlyOneActiveGameByPlayer();
		}

		for (Grid grid : grids) {
			if (grid.getPlayer() == player) {
				return;
			}
		}

		Grid grid = new Grid(player, 10, 10);
		grids.add(grid);
		System.out.println("ADDGRID END");
	}


	/**
	 * Return the player associated with the specified grid.
	 *
	 * @param grid The grid to find the player for.
	 * @return The player associated with the grid, or null if not found.
	 */
	private Player findPlayerByGrid(Grid grid) {
		for (Grid g : grids) {
			if (g == grid) {
				return g.getPlayer();
			}
		}
		return null;
	}

	/**
	 * Remove the grid associated with the specified player from the game.
	 *
	 * @param player The player whose grid should be removed.
	 */
	private void removeGrid(Player player) {
		Grid grid = DiscoveryService.findGrid(player, grids);
		if (grid == null) {
			return;
		}
		grids.remove(grid);
	}

	/**
	 * Remove the specified grid from the game.
	 *
	 * @param grid The grid to remove.
	 */
	public void removeGrid(Grid grid) {
		if (grid == null) {
			return;
		}
		grids.remove(grid);
	}

	/**
	 * Add a player to the game.
	 *
	 * @param player The player to add.
	 */
	public void addPlayer(Player player) {
		System.out.println("ADD PLAYER START");
		if (player == null) {
			return;
		}

		try {
			addGrid(player);
		} catch (OnlyOneActiveGameByPlayer e) {
			System.out.println(e.getMessage());
		}

		System.out.println("ADD PLAYER END");
	}

	/**
	 * Remove a player from the game.
	 *
	 * @param player The player to remove.
	 */
	public void removePlayer(Player player) {
		if (player == null) {
			return;
		}
		removeGrid(player);
	}

	/**
	 * Remove a bot from the game.
	 *
	 * @param bot The bot to remove.
	 */
	public void removeBot(Player bot) {
		if (bot == null) {
			return;
		}
		bots.remove(bot);
	}

	/**
	 * Display the grids associated with a player.
	 *
	 * @param player The player whose grids to display.
	 * @return The formatted string representation of the player's grids.
	 */
	public String displayPlayerGrids(Player player) {
		String output = "";

		for (Grid grid : grids) {
			output += FormatService.showGrid(grid.getPlayer() == player, grid);
		}

		return output;
	}

	/**
	 * Retrieves a list of grids where the boats are not all placed yet.
	 *
	 * @return The list of grids that are not yet configured.
	 */
	public List<Grid> getGridsNotConfigured() {
		List<Grid> notReady = new ArrayList<>();

		// Print the size of the grids list for debugging purposes
		System.out.println("grids = " + grids.size());

		// Iterate through each grid and check if it is not configured
		for (Grid grid : grids) {
			if (!grid.isConfigured()) {
				notReady.add(grid);
			}
		}

		return notReady;
	}

	/**
	 * Retrieves the next grid to play.
	 *
	 * @return The next grid to play.
	 */
	public Grid getNextGrid() {
		int currentIndex = grids.indexOf(currentGrid);
		int nextIndex = currentIndex += 1;

		// If the current index is at the end of the list, wrap around to the beginning
		if (currentIndex == grids.size()) {
			nextIndex = 0;
		}

		// Fetch the next grid to play
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

	/**
	 * Retrieves the list of players associated with the game.
	 *
	 * @return the list of players
	 */
	public ArrayList<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<>();  // Create a new ArrayList to store the players

		for (Grid grid : grids) {
			players.add(grid.getPlayer());  // Add each player associated with a grid to the list
		}

		return players;
	}

	/**
	 * Sends a message to the client associated with the player.
	 *
	 * @param player  the player to send the message to
	 * @param message the message to send
	 */
	private void sendToClient(@NotNull Player player, String message) {
		if (player.isBot()) {
			return;  // If the player is a bot, return without sending the message
		}

		while (player.getPrintWriter() == null) {
			System.out.println("Waiting for " + player.getUsername() + " to reconnect.");  // Print a waiting message

			try {
				Thread.sleep(500);  // Wait for 500 milliseconds
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

		player.getPrintWriter().println(message);  // Send the message to the player's PrintWriter
		player.getPrintWriter().flush();  // Flush the PrintWriter to ensure the message is sent immediately
	}

	/**
	 * Resumes the execution of the game thread.
	 * If the thread state is not WAITING, the method returns without doing anything.
	 */
	public void resume() {
		if (thread.getState() != State.WAITING) return;  // If the thread state is not WAITING, return without resuming
		thread.notify();  // Notify the thread to resume execution
	}

	/**
	 * Stops the execution of the game thread.
	 * If the thread is null or not alive, the method returns without doing anything.
	 */
	public void stop() {
		if (thread == null || !thread.isAlive()) return;  // If the thread is null or not alive, return without stopping

		try {
			thread.wait();  // Wait for the thread to complete
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Kills the game thread by interrupting it.
	 * If the thread is null, the method returns without doing anything.
	 */
	public void kill() {
		if (thread == null) return;  // If the thread is null, return without killing

		thread.interrupt();  // Interrupt the thread to stop its execution
	}

	/**
	 * Animates a message by printing each letter with a pause between each letter and a pause after the full message.
	 *
	 * @param message  the message to animate
	 * @param pauseMs  the pause duration in milliseconds after each letter
	 */
	public void animateMessage(String message, int pauseMs) {
		for (String letter : message.split("")) {
			System.out.print(letter);  // Print each letter

			try {
				Thread.sleep(20);  // Pause for 20 milliseconds
			} catch (Exception e) {
				System.out.println("ERROR");
			}
		}

		try {
			Thread.sleep(pauseMs);  // Pause after the full message
		} catch (Exception e) {
			System.out.println("ERROR");
		}
	}

}
