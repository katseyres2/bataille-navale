package tests;

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
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    public void testCheckCredentials() {
        Player player = buildPlayer(username, password);
        if (player == null) assertTrue(false);


        player.setUsername("user1");
        player.setPassword("pass1");


        boolean result1 = player.checkCredentials("user1", "pass1");

        boolean result2 = player.checkCredentials("user2", "pass2");


        assertEquals(true, result1);
        assertEquals(false, result2);
    }


    @Test
    public void testGetColorPlayer() {
        SocketClient sc = buildSocketClient();
        Player player = new Player(sc, username, password);
        player.setColor(FormatService.ANSI_GREEN);
        assertEquals(FormatService.ANSI_GREEN, player.getColor());
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
    @Test
    public void testGetVictories() {
        SocketClient sc = buildSocketClient();
        Player player = new Player(sc, username, password);
        player.addVictory();
        int result = player.getVictories();

        assertEquals(1, result);
    }

    // Test le nom d'utilisateur. La méthode retourne t -elle le nom d'utilisateur attendu?
    @Test
    public void testGetUsername() {
        SocketClient sc = buildSocketClient();
        Player player = new Player(sc, username, password);
        player.setUsername("LAZARE");
        assertEquals("LAZARE", player.getUsername());
    }

// test de l'ajout du mot de passe. Est ce que la méthode retourne bien le mot de passe attendu?
    @Test
    public void testGetPassword() {
        SocketClient sc = buildSocketClient();
        Player player = new Player(sc, username, password);
        player.setPassword("eaeScae") ;
        assertEquals("eaeScae", player.getPassword());
    }

    @Test
    public void testGetUsersYouInvited() {
        SocketClient sc = buildSocketClient();
        Player player1 = new Player(sc, username, password);
        SocketClient sc2 = buildSocketClient();
        Player playerToInvite = new Player(sc2, username, password);

        // Add the invitedPlayer to usersYouInvited
        player1.addInUsersYouInvited(playerToInvite);

        ArrayList<Player> result = player1.getUsersYouInvited();

        assertEquals(1, result.size());
        assertEquals(playerToInvite, result.get(0));
    }

    @Test
    public void testGetUsersWhoInvitedYou() {
        SocketClient sc = buildSocketClient();
        Player player1 = new Player(sc, username, password);
        SocketClient sc2 = buildSocketClient();
        Player playerWhoInviteYou = new Player(sc2, username, password);


        player1.addInUsersWhoInvitedYou(playerWhoInviteYou);


        ArrayList<Player> result = player1.getUsersWhoInvitedYou();


        assertEquals(1, result.size());
        assertEquals(playerWhoInviteYou, result.get(0));
    }

    @Test
    public void testRemoveFromUsersYouInvited() {
        SocketClient sc = buildSocketClient();
        Player player1 = new Player(sc, username, password);
        SocketClient sc2 = buildSocketClient();
        Player playerToInvite = new Player(sc2, username, password);


        player1.addInUsersYouInvited(playerToInvite);


        player1.removeFromUsersYouInvited(playerToInvite);


        ArrayList<Player> result = player1.getUsersYouInvited();


        assertEquals(0, result.size());
    }

    @Test
    public void testRemoveFromUserWhoInvitedYou() {
        SocketClient sc = buildSocketClient();
        Player player1 = new Player(sc, username, password);
        SocketClient sc2 = buildSocketClient();
        Player playerWhoInviteYou = new Player(sc2, username, password);


        player1.addInUsersWhoInvitedYou(playerWhoInviteYou);


        player1.removeFromUserWhoInvitedYou(playerWhoInviteYou);


        ArrayList<Player> result = player1.getUsersWhoInvitedYou();


        assertEquals(0, result.size());
    }

    @Test
    public void testCompareTo() {
        Player player1 = buildPlayer(username, password);
        player1.setUsername("user1");

        Player player2 = buildPlayer(username, password);
        player2.setUsername("user1");

        Player player3 = buildPlayer(username, password);
        player3.setUsername("user2");

        // Invoke the compareTo() method
        boolean result1 = player1.compareTo(player2);
        boolean result2 = player1.compareTo(player3);

        // Assert the results
        assertEquals(true, result1);
        assertEquals(false, result2);
    }


}


