/**
 * La classe Bots représente un bot jouant à un jeu de grille donné. Elle contient des méthodes pour vérifier et envoyer la grille du jeu, ainsi que pour sélectionner la difficulté du bot.
 */
package Bots;

import game.Action;
import game.grid.Grid;
import org.jetbrains.annotations.NotNull;


import java.util.Random;

public class Bot {

    private Grid grid;
    private Action action;
    private int difficulty;
    private String username;

    private static final Random random = new Random();

    /**
     * Constructeur de la classe Bots
     *
     * @param grid       la grille du jeu
     * @param action
     * @param difficulty
     */

    public Bot(Grid grid, Action action, int difficulty) {
        this.grid = grid;
        this.action = action;
        this.difficulty = difficulty;
    }

    public Grid getGrid(){
        return grid;
    }

    public Action getAction(){
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void returnAction(){

    }

    public void selectDifficulty(){

        switch (getDifficulty()){
            case 1:
                //TODO: implémenter la difficulté 1
                Facile.startTurn(getGrid());
                break;
            case 2:
                //TODO: implémenter la difficulté 2

                break;
            case 3:
                //TODO: implémenter la difficulté 3
                break;
            default:
                System.out.println("Difficulty are not defined");
                break;
        }
    }

}