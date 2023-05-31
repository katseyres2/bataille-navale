package interfaces;

import game.grid.Grid;
import socket.Command.Role;
import socket.server.Player;

public interface IPlayer {
    public String getUsername();
    public void setUsername(String value);
    public void setColor(String value);
    public Role getRole();
    public void setRole(Role role);
    public int getDefeats();
    public int getVictories();
    public void incrementDefeat();
    public void decrementDefeat();
    public void incrementVictory();
    public void decrementVictory();
    public void addInUsersYouInvited(Player client);
    public void addInUsersWhoInvitedYou(Player client);
    public void removeFromUsersYouInvited(Player client);
    public void removeFromUserWhoInvitedYou(Player client);
}
