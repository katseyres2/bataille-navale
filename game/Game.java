package game;
import java.lang.Thread.State;
import java.lang.reflect.Array;
import java.util.*;

import game.boat.Boat;
import game.grid.Grid;
import game.grid.Grid_old;
import services.FormatService;
import services.exceptions.OnlyOneActiveGameByPlayer;
import socket.server.Player;
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

	public void addBot(Player bot) {
		if (bot == null || bots.contains(bot)) return;
		bots.add(bot);
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
	 * @param column The vertical coordinate.
	 * @param row The horizontal coordinate.
	 * @return true if the action is successful, otherwise false.
	 */
	public String sendAction(Player player, Player target, int column, int row) {
		boolean actionSuccessful = true;

		if (player == target) return "Hey, you target yourself.";
		if (playerTurn != player) return "That's not your turn.";

		// Fetch the target grid.
		Grid targetGrid = findGridByPlayer(target);
		// get the message from the fire position : you hit, you miss, ...
		String message = targetGrid.fire(column, row);
		if (message == null){
			actionSuccessful = false;
		}

		if (!actionSuccessful) return "Action failed.";

		if (player == firstPlayer) turnCount++;

		Action action = new Action(player, targetGrid, column, row, turnCount);
		actions.add(action);
		return message;
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
		Grid playerGrid = findGridByPlayer(player);
		Boat playerBoat = playerGrid.getBoatWithLength(length);
		if(playerBoat != null){
			// get the boat with length
			placedBoat = playerGrid.placeBoat(playerBoat, column, row, vector);
		}
		if (!placedBoat)
			return "Action failed.";
		else return "Your boat" + playerBoat.getType().name  + "has been placed";
	}

	public void nextPlayer() {
		int currentIndex = grids.indexOf(findGridByPlayer(playerTurn));
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
	 * return the grid of a player with the player as parameter
	 * @param player
	 * @return
	 */
	public Grid findGridByPlayer(Player player) {
		for (Grid g : grids) {
			if (g.getPlayer() == player) {
				return g;
			}
		}
		return null;
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
		Grid grid = findGridByPlayer(player);
		if (grid == null) return;
		grids.remove(grid);
	}

	public void removeGrid(Grid grid) {
		if (grid == null) return;
		grids.remove(grid);
	}

	/**
	 *
	 * @param player
	 */
	public void addPlayer(Player player) {
		System.out.println("ADDPLAYER START");
		if (player == null) return;

		try {
			addGrid(player);
		} catch (OnlyOneActiveGameByPlayer e) {
			System.out.println(e.getMessage());
		}

		System.out.println("ADDPLAYER END");
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
			if (grid.getPlayer() == player) continue;
			output += "-------------------------------- " + grid.getPlayer().getUsername().toUpperCase();
			output += grid;
			output += "--------------------------------;";
		}

		return output;
	}

	/**
	 * get a list of grids where the boats are not all placed yet
	 * @return
	 */
	public List<Grid> getGridsNotConfigured(){
		List<Grid> notReady  = new ArrayList<>();
		for ( Grid notConfiguredGrid : grids   ) {
				if(!notConfiguredGrid.isConfigured()){
					notReady.add(notConfiguredGrid);
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
				List<Grid> gridsNotConfigured = getGridsNotConfigured();
				do{
					for (Grid notConfiguredYet : gridsNotConfigured) {
						sendToClient(notConfiguredYet.getPlayer(), "Place your Boats" + displayPlayerGrids(notConfiguredYet.getPlayer()));
						gridsNotConfigured = getGridsNotConfigured();
					}
					try {
						Thread.sleep(30000); // decrease loop time
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
						break;
					}
				}while(!gridsNotConfigured.isEmpty());


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
					Grid currentGrid = findGridByPlayer(playerTurn);
					int currentIndex = grids.indexOf(currentGrid);
					int nextIndex = currentIndex += 1;

					if (currentIndex == grids.size()) nextIndex = 0;

					// Fetch the next grid to play.
					Grid nextGrid = grids.get(nextIndex);
					playerTurn = nextGrid.getPlayer();
					sendToClient(playerTurn, "That's your turn.;" + displayPlayerGrids(playerTurn) + FormatService.colorizeString(playerTurn.getColor(), "(" + playerTurn.getUsername() + ")--|"));

					System.out.println("Sent grid to player " + playerTurn.getUsername());
					if(currentGrid.allBoatAreSink()){
						winner = playerTurn;
						sendToClient(playerTurn, "You WON !!!!");
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
		ArrayList<Player> players = new ArrayList<Player>(){};

		for (Grid grid : grids) {
			players.add(grid.getPlayer());
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
