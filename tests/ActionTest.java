package tests;

import game.Game;
import org.junit.Test;
import socket.server.Player;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ActionTest {

    private ArrayList<socket.server.Player> Player;
//
    @Test
    public void testGetPlayer(){
        Action action = new Action();
        Player player1 = new Player(sc, "John","lud" );
        Player player2 = new Player(sc, "Fred","Bocca" );
        action.setPlayer(player1);

        assertEquals(player1, action.player1);

    }
    @Test
    public void testGetNullPlayer(){
        Game game = new Game();
        assertNull(game.getPlayers());
    }
}
