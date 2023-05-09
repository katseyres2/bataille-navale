package socket;

import org.jetbrains.annotations.NotNull;
import socket.server.Player;
import java.time.LocalDateTime;

public class Message {
    private String text;
    private Player from;
    private LocalDateTime time;

    public Message(@NotNull String text, @NotNull Player from) {
        this.text = text;
        time = LocalDateTime.now();
        this.from = from;
    }

    public String getText() { return text; }
    public LocalDateTime getTime() { return time; }
    public Player getFrom() { return this.from; }
}
