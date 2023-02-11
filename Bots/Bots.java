package Bots;

import game.grid.Grid;

import java.util.ArrayList;

public class Bots {

    private ArrayList<ArrayList<String>> grid;

    public Bots(ArrayList<ArrayList<String>> grid) {
        this.grid = grid;
    }

    public ArrayList<ArrayList<String>> getGrid(){
        return grid;
    }

    /*
    Vérification de la grille
     */

    private Boolean verifGrid(){
        return !grid.isEmpty();
    }


    /*
    Envoie le résulat du tour du bot
     */
    public ArrayList<ArrayList<String>> sendGrid(){
        //on vérifie que la grille respecte les conditions de validation
        if(!verifGrid()){

        }
        return grid;
    }
}
