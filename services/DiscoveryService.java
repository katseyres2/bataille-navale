package services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import socket.client.SocketClient;
import socket.server.Player;

import java.net.Socket;
import java.util.ArrayList;

public class DiscoveryService {
    public static ArrayList<Player> findBy(Player needle, ArrayList<Player> haystack) {
        ArrayList<Player> output = new ArrayList<>();
        for (Player elem : haystack)
            if (elem.equals(needle)) output.add(elem);
        return output;
    }

    public static Object findOneBy(Player needle, ArrayList<Player> haystack) {
        var elements = findBy(needle, haystack);
        if (elements.size() == 0) return null;
        return elements.get(0);
    }

    public static @Nullable Player findOneBy(Socket socket, @NotNull ArrayList<Player> players) {
        if (socket == null) return null;

        for (Player player : players) {
            if (player.getSocket() == socket) {
                return player;
            }
        }

        return null;
    }

    public static @Nullable Player findOneBy(SocketClient client, @NotNull ArrayList<Player> players) {
        if (client == null) return null;

        for (Player player : players) {
            if (player.getSocket() == client.getSocket()) {
                return player;
            }
        }

        return null;
    }
}
