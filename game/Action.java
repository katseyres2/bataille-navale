package game;

import game.grid.Cell;

public class Action {
	private Player player;
	private int column;
	private int row;
	private int turnCount;

	public Action(Player player, int column, int row, int turnCount) {
		this.player = player;
		this.column = column;
		this.row = row;
		this.turnCount = turnCount;
	}
}
