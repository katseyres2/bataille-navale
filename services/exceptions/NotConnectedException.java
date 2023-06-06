package services.exceptions;

/**
 * The NotConnectedException is thrown when a connection is required but not established.
 */
public class NotConnectedException extends Throwable {

	/**
	 * Constructs a new NotConnectedException with a default error message.
	 */
	public NotConnectedException() {
		super("Not connected!");
	}
}