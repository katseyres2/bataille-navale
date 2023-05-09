package game;

import game.grid.Grid;
import socket.server.Player;

public class Action {
	private Player player; // the player who made the action
	private Grid target; // the target grid of the action
	private int column; // the column index of the target cell
	private int row; // the row index of the target cell
	private int turnCount; // the turn count at which the action was made

	/**
	 * Constructor for the Action class
	 * @param player the player who made the action
	 * @param target the target grid of the action
	 * @param column the column index of the target cell
	 * @param row the row index of the target cell
	 * @param turnCount the turn count at which the action was made
	 */
	public Action(Player player, Grid target, int column, int row, int turnCount) {
		this.player = player;
		this.target = target;
		this.column = column;
		this.row = row;
		this.turnCount = turnCount;
	}

	/**
	 * Get the player who made the action
	 * @return the player who made the action
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Get the target grid of the action
	 * @return the target grid of the action
	 */
	public Grid getTarget() {
		return target;
	}

	/**
	 * Get the column index of the target cell
	 * @return the column index of the target cell
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Get the row index of the target cell
	 * @return the row index of the target cell
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Get the turn count at which the action was made
	 * @return the turn count at which the action was made
	 */
	public int getTurnCount() {
		return turnCount;
	}
}

