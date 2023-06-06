package tests;

import game.grid.Grid;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import socket.client.SocketClient;
import socket.server.Player;
import socket.server.Server;


class GridTest {
    @Test
   public void getRows() {
        Grid grid = new Grid(new Player(), rows, 0);
        int rows = 5;
        int columns = 4;
        int actual = grid.getRows();
        Assert.assertEquals(rows,5);
    }

    @Test
   public void getColumns() {
        Grid grid = new Grid(new Player(), rows, 0);
        int rows = 5;
        int columns = 4;
        grid.setColumns(columns);
        int actual = grid.getColumns();
        Assert.assertEquals(columns, 4);
    }

    @Test
  public void getPlayer() {
        Player player = new Player();
        Grid grid = new Grid(player, 5, 4);
        Player actual = grid.getPlayer();
        Assert.assertEquals(player, actual);
    }
}