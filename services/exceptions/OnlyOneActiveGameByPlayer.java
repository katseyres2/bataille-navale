package services.exceptions;

/**
 * The OnlyOneActiveGameByPlayer exception is thrown when a player tries to create more than one active game.
 */
public class OnlyOneActiveGameByPlayer extends Throwable {

	/**
	 * Constructs a new OnlyOneActiveGameByPlayer exception with a default error message.
	 */
	public OnlyOneActiveGameByPlayer() {
		super("Only one active game by player.");
	}
}