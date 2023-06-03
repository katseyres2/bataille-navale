package services;

import game.boat.Boat;
import game.grid.Cell;
import game.grid.Grid;
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

    public static Player findOneBy(Player needle, ArrayList<Player> haystack) {
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

    public static @Nullable Cell findCell(int row, int column, @NotNull ArrayList<Cell> cells) {
        // iterate on each cell already filled by another boat
        for (Cell cell : cells) {
            // check this one matches with the current
            if (cell.getColumn() == column && cell.getRow() == row) {
                return cell;
            }
        }

        return null;
    }

    public static Cell findCellInBoats(int row, int column, ArrayList<Boat> haystack) {
        for (Boat b : haystack) {
            for (Cell c : b.getCoordinates()) {
                if (c.getRow() == row && c.getColumn() == column) {
                    return c;
                }
            }
        }

        return null;
    }

    /**
     *
     * @param length
     * @return return a boat from the list by his length
     */
    public static Boat.Model findModelByLength(int length) {
        for (Boat.Model model : Boat.Model.values()) {
            if (model.getLength() == length) {
                return model;
            }
        }
        return null;
    }

    /**
     * return the grid of a player with the player as parameter
     * @param player
     * @return
     */
    public static Grid findGrid(Player player, ArrayList<Grid> grids) {
        for (Grid g : grids) {
            if (g.getPlayer() == player) {
                return g;
            }
        }
        return null;
    }
}
