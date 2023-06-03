package tests;

import game.boat.Boat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.FormatService;
import socket.client.SocketClient;
import socket.server.Player;
import socket.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static socket.Command.Role.ADMIN;

public class PlayerTest {
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
    public void demoTestMethod() {
         assertTrue(true);
    }

    @Test
    public void testBoat() {
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
    public void playerCredentials() {
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
    public void commands() {

        Player p = buildPlayer(username, password);
        p.sendMessage("/signup " + p.getUsername() + " " + password);
        p.sendMessage("/help");

        try { Thread.sleep(1000); }
        catch (Exception ignored) {}

        assertEquals("/help", Server.findLastMessageFrom(p.getUsername()).getText());
    }

   /* @Test
    public void checkTime()
    {
        Time LocalDateTime = Time.valueOf(now());

        if (LocalDateTime == Time.valueOf(now())) assertTrue(false);
        assertEquals(LocalDateTime, Time.valueOf(String.valueOf(LocalDateTime)));
        *//*assertNotEquals(LocalDateTime, Time.valueOf((String.valueOf(LocalDateTime))));*//*

    }*/

   /* @Test
    public void testGetColorPlayer() throws IOException {
        Socket s  = new Socket();
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        SocketClient sc = new SocketClient(s, pw, br);
        Player player = new Player(sc, "abc", "trtt" );
        player.setColor(FormatService.typeColor.ANSI_RED);
        assertEquals("red", player.getColor());
    }*/

    @Test
    public void testGetColorPlayer() {
        SocketClient sc = buildSocketClient();
        Player player = new Player(sc, username, password);
        player.setColor(FormatService.typeColor.ANSI_GREEN);
        assertEquals(FormatService.typeColor.ANSI_GREEN, player.getColor());
    }

    @Test
    public void testGetRole() {
        SocketClient sc = buildSocketClient();
        Player player = new Player(sc, username, password);
        player.setRole(ADMIN);
        assertEquals(ADMIN, player.getRole());
    }
//sert pour tester si la méthode gestdefats retourne correctement la defaite enregistrée
    @Test
    public void testGetDefeats() {
        SocketClient sc = buildSocketClient();
        Player player = new Player(sc, username, password);
        player.addDefeat();
        assertEquals(1, player.getDefeats());
    }
// Test le nom d'utilisateur. La méthode retourne t -elle le nom d'utilisateur attendu?
    @Test
    public void testGetUsername() {
        SocketClient sc = buildSocketClient();
        Player player = new Player(sc, username, password);
        String LAZARE = null;
        player.setUsername("LAZARE");
        assertEquals("LAZARE", player.getUsername());
    }

// test de l'ajout du mot de passe. Est ce que la méthode retourne bien le mot de passe attendu?
    @Test
    public void testGetPassword() {
        SocketClient sc = buildSocketClient();
        Player player = new Player(sc, username, password);
       String eaeScae = "Laz";
        player.setPassword("eaeScae") ;
        assertEquals("eaeScae", player.getPassword());
    }




/*
    @Test
    public void checkTime() {
        Time LocalDateTime = Time.valueOf(now());

        if (LocalDateTime == Time.valueOf(now())) assertTrue(false);
        assertEquals(LocalDateTime, Time.valueOf(String.valueOf(LocalDateTime)));
                /*assertNotEquals(LocalDateTime, Time.valueOf((String.valueOf(LocalDateTime))));*/


    }


