package tests;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;
import socket.Message;
import socket.server.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MessageTest {
    @Test
    public void testGetTime() {
        Socket s = new Socket();
        PrintWriter pw = new PrintWriter(System.out);
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        Player p = new Player(s, pw, bf, "test", "test");
        Message message = new Message("ceci est un test", p);
        assertNotNull(message.getTime());
    }

    @Test
    public void testGetText() {
        Socket s = new Socket();
        PrintWriter pw = new PrintWriter(System.out);
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        Player p = new Player(s, pw, bf, "test", "test");
        Message message = new Message("ceci est un test", p);
        String test = "ceci est un test";
        assertEquals(test, message.getText());
    }
}


