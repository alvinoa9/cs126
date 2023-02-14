package student.adventure;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import student.server.Command;
import student.server.GameStatus;
import student.server.SomeAdventureService;
import student.server.AdventureException;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class AdventureTest {
    GameMap gameMap;
    @Before
    public void setUp() {
        try {
            Gson gson = new Gson();
            Reader jsonReader = Files.newBufferedReader(Paths.get("C:\\Users\\alvin\\IdeaProjects\\api-adventures-alvinoa9\\src\\main\\resources\\pokemonRed.json"));
            gameMap = gson.fromJson(jsonReader, GameMap.class);
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    @Test
    public void resetTest() {
        SomeAdventureService test = new SomeAdventureService();
        test.reset();
        Assert.assertNotNull(test.instanceMap);
    }

    @Test
    public void newGameTest() throws AdventureException {
        SomeAdventureService test = new SomeAdventureService();
        test.newGame();
        Assert.assertNotNull(test.instanceMap);
    }

    @Test
    public void getGameTest() throws AdventureException {
        SomeAdventureService test = new SomeAdventureService();
        test.newGame();
        GameStatus status = test.getGame(0);
        Assert.assertNotNull(status);
    }

    @Test
    public void destroyGameTest() throws AdventureException {
        SomeAdventureService test = new SomeAdventureService();
        test.newGame();
        test.destroyGame(test.id);
        Assert.assertNull(test.instanceMap.get(test.id));
    }

    @Test
    public void executeCommandTest() throws AdventureException {
        SomeAdventureService test = new SomeAdventureService();
        test.newGame();
        Command testCommand = new Command("take", "rare candy");
        test.executeCommand(test.id, testCommand);
        Assert.assertEquals(1, test.instanceMap.get(test.id).playerItem);
    }

    @Test
    public void goTest() {
        GameEngine game = new GameEngine();
        Command command = new Command("go", "back to town");
        game.run(command);
        Assert.assertEquals(4, game.locationIndex);
    }

    @Test
    public void roomBoundsTest() {
        GameEngine game = new GameEngine();
        Command command = new Command("go", "left");
        game.run(command);
        Assert.assertEquals(0, game.locationIndex);
    }

    @Test
    public void testDropItem() {
        GameEngine game = new GameEngine();
        Command command = new Command("take", "rare candy");
        game.run(command);
        Command command1 = new Command("go", "back to town");
        game.run(command);
        Command command2 = new Command("drop", "rare candy");
        game.run(command);
        List<String> expected = new ArrayList<>();
        expected.add("rare candy");
        Assert.assertEquals(expected, game.location.getItems());
    }

    @Test
    public void testDropImaginaryItem() {
        GameEngine game = new GameEngine();
        Command command = new Command("drop", "pokeball");
        game.run(command);
        List<String> expected = new ArrayList<>();
        expected.add("rare candy");
        Assert.assertEquals(expected, game.location.getItems());
    }

    @Test
    public void testTakeItem() {
        GameEngine game = new GameEngine();
        Command command = new Command("take", "rare candy");
        game.run(command);
        Assert.assertEquals(0, game.location.getItems().size());
    }

    @Test
    public void testTakeImaginaryItem() {
        GameEngine game = new GameEngine();
        Command command = new Command("take", "pokeball");
        game.run(command);
        List<String> expected = new ArrayList<>();
        expected.add("rare candy");
        Assert.assertEquals(expected, game.location.getItems());
    }

    @Test
    public void testDropDuplicateItem() {
        GameEngine game = new GameEngine();
        game.playerItem.add("rare candy");
        Command command = new Command("drop", "rare candy");
        game.run(command);
        List<String> expected = new ArrayList<>();
        expected.add("rare candy");
        Assert.assertEquals(expected, game.location.getItems());
    }

    @Test
    public void testEvolve() {
        GameEngine game = new GameEngine();
        Command command = new Command("choose", "Charmander");
        game.run(command);
        game.evolve();
        Assert.assertEquals("Charmeleon", game.pokemon);
    }
}