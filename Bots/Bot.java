/**
 * La classe Bots représente un bot jouant à un jeu de grille donné. Elle contient des méthodes pour vérifier et envoyer la grille du jeu, ainsi que pour sélectionner la difficulté du bot.
 */
package Bots;

import game.Action;
import game.Game;
import game.grid.Grid;
import org.jetbrains.annotations.NotNull;
import socket.server.Player;
import socket.server.Server;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Bot extends Player {
    public enum Difficulty {
        Easy,
        Medium,
        Hard
    };

    private Action action;
    private Difficulty difficulty;

    private static final Random random = new Random();

    /**
     * Constructeur de la classe Bots
     *
     * @param difficulty
     */
    public Bot(String username, Difficulty difficulty) {
        super(null,null,null,username,null, true);
        this.difficulty = difficulty;
    }

    public Action getAction(){
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void returnAction() {

    }

    public void run() {
        Game game = Server.getActiveGame(this);
        ArrayList<Player> players = game.getPlayers();
        Grid randomGrid = game.findGridByPlayer(players.get((new Random()).nextInt(players.size() - 1)));

        switch (getDifficulty()) {
            case Easy:
                //TODO: implémenter la difficulté 1
//                Facile.startTurn(randomGrid);
                break;
            case Medium:
                //TODO: implémenter la difficulté 2

                break;
            case Hard:
                //TODO: implémenter la difficulté 3
                break;

            default:
                System.out.println("Difficulty are not defined");
                break;
        }
    }

}