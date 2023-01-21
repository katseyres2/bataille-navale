package game;
import java.util.Random;

import game.grid.Cell;
import game.grid.Grid;
import socket.Client;

public class Player extends Client {
	private String username;
	private int victories;
	private int defeats;

	public Player() {
		defeats = 0;
		victories = 0;
	}

	public String getUsername() { return username; }
	public void setUsername(String value) { username = value; }

	public int getVictories() { return victories; }
	public int getDefeats() { return defeats; }
	public int getNumberOfGamesPlayed() { return defeats + victories; }

	public void inviteOpponent(Player opponent) {

	}

	/*
	 * 
	 */
	public void inviteAI() {

	}

	// /*
	//  * 
	//  */
	// private static Player generateAI() {
	// 	Player ai = new Player();
	// 	ai.setUsername("bot" + (new Random()).nextInt(1000));
	// 	return ai;
	// }

	/*
	 * 
	 */
	public void shoot(Grid grid, Cell cell) {

	}
}
