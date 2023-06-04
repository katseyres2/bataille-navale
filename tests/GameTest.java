package tests;

import game.Game;
import game.grid.Grid;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import socket.client.SocketClient;
import socket.server.Player;
import socket.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;
    private Player player1;
    private Player player2;
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

    @Before
    public void setUp() {
        game = new Game();
        player1 = buildPlayer("player1", "123");
        player2 = buildPlayer("player2", "123");
    }

    @Test
    public void testHasPlayer() {
        assertFalse(game.hasPlayer(player1));

        // Add player1 to the game
        game.addPlayer(player1);

        assertTrue(game.hasPlayer(player1));
        assertFalse(game.hasPlayer(player2));
    }

    @Test
    public void testSendAction() {

        game.addPlayer(player1);
        game.addPlayer(player2);

        // Make player1's turn
        game.nextPlayer();

        // Send a valid action from player1 to player2
        String result = game.sendAction(player1, player2, 3, 4);

        assertNull(result); // Action should be successful

        // Send an action from player2 to itself
        result = game.sendAction(player2, player2, 1, 2);

        assertEquals("Hey, you target yourself.", result); // Action should fail

        // Send an action from player2 to player1 when it's not player2's turn
        result = game.sendAction(player2, player1, 5, 6);

        assertEquals("That's not your turn.", result); // Action should fail
    }

    @Test
    public void testNextPlayer() {

        game.addPlayer(player1);
        game.addPlayer(player2);

        // Make player1's turn
        game.nextPlayer();
        assertEquals(player1, game.isPlayerTurn(player1));

        // Make player2's turn
        game.nextPlayer();
        assertEquals(player2, game.isPlayerTurn(player2));

        // Make player1's turn again
        game.nextPlayer();
        assertEquals(player1, game.isPlayerTurn(player1));
    }

    @Test
    public void testIsPlayerTurn() {
        // Add player1 and player2 to the game
        game.addPlayer(player1);
        game.addPlayer(player2);


        game.nextPlayer();

        assertTrue(game.isPlayerTurn(player1));
        assertFalse(game.isPlayerTurn(player2));
    }

    @Test
    public void testAddPlayer() {
        // Add player1 to the game
        game.addPlayer(player1);

        assertTrue(game.hasPlayer(player1));
    }

    @Test
    public void testRemovePlayer() {

        game.addPlayer(player1);
        game.addPlayer(player2);

        assertTrue(game.hasPlayer(player1));
        assertTrue(game.hasPlayer(player2));

        // Remove player1 from the game
        game.removePlayer(player1);

        assertFalse(game.hasPlayer(player1));
        assertTrue(game.hasPlayer(player2));
    }


    @Test
    public void testGetPlayers() {
        // Add player1 and player2 to the game
        game.addPlayer(player1);
        game.addPlayer(player2);

        assertEquals(2, game.getPlayers().size());
        assertTrue(game.getPlayers().contains(player1));
        assertTrue(game.getPlayers().contains(player2));
    }
}