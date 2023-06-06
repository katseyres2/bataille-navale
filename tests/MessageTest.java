package tests;

import Bots.Bot;
import org.junit.jupiter.api.Test;
import socket.Message;
import socket.client.SocketClient;
import socket.server.Player;
import socket.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MessageTest {

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
    @Test
    public void testGetTime() {
        Player p = new Bot("LOCO",Bot.Difficulty.HARD);
        Message message = new Message("ceci est un test", p);
        assertNotNull(message.getTime());
    }

    @Test
    public void testGetText() {
        Player p = new Bot("LOCO",Bot.Difficulty.HARD);
        Message message = new Message("ceci est un test", p);
        String test = "ceci est un test";
        assertEquals(test, message.getText());
    }
}


