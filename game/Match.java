package game;

import game.grid.Grid;

public class Match implements Runnable {
	public Player player1;
	public Player player2;
	public Grid gridP1;
	public Grid gridP2;

	public Match(Player p1, Player p2) {
		this.player1 = p1;
		this.player2 = p2;
	}

	public void startGame() {
		Thread gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		gridP1 = new Grid(player1);
		gridP2 = new Grid(player2);

		try{
			// ask to players to place the boast
			player1.getPrintWriter().println("test");
			player1.getPrintWriter().flush();
			// wait for the boat placement
		} catch (Exception e) {
			player1.getPrintWriter().println(e);
			player1.getPrintWriter().flush();
		}
	}
}
