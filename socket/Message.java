package socket;

import org.jetbrains.annotations.NotNull;
import socket.server.Player;
import java.time.LocalDateTime;

/**
 * Class representing a message.
 */
public class Message {
    private String text;
    private Player from;
    private LocalDateTime time;

    /**
     * Constructor for the Message class.
     *
     * @param text The text content of the message.
     * @param from The player who sent the message.
     */
    public Message(@NotNull String text, @NotNull Player from) {
        this.text = text;
        time = LocalDateTime.now();
        this.from = from;
    }

    /**
     * Retrieves the text content of the message.
     *
     * @return The text content of the message.
     */
    public String getText() {
        return text;
    }

    /**
     * Retrieves the timestamp of the message.
     *
     * @return The timestamp of the message.
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Retrieves the player who sent the message.
     *
     * @return The player who sent the message.
     */
    public Player getFrom() {
        return this.from;
    }
}