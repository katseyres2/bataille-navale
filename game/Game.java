package game;
import java.lang.Thread.State;
import java.util.*;

import game.grid.Grid;
import game.grid.Grid_old;
import services.FormatService;
import services.expections.OnlyOneActiveGameByPlayer;
import socket.server.Player;
import socket.server.Server;

public class Game {
	private ArrayList<Player> bots = new ArrayList<Player>();
	private ArrayList<Action> actions = new ArrayList<Action>();
	private ArrayList<Grid_old> gridOlds = new ArrayList<Grid_old>();
	private Thread thread = null;
	private Player firstPlayer;
	private Player playerTurn;
	private int turnCount = 0;
	private Player winner;

	public boolean hasPlayer(Player player) {
		for (Grid_old g : gridOlds) {
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
	public String sendAction(Player player, Player target, int column, int row) {
		boolean actionSuccessful = true;

		if (player == target) return "Hey, you target yourself.";
		if (playerTurn != player) return "That's not your turn.";
		
		// Fetch the target grid.
		Grid_old targetGridOld = findGridByPlayer(target);

		/**
		 * TODO try to hit a boat on this grid
		 * targetGrid.askPosition(null);
		 */

		if (!actionSuccessful) return "Action failed.";

		if (player == firstPlayer) turnCount++;

		Action action = new Action(player, targetGridOld, column, row, turnCount);
		actions.add(action);
		return null;
	}

	public String placePlayerBoat(Player player, int length , int column, int row, String vector) {
		boolean placedBoat = false;

		// Fetch the player grid.
		Grid playerGrid = findGridByPlayer(player);
		// get the boat with length

		placedBoat = placeBoat( ,column,row,vector);

		if (!placedBoat)
			return "Action failed.";

		return null;
	}

	public void nextPlayer() {
		int currentIndex = gridOlds.indexOf(findGridByPlayer(playerTurn));
		System.out.println("Current index1 : " + currentIndex);
		if (currentIndex == gridOlds.size()) currentIndex = 0;
		System.out.println("Current index2 : " + currentIndex);
		Player p =  findPlayerByGrid(gridOlds.get(currentIndex++));
		System.out.println("New player : " + p.getUsername());
		playerTurn = p;
	}

	public boolean isPlayerTurn(Player player) {
		return playerTurn == player;
	}

	public void askActionToPlayer(Player player) {
		if (!player.isLogged()) return;
		player.getPrintWriter().println("/sendaction");
		player.getPrintWriter().flush();
	}

	private void addGrid(Player player) throws OnlyOneActiveGameByPlayer {
		if (Server.getActiveGame(player) != null) throw new OnlyOneActiveGameByPlayer();

		for (Grid_old gridOld : gridOlds) {
			if (gridOld.getPlayer() == player) return;
		}

		Grid_old gridOld = new Grid_old(player);
		gridOlds.add(gridOld);
	}

	private Grid_old findGridByPlayer(Player player) {
		for (Grid_old g : gridOlds) {
			if (g.getPlayer() == player) {
				return g;
			}
		}
		return null;
	}

	private Player findPlayerByGrid(Grid_old gridOld) {
		for (Grid_old g : gridOlds) {
			if (g == gridOld) {
				return g.getPlayer();
			}
		}
		return null;
	}

	private void removeGrid(Player player) {
		Grid_old gridOld = findGridByPlayer(player);
		if (gridOld == null) return;
		gridOlds.remove(gridOld);
	}

	public void removeGrid(Grid_old gridOld) {
		if (gridOld == null) return;
		gridOlds.remove(gridOld);
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

	public String displayPlayerGrids(Player player) {
		String output = "";

		for (Grid_old gridOld : gridOlds) {
			if (gridOld.getPlayer() == player) continue;
			output += "-------------------------------- " + gridOld.getPlayer().getUsername().toUpperCase();
			output += gridOld;
			output += "--------------------------------;";
		}

		return output;
	}

	public void run() {
		if (gridOlds.size() < 2) return;
		int index = (new Random()).nextInt(gridOlds.size());
		firstPlayer = playerTurn = findPlayerByGrid(gridOlds.get(index));

		if (thread != null) return;

		thread = new Thread() {
			@Override
			public void run() {
				sendToClient(playerTurn, "That's your turn.;" + displayPlayerGrids(playerTurn) + FormatService.colorizeString(playerTurn.getColor(), "(" + playerTurn.getUsername() + ")--|"));

				while (winner == null) {
					try {
						Thread.sleep(5000); // decrease loop time
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
						break;
					}

					System.out.println(playerTurn.getUsername() + " turn.");

					Action lastAction = null;
					if (actions.size() > 0) lastAction = actions.get(actions.size() - 1);
					if (lastAction == null || lastAction.getPlayer() != playerTurn) continue;

					// Fetch the grid of the player who must play.
					Grid_old currentGridOld = findGridByPlayer(playerTurn);
					int currentIndex = gridOlds.indexOf(currentGridOld);
					int nextIndex = currentIndex += 1;

					if (currentIndex == gridOlds.size()) nextIndex = 0;

					// Fetch the next grid to play.
					Grid_old nextGridOld = gridOlds.get(nextIndex);
					playerTurn = nextGridOld.getPlayer();
					sendToClient(playerTurn, "That's your turn.;" + displayPlayerGrids(playerTurn) + FormatService.colorizeString(playerTurn.getColor(), "(" + playerTurn.getUsername() + ")--|"));

					System.out.println("Sent grid to player " + playerTurn.getUsername());
				}
			}
		};

		thread.start();
	}

	public ArrayList<Player> getPlayers()
	{
		ArrayList<Player> players = new ArrayList<Player>(){};

		for (Grid_old gridOld : gridOlds) {
			players.add(gridOld.getPlayer());
		}

		return players;
	}

	private void sendToClient(Player player, String message) {
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
