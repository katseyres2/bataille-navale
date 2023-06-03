package tests;

import game.Match;
import org.junit.jupiter.api.Test;
import socket.client.SocketClient;
import socket.server.Player;

import static org.junit.Assert.assertEquals;

public class MatchTest {

    @Test
    public void testStartGame() {
        // creation d'une instance de la classe qui contient la methode startGame
        SocketClient sc = new SocketClient(null, null, null);
        Player player1 = new Player(sc, "John","lud" );
        Player player2 = new Player(sc, "doe", "max");
        Match match = new Match(player1, player2);
        // j'appele la methode
        match.startGame();
        assertEquals(player1, match.player1);
    }
}