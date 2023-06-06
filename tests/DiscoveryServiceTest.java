package tests;

import Bots.Bot;
import game.Game;
import game.boat.Boat;
import game.grid.Cell;
import game.grid.Grid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import socket.client.SocketClient;
import socket.server.Player;
import services.DiscoveryService;

import java.net.Socket;
import java.util.ArrayList;

public class DiscoveryServiceTest {

    @Test
    public void testFindBy() {
        Player player1 = new Bot("hitler", Bot.Difficulty.HARD);
        Player player2 = new Bot("jane", Bot.Difficulty.HARD);
        Player player3 = new Bot("oscour", Bot.Difficulty.HARD);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        ArrayList<Player> foundPlayers = DiscoveryService.findBy(player1, players);
        Assertions.assertEquals(1, foundPlayers.size());
        Assertions.assertTrue(foundPlayers.contains(player1));
        Assertions.assertFalse(foundPlayers.contains(player3));
    }

    @Test
    public void testFindOneBy() {
        Player player1 = new Bot("hitler", Bot.Difficulty.HARD);
        Player player2 = new Bot("jane", Bot.Difficulty.HARD);
        Player player3 = new Bot("oscour", Bot.Difficulty.HARD);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        Player foundPlayer = DiscoveryService.findOneBy(player2, players);
        Assertions.assertEquals(player2, foundPlayer);

        foundPlayer = DiscoveryService.findOneBy(player3, players);
        Assertions.assertEquals(player3, foundPlayer);

        foundPlayer = DiscoveryService.findOneBy(player1, players);
        Assertions.assertEquals(player1, foundPlayer);

        foundPlayer = DiscoveryService.findOneBy(new Bot("zdzd", Bot.Difficulty.HARD), players);
        Assertions.assertNull(foundPlayer);
    }

    @Test
    public void testFindPlayerInGameByUsername() {
        Player player1 = new Bot("hitler", Bot.Difficulty.HARD);
        Player player2 = new Bot("jane", Bot.Difficulty.HARD);
        Player player3 = new Bot("oscour", Bot.Difficulty.HARD);



        Game game = new Game();

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        Player foundPlayer = DiscoveryService.findPlayerInGameByUsername("hitler", game);
        Assertions.assertEquals(player1, foundPlayer);

        foundPlayer = DiscoveryService.findPlayerInGameByUsername("jane", game);
        Assertions.assertEquals(player2, foundPlayer);

        foundPlayer = DiscoveryService.findPlayerInGameByUsername("oscour", game);
        Assertions.assertEquals(player3, foundPlayer);

        foundPlayer = DiscoveryService.findPlayerInGameByUsername("Alex", game);
        Assertions.assertNull(foundPlayer);
    }


    @Test
    public void testFindOneBySocketClient() {
        SocketClient client1 = new SocketClient(null,null,null);
        SocketClient client2 = new SocketClient(null,null,null);
        SocketClient client3 = new SocketClient(null,null,null);

        Player player1 = new Bot("hitler", Bot.Difficulty.HARD);
        Player player2 = new Bot("jane", Bot.Difficulty.HARD);
        Player player3 = new Bot("oscour", Bot.Difficulty.HARD);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        Player foundPlayer = DiscoveryService.findOneBy(client2, players);
        Assertions.assertEquals(player2, foundPlayer);

        foundPlayer = DiscoveryService.findOneBy(client3, players);
        Assertions.assertEquals(player3, foundPlayer);

        foundPlayer = DiscoveryService.findOneBy(client1, players);
        Assertions.assertEquals(player1, foundPlayer);

        SocketClient client4 = new SocketClient(null,null,null);
        foundPlayer = DiscoveryService.findOneBy(client4, players);
        Assertions.assertNull(foundPlayer);
    }

    @Test
    public void testFindCell() {
        Cell cell1 = new Cell(1, 1);
        Cell cell2 = new Cell(2, 2);
        Cell cell3 = new Cell(3, 3);

        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(cell1);
        cells.add(cell2);
        cells.add(cell3);

        Cell foundCell = DiscoveryService.findCell(2, 2, cells);
        Assertions.assertEquals(cell2, foundCell);

        foundCell = DiscoveryService.findCell(3, 3, cells);
        Assertions.assertEquals(cell3, foundCell);

        foundCell = DiscoveryService.findCell(1, 1, cells);
        Assertions.assertEquals(cell1, foundCell);

        foundCell = DiscoveryService.findCell(4, 4, cells);
        Assertions.assertNull(foundCell);
    }

    @Test
    public void testFindCellInGrid() {
        Grid grid = new Grid(new Bot("oscour", Bot.Difficulty.HARD), 10, 10);
        Cell cell1 = new Cell(1, 1);
        Cell cell2 = new Cell(2, 2);
        Cell cell3 = new Cell(3, 3);

        Cell foundCell = DiscoveryService.findCellInGrid(2, 2, grid);
        Assertions.assertEquals(cell2, foundCell);

        foundCell = DiscoveryService.findCellInGrid(3, 3, grid);
        Assertions.assertEquals(cell3, foundCell);

        foundCell = DiscoveryService.findCellInGrid(1, 1, grid);
        Assertions.assertEquals(cell1, foundCell);

        foundCell = DiscoveryService.findCellInGrid(4, 4, grid);
        Assertions.assertNull(foundCell);
    }

