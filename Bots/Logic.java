package Bots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Logic extends Bots{

    //récupération et création de l'objet sur une base default
    private final Integer[] coordonnees;
    private List<int[]> alreadyTarget;

    //pritate méthode pour générer des nombres aléatoires
    private Random random;

    public Logic(ArrayList<ArrayList<String>> grid, Integer[] coordonnees, List<int[]> alreadyTarget) {
        super(grid);
        this.coordonnees = coordonnees;
        this.alreadyTarget = alreadyTarget;
    }

    public Boolean startTurn(ArrayList<ArrayList<String>> playerGrid){

        /*
        ETAPES D'UN TOUR :
            Les fonctions ex : sendGrid, sont dans la classe objet bot
            1 on vérifie s'il n'a pas déjà trouvé un bâteau / s'il a trouvé un bâteau on voit si la direction est déjà trouvé
            2 si pas de bâteau, on doit en trouvé un, si le bot n'a pas pu toucher un bâteau, on retourne la grille
            3
         */

        if(coordonnees[0] == null && coordonnees[1] == null){

            //si c'est faux, il n'a pas touché de bâteau donc on ne fait rien
            //si c'est vrai, on indique la position du bateau.
            int x = random.nextInt(getGrid().size());
            int y = random.nextInt(getGrid().get(x).size());
            int[] targetPoint = checkSearchShip(x, y);

            if(targetPoint != null){
                coordonnees[0] = targetPoint[0];
                coordonnees[1] = targetPoint[1];
                //sendGrid();
                return false;
            }
        }else{
            //maintenant, on vérifie si la direction n'est pas trouvée
            // un point de façon aléatoire, mais qui doit se trouver à côté du point trouvé
            String direction = startSearchDirection(random.nextInt(4));
        }
        return true;
    }

    private String startSearchDirection(int direction){

        int[] direction_form_interger = null;

        switch (direction){
            case 0:
                direction_form_interger = new int[]{-1, 0};
            case 2:
                direction_form_interger = new int[]{1, 0};
            case 3:
                direction_form_interger = new int[]{0, -1};
            case 1:
                direction_form_interger = new int[]{0, 1};
        }

        checkTargetWithDirection(direction_form_interger);

        return null;
    }

    private Boolean checkTargetWithDirection(int[] newTarget){

        if(checkSearchShip(newTarget[0], newTarget[1]) != null){

        }

        return false;
    }

    private int[] checkSearchShip(int x, int y){

        int[] output = new int[2];

        output[0] = x;
        output[1] = y;

        //vérifier si le point trouvé n'est pas déjà dans la liste des targetpoints sinon recommencer
        if(alreadyTarget.contains(output)){
            //checkSearchShip(random.nextInt(getGrid().size()), random.nextInt(getGrid().get(x).size()));
        }

        alreadyTarget.add(output);

        if(getGrid().get(x).get(y).equals(" ")){
            return null;
        }
        return output;
    }

}

