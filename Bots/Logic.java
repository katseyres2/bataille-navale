package Bots;

import game.grid.Grid;

import java.util.ArrayList;

public class Logic extends Bots{

    private ArrayList<ArrayList<String>> grid;

    private Integer[] coordonnees;

    public Logic(ArrayList<ArrayList<String>> grid) {
        super(grid);
        this.grid = grid;
    }

    public Boolean startTurn(){

        /*
        ETAPES D'UN TOUR :
            1 on vérifie s'il n'a pas déjà trouvé un bâteau
         */

        if(coordonnees[0] == null && coordonnees[1] == null){
            //si c'est faux, il n'a pas touché de bâteau donc on ne fait rien
            //si c'est vrai, on indique la position du bateau.
            if(startSearchShip()){

            }
        }else{
            //maintenant on vérifie si la direction n'est pas trouvé
            // un point de façon aléatoire mais qui doit se trouvé à côté du point trouvé

        }



        return true;
    }

    private String startSearchDirection(){

        return null;
    }

    private Boolean startSearchShip(){



        return false;
    }




}
