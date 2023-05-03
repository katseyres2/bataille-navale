package game;

import game.grid.Grid;
import socket.server.Player;

public class Action {
	private Player player;
	private Grid target;
	private int column;
	private int row;
	private int turnCount;

	public Action(Player player, Grid target, int column, int row, int turnCount) {
		this.player = player;
		this.target = target;
		this.column = column;
		this.row = row;
		this.turnCount = turnCount;
	}

	public Player getPlayer() 	{ return player; }
	public Grid getTarget() 	{ return target; }
	public int getColumn() 		{ return column; }
	public int getRow() 		{ return row; }
	public int getTurnCount() 	{ return turnCount; }
}
