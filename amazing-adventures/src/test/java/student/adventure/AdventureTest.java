package student.adventure;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class AdventureTest {
    GameMap gameMap;
    @Before
    public void setUp() {
        try {
            Gson gson = new Gson();
            Reader jsonReader = Files.newBufferedReader(Paths.get("C:\\Users\\alvin\\IdeaProjects\\amazing-adventures-alvinoa9\\src\\Resource\\pokemonRed.json"));
            gameMap = gson.fromJson(jsonReader, GameMap.class);
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    @Test
    public void testGetTown() {
        String name = gameMap.getPalletTown(0).getName();
        Assert.assertEquals("Player's House", name);
    }

    @Test
    public void testEvolve() {
        gameplay testPokemon = new gameplay();
        testPokemon.pokemon = "Charmander";
        testPokemon.evolve();
        Assert.assertEquals("Charmeleon", testPokemon.pokemon);
    }

    @Test
    public void testDropItem() {
        gameplay testPokemon = new gameplay();
        testPokemon.takeItem("take rare candy");
        testPokemon.move("back to town");
        testPokemon.dropItem("rare candy");
        List<String> expected = new ArrayList<>();
        expected.add("rare candy");
        Assert.assertEquals(expected, testPokemon.location.getItems());
    }

    @Test
    public void testDropImaginaryItem() {
        gameplay testPokemon = new gameplay();
        testPokemon.dropItem("pokeball");
        List<String> expected = new ArrayList<>();
        expected.add("rare candy");
        Assert.assertEquals(expected, testPokemon.location.getItems());
    }

    @Test
    public void testDuplicateItem() {
        gameplay testPokemon = new gameplay();
        testPokemon.playerItem.add("rare candy");
        testPokemon.dropItem("rare candy");
        Assert.assertEquals(1, testPokemon.location.getItems().size());
    }

    @Test
    public void testTakeItem() {
        gameplay testPokemon = new gameplay();
        testPokemon.takeItem("take rare candy");
        Assert.assertEquals(0, testPokemon.location.getItems().size());
    }

    @Test
    public void testTakeImaginaryItem() {
        gameplay testPokemon = new gameplay();
        testPokemon.takeItem("take pokeball");
        Assert.assertEquals(0, testPokemon.playerItem.size());
    }

    @Test
    public void testMoveBackToTown() {
        gameplay testPokemon = new gameplay();
        testPokemon.move("back to town");
        int index = testPokemon.locationIndex;
        Assert.assertEquals(3, index);
    }

    @Test
    public void testInvalidMove() {
        gameplay testPokemon = new gameplay();
        testPokemon.move("left");
        int index = testPokemon.locationIndex;
        Assert.assertEquals(0, index);
    }
}