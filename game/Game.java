package game;
import java.lang.Thread.State;
import java.util.*;

import game.grid.Grid;
import services.expections.OnlyOneActiveGameByPlayer;
import socket.server.Server;

public class Game {
	private ArrayList<Player> bots = new ArrayList<Player>();
	private ArrayList<Action> actions = new ArrayList<Action>();
	private ArrayList<Grid> grids = new ArrayList<Grid>();
	private Thread thread = null;
	private Player firstPlayer;
	private Player playerTurn;
	private int turnCount = 0;
	private Player winner;

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
	 * @param column The vertical coordinate.
	 * @param row The horizontal coordinate.
	 * @return true if the action is successful, otherwise false.
	 */
	public boolean sendAction(Player player, Player target, int column, int row) {
		boolean actionSuccessful = false;
		if (playerTurn != player) return false;
		
		// Fetch the target grid.
		Grid targetGrid = findGridByPlayer(target);

		/**
		 * TODO try to hit a boat on this grid
		 * targetGrid.askPosition(null);
		 */

		if (!actionSuccessful) return false;

		if (player == firstPlayer) turnCount++;
		actions.add(new Action(player, targetGrid, column, row, turnCount));
		playerTurn = getNextPlayer();
		return true;
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
		if (!player.isLogged()) return;
		player.getPrintWriter().println("/sendaction");
		player.getPrintWriter().flush();
	}

	private void addGrid(Player player) throws OnlyOneActiveGameByPlayer {
		if (Server.getActiveGame(player) != null) throw new OnlyOneActiveGameByPlayer();

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
		
		try {
			addGrid(player);
		} catch (OnlyOneActiveGameByPlayer e) {
			System.out.println(e.getMessage());
		}
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

	public void run() {
		if (grids.size() < 2) return;
		int index = (new Random()).nextInt(grids.size());
		firstPlayer = playerTurn = findPlayerByGrid(grids.get(index));

		if (thread != null) return;

		thread = new Thread() {
			public void run() {
				while (winner == null) {
					Grid grid = findGridByPlayer(playerTurn);
					sendToClient(grid.getPlayer(), "It's your turn.");
				}
			}
		};

		thread.start();
	}

	public ArrayList<Player> getPlayers()
	{
		ArrayList<Player> players = new ArrayList<Player>(){};

		for (Grid grid : grids) {
			players.add(grid.getPlayer());
		}

		return players;
	}

	private void sendToClient(Player player, String message) {
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
