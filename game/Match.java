package game;

import socket.Player;
import game.grid.Grid;
import socket.Player;
import socket.Server;
import socket.commands.Command.Role;

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
		gridP1 = new Grid();
		gridP2 = new Grid();

		try{

			// demander aux joueurs de placer les bateaux
			player1.getPrintWriter().println("test");
			player1.getPrintWriter().flush();
			// attendre que les bateaux sois placer pour les 2 joueurs

		} catch (Exception e) {
			player1.getPrintWriter().println(e);
			player1.getPrintWriter().flush();
		}
	}
}
