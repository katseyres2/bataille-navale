/**
 * La classe Bots représente un bot jouant à un jeu de grille donné. Elle contient des méthodes pour vérifier et envoyer la grille du jeu, ainsi que pour sélectionner la difficulté du bot.
 */
package Bots;

import game.Action;
import game.grid.Grid_old;

public class BotsTurn {

    private Grid_old gridOld;
    private Action action;

    /**
     * Constructeur de la classe Bots
     * @param gridOld la grille du jeu
     */
    public BotsTurn(Grid_old gridOld) {
        this.gridOld = gridOld;
    }


    /**
     * Accesseur pour la grille du jeu
     * @return la grille du jeu
     */
    public Grid_old getGrid(){
        return gridOld;
    }

    public Action getAction(){
        return action;
    }

    /**
     * Envoie le résultat du tour du bot
     * @return la grille mise à jour après le tour du bot
     */
    public void returnAction(){
        //on vérifie que la grille respecte les conditions de validation

    }

    /**
     * Sélectionne la difficulté du bot en fonction d'un paramètre donné
     * @param difficulty la difficulté du bot, entre 1 et 3 inclus
     */
    public void selectDifficulty(String difficulty){

        switch (difficulty){
            case "1":
                //TODO: implémenter la difficulté 1
                Facile.startTurn(getGrid());
                break;
            case "2":
                //TODO: implémenter la difficulté 2
                break;
            case "3":
                //TODO: implémenter la difficulté 3
                break;
            default:
                System.out.println("Difficulty are not defined");
                break;
        }
    }

}