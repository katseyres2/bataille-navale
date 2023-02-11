// package game;

// import game.grid.Cell;
// import game.grid.Grid;
// import socket.Player;

// public class Player extends Player {
// 	private String username;
// 	private int victories;
// 	private int defeats;

// 	public Player() {
// 		defeats = 0;
// 		victories = 0;
// 	}

// 	public static Player copyOf(Player player) {
// 		Player p = new Player();
// 		p.setBufferedReader(player.getBufferedReader());
// 		p.setSocket(player.getSocket());
// 		p.setPrintWriter(player.getPrintWriter());
// 		p.setUsername(player.getUsername());
// 		p.setPassword(player.getPassword());
// 		p.setColor(player.getColor());

// 		return p;
// 	}

// 	public String getUsername() { return username; }
// 	public void setUsername(String value) { username = value; }

// 	public int getVictories() { return victories; }
// 	public int getDefeats() { return defeats; }
// 	public int getNumberOfGamesPlayed() { return defeats + victories; }

// 	public void inviteOpponent(Player opponent) {

// 	}

// 	/*
// 	 * 
// 	 */
// 	public void inviteAI() {

// 	}

// 	// /*
// 	//  * 
// 	//  */
// 	// private static Player generateAI() {
// 	// 	Player ai = new Player();
// 	// 	ai.setUsername("bot" + (new Random()).nextInt(1000));
// 	// 	return ai;
// 	// }

// 	/*
// 	 * 
// 	 */
// 	public void shoot(Grid grid, Cell cell) {

// 	}
// }
