package Bots;

import game.Action;
import game.grid.Grid;

import java.util.Objects;
import java.util.Random;

public class Facile{

    private static final Random random = new Random();

    public static void startTurn(Grid grid){


        int[] randomPoint = grid.getRandomPoint();


        if(!Objects.equals(grid.getGrid()[randomPoint[0]][randomPoint[1]], ".")){
            startTurn(grid);
        }

        return new Action();

    }

}
