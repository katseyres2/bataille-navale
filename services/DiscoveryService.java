package services;

import game.Game;
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
    /**
     * Finds players that match the specified player object in the list.
     *
     * @param needle   The player to search for.
     * @param haystack The list of players to search in.
     * @return The list of found players.
     */
    public static ArrayList<Player> findBy(Player needle, ArrayList<Player> haystack) {
        ArrayList<Player> output = new ArrayList<>();
        for (Player elem : haystack) {
            if (elem.equals(needle)) {
                output.add(elem);
            }
        }
        return output;
    }

    /**
     * Finds the first player that matches the specified player object in the list.
     *
     * @param needle   The player to search for.
     * @param haystack The list of players to search in.
     * @return The first found player, or null if not found.
     */
    public static Player findOneBy(Player needle, ArrayList<Player> haystack) {
        var elements = findBy(needle, haystack);
        if (elements.size() == 0) {
            return null;
        }
        return elements.get(0);
    }

    /**
     * Finds a player in the game by username.
     *
     * @param username The username of the player to search for.
     * @param game     The game to search in.
     * @return The found player, or null if not found.
     */
    public static Player findPlayerInGameByUsername(String username, Game game) {
        for (Player p : game.getPlayers()) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    public static Player findPlayerByUsername(String username, ArrayList<Player> players) {
        for (Player p : players) {
            if (p.getUsername().compareTo(username) == 0) return p;
        }
        return null;
    }

    /**
     * Finds the player that corresponds to the specified socket in the list of players.
     *
     * @param socket  The socket to search for.
     * @param players The list of players to search in.
     * @return The found player, or null if not found.
     */
    public static @Nullable Player findOneBy(Socket socket, @NotNull ArrayList<Player> players) {
        if (socket == null) {
            return null;
        }

        for (Player player : players) {
            if (player.getSocket() == socket) {
                return player;
            }
        }

        return null;
    }

    /**
     * Finds the player that corresponds to the specified socket client in the list of players.
     *
     * @param client  The socket client to search for.
     * @param players The list of players to search in.
     * @return The found player, or null if not found.
     */
    public static @Nullable Player findOneBy(SocketClient client, @NotNull ArrayList<Player> players) {
        if (client == null) {
            return null;
        }

        for (Player player : players) {
            if (player.getSocket() == client.getSocket()) {
                return player;
            }
        }

        return null;
    }

    /**
     * Finds a cell with the specified row and column in the list of cells.
     *
     * @param row   The row of the cell.
     * @param column The column of the cell.
     * @param cells The list of cells to search in.
     * @return The found cell, or null if not found.
     */
    public static @Nullable Cell findCell(int row, int column, @NotNull ArrayList<Cell> cells) {
        // Iterate over each cell already filled by another boat
        for (Cell cell : cells) {
            // Check if this cell matches the current row and column
            if (cell.getColumn() == column && cell.getRow() == row) {
                return cell;
            }
        }

        return null;
    }

    /**
     * Finds a cell with the specified row and column in the grid.
     *
     * @param row    The row of the cell.
     * @param column The column of the cell.
     * @param grid   The grid to search.
     * @return The found cell, or null if not found.
     */
    public static Cell findCellInGrid(int row, int column, Grid grid) {
        for (Cell cell : grid.getAllCells()) {
            if (cell.getColumn() == column && cell.getRow() == row) {
                return cell;
            }
        }
        return null;
    }

    /**
     * Finds a cell with the specified row and column in the list of boats.
     *
     * @param row      The row of the cell.
     * @param column   The column of the cell.
     * @param haystack The list of boats to search.
     * @return The found cell, or null if not found.
     */
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
     * Finds the boat which contains the specified cell in the list of boats.
     *
     * @param cell  The cell to search for.
     * @param boats The list of boats to search.
     * @return The boat containing the cell, or null if not found.
     */
    public static @Nullable Boat findBoatWhichHasCell(Cell cell, @NotNull ArrayList<Boat> boats) {
        for (Boat b : boats) {
            for (Cell c : b.getCoordinates()) {
                if (c.getRow() == cell.getRow() && c.getColumn() == cell.getColumn()) {
                    return b;
                }
            }
        }
        return null;
    }


    /**
     *
     * @param length boat length
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
     * @param player player
     * @return Grid
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
