package interfaces;

import game.grid.Grid;
import socket.Command.Role;
import socket.server.Player;

/**
 * The IPlayer interface represents a player in the game.
 */
public interface IPlayer {

    /**
     * Retrieves the username of the player.
     *
     * @return the username of the player.
     */
    public String getUsername();

    /**
     * Sets the username of the player.
     *
     * @param value the username to set.
     */
    public void setUsername(String value);

    /**
     * Sets the color of the player.
     *
     * @param value the color to set.
     */
    public void setColor(String value);

    /**
     * Retrieves the role of the player.
     *
     * @return the role of the player.
     */
    public Role getRole();

    /**
     * Sets the role of the player.
     *
     * @param role the role to set.
     */
    public void setRole(Role role);

    /**
     * Retrieves the number of defeats for the player.
     *
     * @return the number of defeats for the player.
     */
    public int getDefeats();

    /**
     * Retrieves the number of victories for the player.
     *
     * @return the number of victories for the player.
     */
    public int getVictories();

    /**
     * Increments the number of defeats for the player.
     */
    public void incrementDefeat();

    /**
     * Decrements the number of defeats for the player.
     */
    public void decrementDefeat();

    /**
     * Increments the number of victories for the player.
     */
    public void incrementVictory();

    /**
     * Decrements the number of victories for the player.
     */
    public void decrementVictory();

    /**
     * Adds a player to the list of users the player has invited.
     *
     * @param client the player to add.
     */
    public void addInUsersYouInvited(Player client);

    /**
     * Adds a player to the list of users who have invited the player.
     *
     * @param client the player to add.
     */
    public void addInUsersWhoInvitedYou(Player client);

    /**
     * Removes a player from the list of users the player has invited.
     *
     * @param client the player to remove.
     */
    public void removeFromUsersYouInvited(Player client);

    /**
     * Removes a player from the list of users who have invited the player.
     *
     * @param client the player to remove.
     */
    public void removeFromUserWhoInvitedYou(Player client);
}