package Bots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class representing a bot with medium difficulty level.
 * Inherits from the BotsTurn class.
 */
public class Moyen extends BotsTurn {

    // The coordinates of the bot's current target, null if no target has been found yet
    private final Integer[] coordonnees;

    // A list of coordinates of target points that the bot has already hit
    private List<int[]> alreadyTarget;

    // Random number generator
    private Random random;

    /**
     * Constructor for the Moyen bot.
     *
     * @param grid          The grid representing the player's ships
     * @param coordonnees   The coordinates of the bot's current target
     * @param alreadyTarget A list of coordinates of target points that the bot has already hit
     */
    public Moyen(ArrayList<ArrayList<String>> grid, Integer[] coordonnees, List<int[]> alreadyTarget) {
        super(grid);
        this.coordonnees = coordonnees;
        this.alreadyTarget = alreadyTarget;
        random = new Random();
    }

    /**
     * Executes the bot's turn.
     *
     * @param playerGrid The grid representing the player's ships
     * @return True if the bot's turn is over, false if the bot has found a target and needs to continue
     */
    public Boolean startTurn(ArrayList<ArrayList<String>> playerGrid) {

        /*
        STEPS OF A TURN:
            Functions like sendGrid are defined in the BotsTurn class
            1. Check if the bot has already hit a ship. If so, check if the direction is known
            2. If no ship has been hit, try to find one. If the bot cannot hit a ship, return the grid
            3. Continue to hit the ship in the known direction
         */

        if (coordonnees[0] == null && coordonnees[1] == null) {

            // If the bot has not hit a ship yet, pick a random point and try to hit a ship there
            int x = random.nextInt(getGrid().size());
            int y = random.nextInt(getGrid().get(x).size());
            int[] targetPoint = checkSearchShip(x, y);

            if (targetPoint != null) {
                coordonnees[0] = targetPoint[0];
                coordonnees[1] = targetPoint[1];
                return false;
            }

        } else {

            // If the bot has hit a ship, try to hit the ship in the known direction
            String direction = startSearchDirection(random.nextInt(4));
        }

        return true;
    }

    /**
     * Determines the direction to search for the ship in.
     *
     * @param direction A number representing the initial direction to search in
     * @return A string representing the direction to search in
     */
    private String startSearchDirection(int direction) {

        int[] directionFromInteger = null;

        switch (direction) {
            case 0:
                directionFromInteger = new int[]{-1, 0};
            case 2:
                directionFromInteger = new int[]{1, 0};
            case 3:
                directionFromInteger = new int[]{0, -1};
            case 1:
                directionFromInteger = new int[]{0, 1};
        }

        checkTargetWithDirection(directionFromInteger);

        return null;
    }


    /**
     * Checks if the target with direction is valid
     *
     * @param newTarget an array containing the x and y coordinates of the new target
     * @return a boolean indicating whether the target is valid
     */
    private Boolean checkTargetWithDirection(int[] newTarget) {

        // Check if the new target is a ship
        if (checkSearchShip(newTarget[0], newTarget[1]) != null) {
            // Add code to handle valid target
        }

        // Return false if target is invalid
        return false;
    }

    /**
     * Searches for a ship at the given coordinates
     *
     * @param x the x coordinate of the target
     * @param y the y coordinate of the target
     * @return an array containing the x and y coordinates of the target if it is a ship, null otherwise
     */
    private int[] checkSearchShip(int x, int y) {

        // Create an array to store the coordinates of the target
        int[] output = new int[2];
        output[0] = x;
        output[1] = y;

        // Check if the target has already been selected as a target point
        if (alreadyTarget.contains(output)) {
            // Add code to handle case where target has already been selected
        }

        // Add the target to the list of target points
        alreadyTarget.add(output);

        // Check if the target is a ship or not
        if (getGrid().get(x).get(y).equals(" ")) {
            // Return null if target is not a ship
            return null;
        }
        // Return the coordinates of the target if it is a ship
        return output;
    }
}
