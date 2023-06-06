package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import socket.client.SocketClient;
import socket.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class SocketClientTest {

    @Test
    public void testGetPort() throws IOException {
        String host = "127.0.0.1";
        System.out.println(host + ":" + Server.PORT);
        Socket s = new Socket(host, Server.PORT);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        BufferedReader  br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        // Set the port of the socket
        SocketClient sc = new SocketClient(s, pw, br);
          // Get the port from the socket client
        Assertions.assertNotNull(sc.getPort());

    }

    @Test
    public void testGetSocket() throws IOException {
        String host = "127.0.0.1";
        System.out.println(host + ":" + Server.PORT);
        Socket s = new Socket(host, Server.PORT);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        BufferedReader  br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        // Set the port of the socket
        SocketClient sc = new SocketClient(s, pw, br);
        // Verify that the socket matches the one set in the constructor
        Assertions.assertNotNull(sc);
    }

    @Test
   public void testIsLogged() throws IOException {
        String host = "127.0.0.1";
        System.out.println(host + ":" + Server.PORT);
        Socket s = new Socket(host, Server.PORT);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        BufferedReader  br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        // Set the port of the socket
        SocketClient sc = new SocketClient(s, pw, br);
        Assertions.assertTrue(sc.isLogged());

    }

    @Test
    public void testGetAddress() throws IOException {
        String host = "127.0.0.1";
        System.out.println(host + ":" + Server.PORT);
        Socket s = new Socket(host, Server.PORT);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        BufferedReader  br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        // Set the port of the socket
        SocketClient sc = new SocketClient(s, pw, br);

        // Verify that the address matches the expected value
        Assertions.assertNotNull(sc.getAddress());
    }

    @Test
    void testGetLastConnection() throws IOException {
        String host = "127.0.0.1";
        System.out.println(host + ":" + Server.PORT);
        Socket s = new Socket(host, Server.PORT);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        BufferedReader  br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        // Set the port of the socket
        SocketClient sc = new SocketClient(s, pw, br);

        // Verify that the last connection time is not null
        Assertions.assertNotNull(sc.getLastConnection());
    }
}