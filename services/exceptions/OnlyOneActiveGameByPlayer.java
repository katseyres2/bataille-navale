package services.exceptions;

public class OnlyOneActiveGameByPlayer extends Throwable {
	public OnlyOneActiveGameByPlayer() {
		super("Only one active game by player.");
	}
}
