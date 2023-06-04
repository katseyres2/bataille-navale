package tests;

import game.Action;
import game.grid.Grid;
import org.junit.Test;
import socket.client.SocketClient;
import socket.server.Player;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import static jdk.internal.org.jline.utils.Colors.s;
import static org.junit.Assert.assertEquals;

public class ActionTest {

    private ArrayList<socket.server.Player> Player;
//
//    @Test
//    public void testGetPlayer(){
//        Action action = new Action(Player player, Grid target, int column, int row, int turnCount);
//        Grid grid = new Grid;
//
//        Player player1 = new Player(sc, "John","lud" );
//        Player player2 = new Player(sc, "Fred","Bocca" );
//        action.setPlayer(player1);
//
//        assertEquals(player1, action.Getplayer();
//
//    }
    @Test
    public void testGetPlayer() {
        SocketClient sc = new SocketClient();
        Player player1 = new Player(sc, "John", "lud");
        Player player2 = new Player(sc, "Fred", "Bocca");
        Grid grid = new Grid(player1);
        Action action = new Action(player1, grid, column, row, turnCount);
        action.setPlayer(player1);
        assertEquals(player1, action.getPlayer());
    }



}
