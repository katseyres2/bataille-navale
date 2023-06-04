package tests;

import game.Action;
import game.Game;
import game.grid.Grid;
import org.junit.Test;
import socket.server.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ActionTest {

    private ArrayList<socket.server.Player> Player;
//
    @Test
    public void testGetPlayer(){
        Socket s = new Socket();
        PrintWriter pw = new PrintWriter(System.out);
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        Player p = new Player(s, pw, bf, "test", "test");
        Grid grid = new Grid(p);
        Action action = new Action(p,grid, 2,3,2  );
        Player getPlayer = action.getPlayer();

        assertEquals(p, getPlayer);
    }

    @Test
    public void testGetTarget() {
        Socket s = new Socket();
        PrintWriter pw = new PrintWriter(System.out);
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        Player p = new Player(s, pw, bf, "test", "test");
        Grid grid = new Grid(p);
        Action action = new Action(p, grid, 2, 3, 3);

        Grid result = action.getTarget();

        assertEquals(grid, result);
    }

    @Test
    public void testGetColumn() {
        Socket s = new Socket();
        PrintWriter pw = new PrintWriter(System.out);
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        Player p = new Player(s, pw, bf, "test", "test");
        Grid grid = new Grid(p);
        int column = 2;
        Action action = new Action(p, grid, column, 3, 3);


        int result = action.getColumn();


        assertEquals(column, result);
    }

    @Test
    public void testGetRow() {
        Socket s = new Socket();
        PrintWriter pw = new PrintWriter(System.out);
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        Player p = new Player(s, pw, bf, "test", "test");
        Grid grid = new Grid(p);
        int row = 2;
        Action action = new Action(p, grid, 3, row, 3);


        int result = action.getRow();


        assertEquals(row, result);
    }

    @Test
    public void testGetTurnCount() {
        Socket s = new Socket();
        PrintWriter pw = new PrintWriter(System.out);
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        Player p = new Player(s, pw, bf, "test", "test");
        Grid grid = new Grid(p);
        int turnCount = 3;
        Action action = new Action(p, grid, 3, 2, turnCount);


        int result = action.getTurnCount();


        assertEquals(turnCount, result);
    }
}