    @Test
    public void testFindCellInBoats() throws InstantiationException {
        Cell cell1 = new Cell(1, 1);
        Cell cell2 = new Cell(2, 2);
        Cell cell3 = new Cell(3, 3);

        ArrayList<Boat> boats = new ArrayList<>();
        Boat boat1 = new Boat(Boat.Model.CRUISER, new ArrayList<>());
        boat1.getCoordinates().add(cell1);
        Boat boat2 = new Boat(Boat.Model.CRUISER, new ArrayList<>());
        boat2.getCoordinates().add(cell2);
        Boat boat3 = new Boat(Boat.Model.CRUISER, new ArrayList<>());
        boat3.getCoordinates().add(cell3);

        boats.add(boat1);
        boats.add(boat2);
        boats.add(boat3);

        Cell foundCell = DiscoveryService.findCellInBoats(2, 2, boats);
        Assertions.assertEquals(cell2, foundCell);

        foundCell = DiscoveryService.findCellInBoats(3, 3, boats);
        Assertions.assertEquals(cell3, foundCell);

        foundCell = DiscoveryService.findCellInBoats(1, 1, boats);
        Assertions.assertEquals(cell1, foundCell);

        foundCell = DiscoveryService.findCellInBoats(4, 4, boats);
        Assertions.assertNull(foundCell);
    }

    @Test
    public void testFindBoatWhichHasCell() throws InstantiationException {
        Cell cell1 = new Cell(1, 1);
        Cell cell2 = new Cell(2, 2);
        Cell cell3 = new Cell(3, 3);

        Boat boat1 = new Boat(Boat.Model.CRUISER, new ArrayList<>());
        boat1.getCoordinates().add(cell1);
        Boat boat2 = new Boat(Boat.Model.CRUISER, new ArrayList<>());
        boat2.getCoordinates().add(cell2);
        Boat boat3 = new Boat(Boat.Model.CRUISER, new ArrayList<>());
        boat3.getCoordinates().add(cell3);

        ArrayList<Boat> boats = new ArrayList<>();
        boats.add(boat1);
        boats.add(boat2);
        boats.add(boat3);

        Boat foundBoat = DiscoveryService.findBoatWhichHasCell(cell2, boats);
        Assertions.assertEquals(boat2, foundBoat);

        foundBoat = DiscoveryService.findBoatWhichHasCell(cell3, boats);
        Assertions.assertEquals(boat3, foundBoat);

        foundBoat = DiscoveryService.findBoatWhichHasCell(cell1, boats);
        Assertions.assertEquals(boat1, foundBoat);

        Cell cell4 = new Cell(4, 4);
        foundBoat = DiscoveryService.findBoatWhichHasCell(cell4, boats);
        Assertions.assertNull(foundBoat);
    }

    @Test
    public void testFindModelByLength() {
        Boat.Model model1 = DiscoveryService.findModelByLength(5);
        Assertions.assertEquals(Boat.Model.AIRCRAFT_CARRIER, model1);

        Boat.Model model3 = DiscoveryService.findModelByLength(4);
        Assertions.assertEquals(Boat.Model.CRUISER, model3);

        Boat.Model model4 = DiscoveryService.findModelByLength(3);
        Assertions.assertEquals(Boat.Model.SUBMARINE, model4);

        Boat.Model model6 = DiscoveryService.findModelByLength(10);
        Assertions.assertNull(model6);
    }

    @Test
    public void testFindGrid() {
        Player player1 = new Bot("hitler", Bot.Difficulty.HARD);
        Player player2 = new Bot("jane", Bot.Difficulty.HARD);
        Player player3 = new Bot("oscour", Bot.Difficulty.HARD);


        Grid grid1 = new Grid(player1, 10, 10);
        Grid grid2 = new Grid(player2, 10, 10);
        Grid grid3 = new Grid(player3, 10, 10);

        ArrayList<Grid> grids = new ArrayList<>();
        grids.add(grid1);
        grids.add(grid2);
        grids.add(grid3);

        Grid foundGrid = DiscoveryService.findGrid(player2, grids);
        Assertions.assertEquals(grid2, foundGrid);

        foundGrid = DiscoveryService.findGrid(player3, grids);
        Assertions.assertEquals(grid3, foundGrid);

        foundGrid = DiscoveryService.findGrid(player1, grids);
        Assertions.assertEquals(grid1, foundGrid);

        Player player4 = new Bot("oscour", Bot.Difficulty.HARD);
        foundGrid = DiscoveryService.findGrid(player4, grids);
        Assertions.assertNull(foundGrid);
    }
}
