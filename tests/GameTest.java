package tests;

import game.Game;
import game.grid.Grid;
import org.junit.jupiter.api.Test;
import socket.client.SocketClient;
import socket.server.Player;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class GameTest {

    @Test
    void removeGrid() {
    }

    @Test
   public void testGetPlayers() {
        //creation instance contenant la méthode getPlayer
        SocketClient sc = new SocketClient();
        Game game = new Game()
        game.addPlayer(null);
//ajouter des joueurs tests
        Player player1 = new Player(SocketClient "sc", "LUD","lock" );
        Player player2 = new Player(");
// ajout des grilles joueurs
        Grid grid1 = new Grid(player1);
        Grid grid2 = new Grid(player2);
        // ajout des grilles à la classe de jeu
        game.addGrid(grid1);
        game.addGrid(grid2);

        //recuperer les joueurs via la methode getPlayers()
        ArrayList<Player> expectedPlayers = new ArrayList<>(Arrays.asList(player1,player2));
        ArrayList<Player> actualPlayers = game.getPlayers();
        // Vérifiez si les joueurs renvoyés sont les mêmes que ceux ajoutés
        assertEquals(expectedPlayers, actualPlayers);


    }
}