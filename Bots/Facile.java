package Bots;

import game.Action;
import game.grid.Grid_old;

import java.util.Objects;
import java.util.Random;

public class Facile {

    private static final Random random = new Random();

    public static void startTurn(Grid_old gridOld){


        int[] randomPoint = gridOld.getRandomPoint();


        if(!Objects.equals(gridOld.getGrid()[randomPoint[0]][randomPoint[1]], ".")){
            startTurn(gridOld);
        }

//        return new Action();

    }

}
