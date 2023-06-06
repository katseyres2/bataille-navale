package Bots;

import game.Action;
import game.Game;
import game.grid.Cell;
import game.grid.Grid;
import org.jetbrains.annotations.NotNull;
import services.DiscoveryService;
import socket.server.Player;
import socket.server.Server;

import java.util.*;

/**
 * Represents a bot player in a game.
 */
public class Bot extends Player {

    /**
     * Enum representing the difficulty levels of the bot.
     */
    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
    }


    private final Difficulty difficulty;
    private static final Random random = new Random();

    /**
     * Creates a new bot player with the specified username and difficulty level.
     *
     * @param username   The username of the bot player.
     * @param difficulty The difficulty level of the bot.
     */
    public Bot(String username, Difficulty difficulty) {
        super(null, null, null, username, null, true);
        this.difficulty = difficulty;
    }



    public boolean isLogged() {
        return true;
    }

    /**
     * Retrieves the current difficulty level.
     *
     * @return The current difficulty level.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }


    /*
     * Executes the bots turn in the game.
     */
    public Action run() {
//        System.out.println("run START");
        // Retrieve the active game in which the bot is playing
        Game game = Server.getActiveGame(this);
        // Retrieve the list of players in the game
        ArrayList<Player> players = game.getPlayers();
//        System.out.println("Player : " + players);
        Grid randomGrid = null;

        do {
            // Select a random player from the available players
            Player player = players.get(random.nextInt(players.size()));
            // Find the grid associated with the selected player
            randomGrid = DiscoveryService.findGrid(player, game.getGrids());
//            System.out.println("Select target " + player.getUsername());
        } while (randomGrid.getPlayer() == this);

        Cell cell = null;

        // Execute the turn based on the bots difficulty level
        switch (difficulty) {
            case EASY -> cell = startTurnEasy(randomGrid);
            case MEDIUM -> cell = startTurnMedium(randomGrid);
            case HARD -> cell = startTurnHard(randomGrid);
            default -> System.out.println("Difficulty is not defined");
        }

//        System.out.println("run END");
        return new Action(this, randomGrid, cell.getColumn(), cell.getRow(), game.getTurnCount());
    }



    /**
     * Executes the bots turn when the difficulty is set to "EASY".
     *
     * @param grid The grid on which the bot will perform its action.
     * @return The action chosen by the bot.
     */
    private @NotNull Cell startTurnEasy(@NotNull Grid grid) {
        // Select a random cell from the grid
        ArrayList<ArrayList<Cell>> cells = grid.getGrid();

        var random = new Random();
        int randRow = random.nextInt(cells.size());
        int randCol = random.nextInt(cells.get(0).size());
        return grid.getGrid().get(randRow).get(randCol);
    }


    /**
     * Executes the bots turn when the difficulty is set to "MEDIUM".
     *
     * @param grid The grid on which the bot will perform its action.
     * @return The action chosen by the bot.
     */
    private @NotNull Cell startTurnMedium(@NotNull Grid grid) {
        int rowCount = grid.getRows();
        int columnCount = grid.getColumns();
        List<Cell> boatCells = new ArrayList<>();

        // Iterate through all cells in the grid
        for (int x = 0; x < columnCount; x++) {
            for (int y = 0; y < rowCount; y++) {
                Cell cell = grid.getCellFromPosition(x, y);
                // Check if the cell contains an undiscovered boat
                if (cell != null && grid.isEmptyCell(cell) && !cell.isDiscovered()) {
                    boatCells.add(cell);
                }
            }
        }

        // Check if there are cells with undiscovered boats and randomly decide whether to choose one or not
        if (!boatCells.isEmpty() && random.nextBoolean()) {
            return boatCells.get(random.nextInt(boatCells.size()));
        } else {
            // Select a random cell from the grid
            return grid.getRandomCell(grid.getEmptyCells());
        }
    }


    /**
     * Executes the bot's turn when the difficulty is set to "HARD".
     *
     * @param grid The grid on which the bot will perform its action.
     * @return The action chosen by the bot.
     */
    private @NotNull Cell startTurnHard(@NotNull Grid grid) {
        int rowCount = grid.getRows();
        int columnCount = grid.getColumns();
        List<Cell> undiscoveredBoatCells = new ArrayList<>();

        // Iterate through all cells in the grid
        for (int x = 0; x < columnCount; x++) {
            for (int y = 0; y < rowCount; y++) {
                Cell cell = grid.getCellFromPosition(x, y);
                // Check if the cell contains an undiscovered boat
                if (cell != null && grid.isEmptyCell(cell) && !cell.isDiscovered()) {
                    undiscoveredBoatCells.add(cell);
                }
            }
        }

        // Check if there are cells with undiscovered boats
        if (!undiscoveredBoatCells.isEmpty()) {
            // Sort the cells based on their likelihood of containing a boat (calculated by the calculateLikelihood method)
            Collections.sort(undiscoveredBoatCells, (c1, c2) -> {
                double likelihood1 = calculateLikelihood(c1, grid);
                double likelihood2 = calculateLikelihood(c2, grid);
                return Double.compare(likelihood2, likelihood1);
            });

            // Select the cell with the highest likelihood of containing a boat
            return undiscoveredBoatCells.get(0);
        } else {
            // If no undiscovered boats are found, execute the turn in "MEDIUM" mode
            return startTurnMedium(grid);
        }
    }


    /**
     * Calculates the likelihood of a cell containing a boat.
     *
     * @param cell The cell for which to calculate the likelihood.
     * @param grid The grid in which the cell is located.
     * @return The calculated likelihood.
     */
    private double calculateLikelihood(Cell cell, Grid grid) {
        // Returns a random probability value between 0.5 and 1.0
        return 0.5 + random.nextDouble() * 0.5;
    }

}