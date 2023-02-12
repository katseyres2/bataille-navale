package Bots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Logic extends Bots{

    //récupération et création de l'objet sur une base default
    private ArrayList<ArrayList<String>> grid;
    private Integer[] coordonnees;
    private List<int[]> alreadyTarget;

    //pritate méthode pour générer des nombres aléatoires
    private Random random;

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

            int[] targetPoint = startSearchShip();



        }else{
            //maintenant on vérifie si la direction n'est pas trouvé
            // un point de façon aléatoire mais qui doit se trouvé à côté du point trouvé

        }

        return true;
    }

    private String startSearchDirection(){
        return null;
    }

    private int[] startSearchShip(){

        int x = random.nextInt(getGrid().size());
        int y = random.nextInt(getGrid().get(x).size());

        int[] output = new int[2];

        output[0] = x;
        output[1] = y;

        //vérifier si le point trouvé n'est pas déjà dans la liste des targetpoints sinon recommencer
        if(alreadyTarget.contains(output)){
            startSearchShip();
        }
        alreadyTarget.add(output);
        return output;
    }

}

