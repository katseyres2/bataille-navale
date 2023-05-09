package tests;

import socket.client.SocketClient;
import socket.server.Player;
import game.boat.Boat;
import org.junit.jupiter.api.*;
import socket.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class MyTest {
    static Server server;
    static String username = "john";
    static String password = "1234";
    static final String host = "127.0.0.1";
    static final int port = Server.PORT;

    @BeforeAll
    public static void configure() {
        Thread thServer = new Thread(new Runnable() {
            @Override
            public void run() {
                server = new Server();
                server.start(Server.PORT);
            }
        });

        thServer.start();
    }

    @Test
    void demoTestMethod() {
         assertTrue(true);
    }

    @Test
    void testBoat() {
        Boat boat = new Boat(Boat.typeBoat.CRUISER);

        assertEquals(Boat.typeBoat.CRUISER, boat.type);
        assertEquals(4, boat.getHp());
        assertEquals(false, boat.isSink());
    }

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

    @Test
    void playerCredentials() {
        Player player = buildPlayer(username, password);
        if (player == null) assertTrue(false);

        assertEquals(username, player.getUsername());
        assertTrue(player.checkCredentials(username, password));
        assertFalse(player.checkCredentials(username, "notDoe"));
        assertFalse(player.checkCredentials("", ""));
        assertEquals(0, player.getDefeats());
        assertEquals(0, player.getVictories());

        player.setUsername("jane");
        assertEquals("jane", player.getUsername());
        assertTrue(player.checkCredentials("jane", password));

        player.setPassword("1234");
        assertTrue(player.checkCredentials("jane", "1234"));
        assertEquals("jane", player.getUsername());
    }

    @Test
    void commands() {

        Player p = buildPlayer(username, password);
        p.sendMessage("/signup " + p.getUsername() + " " + password);
        p.sendMessage("/help");

        try { Thread.sleep(1000); }
        catch (Exception ignored) {}

        assertEquals("/help", Server.findLastMessageFrom(p.getUsername()).getText());
    }
}
