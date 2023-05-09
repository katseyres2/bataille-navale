package game;

import game.grid.Grid_old;

public class Match implements Runnable {
	public Player player1;
	public Player player2;
	public Grid_old gridOldP1;
	public Grid_old gridOldP2;

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
		gridOldP1 = new Grid_old(player1);
		gridOldP2 = new Grid_old(player2);

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
