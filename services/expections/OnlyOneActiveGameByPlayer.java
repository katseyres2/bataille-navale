package services.expections;

public class OnlyOneActiveGameByPlayer extends Throwable {
	public OnlyOneActiveGameByPlayer() {
		super("Only one active game by player.");
	}
}
