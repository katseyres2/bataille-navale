package tests;

import Bots.Bot;
import game.grid.Grid;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import socket.server.Player;


class GridTest {
    @Test
   public void getRows() {
        int rows = 5;
        int columns = 4;
        Grid grid = new Grid(new Bot("LAZARE", Bot.Difficulty.MEDIUM), rows, columns);
        int actual = grid.getRows();
        Assert.assertEquals(grid.getRows(),5);
    }

    @Test
   public void getColumns() {
        int rows = 5;
        int columns = 4;
        Grid grid = new Grid(new Bot("LAZARE", Bot.Difficulty.MEDIUM), rows, columns);
        Assert.assertEquals(grid.getColumns(), 4);
    }

    @Test
  public void getPlayer() {
        Player player = new Bot("LAZARE", Bot.Difficulty.MEDIUM);
        Grid grid = new Grid(player, 5, 4);
        Player actual = grid.getPlayer();
        Assert.assertEquals(player, actual);
    }
}