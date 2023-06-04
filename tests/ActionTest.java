package tests;

import game.Action;
import game.Game;
import game.grid.Grid;
import org.junit.Test;
import socket.client.SocketClient;
import socket.server.Player;
import socket.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ActionTest {

    static final String host = "127.0.0.1";
    static final int port = Server.PORT;

    Player buildPlayer(String username, String password) {
        return new Player(buildSocketClient(), username, password);
    }

    SocketClient buildSocketClient() {
        Socket s;
        PrintWriter pw;
        BufferedReader br;

        try {
            s = new Socket(host, port);
            pw = new PrintWriter(s.getOutputStream());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return new SocketClient(s, pw, br);
    }

    private ArrayList<socket.server.Player> Player;
//
    @Test
    public void testGetPlayer(){
        Player p = buildPlayer("test", "test");
        Grid grid = new Grid(p,10,10);
        Action action = new Action(p,grid, 2,3,2  );
        Player getPlayer = action.getPlayer();

        assertEquals(p, getPlayer);
    }

    @Test
    public void testGetTarget() {
        Player p = buildPlayer("test", "test");
        Grid grid = new Grid(p,10,10);
        Action action = new Action(p, grid, 2, 3, 3);

        Grid result = action.getTarget();

        assertEquals(grid, result);
    }

    @Test
    public void testGetColumn() {
        Player p = buildPlayer("test", "test");
        Grid grid = new Grid(p,10,10);
        int column = 2;
        Action action = new Action(p, grid, column, 3, 3);


        int result = action.getColumn();


        assertEquals(column, result);
    }

    @Test
    public void testGetRow() {
        Player p = buildPlayer("test", "test");
        Grid grid = new Grid(p,10,10);
        int row = 2;
        Action action = new Action(p, grid, 3, row, 3);


        int result = action.getRow();


        assertEquals(row, result);
    }

    @Test
    public void testGetTurnCount() {
        Player p = buildPlayer("test", "test");
        Grid grid = new Grid(p,10,10);
        int turnCount = 3;
        Action action = new Action(p, grid, 3, 2, turnCount);


        int result = action.getTurnCount();


        assertEquals(turnCount, result);
    }
}
