package student.adventure;

import com.google.gson.Gson;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class gameplay {
    private GameMap gameMap;
    public PalletTown location;
    public int locationIndex;
    public String pokemon;
    public List<String> playerItem = new ArrayList<>();
    public String garyPokemon;

    /**
     * Default constructor
     */
    public gameplay() {
        try {
            Gson gson = new Gson();
            Reader jsonReader = Files.newBufferedReader(Paths.get("C:\\Users\\alvin\\IdeaProjects\\amazing-adventures-alvinoa9\\src\\Resource\\pokemonRed.json"));
            gameMap = gson.fromJson(jsonReader, GameMap.class);
            location = gameMap.getPalletTown(0);
            locationIndex = 0;
            pokemon = "None";
            garyPokemon = "";
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    /**
     * Method that runs the whole program using while loop to keep asking for user input until goal is reached
     */
    public void run() {
        System.out.println("Lets get started!");
        examine();
        while (!gameMap.getPalletTown(6).isVisited()) {          // While the area in index 5 is not visited
            Scanner scan = new Scanner(System.in);            // Initialize scanner for user input
            System.out.print("> ");
            String command = scan.nextLine();
            command = command.trim().toLowerCase();           // Makes input lower case and trims it
            play(command);                                    // Run play method
        }
        System.out.println("You have won against Gary!");
    }

    /**
     * Takes in the input and determinds the move
     * @param command input of user
     */
    public void play(String command) {
        if (command.contains("quit") || command.contains("exit")) {     // Exits program
            System.exit(0);
        }

        else if (command.contains("examine")) {                         // Runs examine()
            examine();
        }

        else if (command.contains("inventory")) {                       // Runs printInventory
            printInventory();
        }

        else if (command.contains("use")) {                             // Checks input for keyword "use"
            if (command.contains("rare candy")) {
                useRareCandy();
            }
            else if (command.contains("town map")) {
                useTownMap();
            }
            else {
                System.out.println("You don't have \"" + command + "\"!");
            }
        }

        else if (command.contains("drop")) {                            // Checks input for keyword "drop"
            String item = command.substring(4).trim();         // Gets the item name by removing "drop" from input
            if (playerItem.contains(item)) {
                dropItem(item);
            }
            else {
                System.out.println("You don't have \"" + item + "\"!");
            }
        }

        else if (command.contains("take")) {                            // Checks input for keyword "take"
            takeItem(command);
        }

        else if (command.contains("talk")) {                            // Checks input for keyword "talk"
            choosePokemon();
        }

        else if (command.contains("fight")) {                           // Checks input for keyword "fight"
            fight();
        }

        else if (command.contains("go")) {                              // Checks input for keyword "go"
            String direction = command.substring(2).trim();             // Cuts "go" from input and trims
            if (location.getPath().toLowerCase().contains(direction)) { // Checks if input is a possible move
                move(direction);
            }
            else {
                System.out.println("I can't go \"" + command + "\"");
            }
        }

        else {                                                          // Prints if input is invalid
            System.out.println("I don't understand \"" + command + "\"");
        }
    }

    /**
     * Prints out the room's variables
     */
    public void examine() {
        System.out.println("You are on " + location.getName() + ", " + location.getDescription());
        System.out.println("from here, you can go: " + location.getPath());

        if (!location.getItems().isEmpty()) {
            System.out.println("Items visible: " + location.getItems());
        }

        System.out.println("Pokemon: " + pokemon);
    }

    /**
     * Updates location when pokemon fainted
     */
    private void fainted() {
        System.out.println("Your pokemon has fainted, you are now back at your house.");
        System.out.println("Get your pokemon stronger.");
        this.location = gameMap.getPalletTown(0);               // Move back to player's house
    }

    /**
     * Makes user choose pokemon
     */
    private void choosePokemon() {
        if (!gameMap.getPalletTown(2).isVisited()) {            // Checks if player already receives pokemon
            System.out.println("Professor Oak: Welcome player!");
            System.out.println("Professor Oak: Choose a pokemon to get your journey started.");
            System.out.println("Professor Oak: Your choices are: Charmander   Squirtle   Bulbasaur");

            Scanner pickPokemon = new Scanner(System.in);             // Scanner for player's choice
            System.out.print("> ");
            String pick = pickPokemon.next().toLowerCase();

            switch (pick) {                                           // Updates User's and Rival's pokemon
                case "charmander":
                    this.pokemon = "Charmander";
                    garyPokemon = "Blastoise";
                    break;
                case "squirtle":
                    this.pokemon = "Squirtle";
                    garyPokemon = "Venusaur";
                    break;
                case "bulbasaur":
                    this.pokemon = "Bulbasaur";
                    garyPokemon = "Charizard";
                    break;
            }

            gameMap.getPalletTown(2).haveVisited();             // Updates visited variable
            System.out.println("Defeat your rival, Gary, in Plains 3.");
        }
        else {
            System.out.println("Professor Oak is not here at the moment.");
        }
    }

    /**
     * Determines user movement based on input
     * Updates user location and index on the map
     * @param command user input
     */
    public void move(String command) {
        if (command.equalsIgnoreCase("back to town")) {
            location = gameMap.getPalletTown(3);
            locationIndex = 3;
        }
        if (command.equalsIgnoreCase("run")) {
            location = gameMap.getPalletTown(locationIndex - 1);
            locationIndex -= 1;
        }
        if (command.equalsIgnoreCase("left")) {
            location = gameMap.getPalletTown(0);
            locationIndex = 0;
        }
        if (command.equalsIgnoreCase("right")) {
            location = gameMap.getPalletTown(2);
            locationIndex = 2;
        }
        if (command.equalsIgnoreCase("down")) {
            location = gameMap.getPalletTown(1);
            locationIndex = 1;
        }
        if (command.equalsIgnoreCase("up")) {
            if (pokemon.equalsIgnoreCase("None")) {  // Prohibits user on going to plains 1 without pokemon
                System.out.println("You need a pokemon for this area. Go to Professor Oak's lab.");
            }
            else {
                location = gameMap.getPalletTown(locationIndex + 1);
                locationIndex += 1;
            }
        }
        examine();
    }

    /**
     * Simulate fights on plains 1,2,3 and updates path of location
     */
    private void fight() {
        location.changePath();

        if (location.getName().equalsIgnoreCase("Plains 1")) {
            System.out.println("A wild pidgey has appeared!");
            System.out.println("You choose " + pokemon);
            System.out.println(pokemon + " use tackle.");
            System.out.println("Pidgey has fainted.");
            System.out.println("You won!");
            System.out.println(pokemon + " gained 100 xp");
            if (pokemon.equals("Charmander") || pokemon.equals("Squirtle") || pokemon.equals("Bulbasaur")){
                evolve();
            }
            location.haveVisited();
        }

        if (location.getName().equalsIgnoreCase("Plains 2")) {
            System.out.println("A wild caterpie has appeared!");
            System.out.println("You choose " + pokemon);
            System.out.println(pokemon + " use tackle.");
            System.out.println("Caterpie has fainted.");
            System.out.println("You won!");
            System.out.println(pokemon + " gained 100 xp");
            location.haveVisited();
        }

        if (location.getName().equalsIgnoreCase("Plains 3")) {
            if (pokemon.equals("Charizard") || pokemon.equals("Blastoise") || pokemon.equals("Venusaur")) {
                System.out.println("Gary has appeared!");
                System.out.println("Gary choose " + garyPokemon);
                System.out.println("You choose " + pokemon);
                System.out.println(pokemon + " use tackle.");
                System.out.println(garyPokemon + " has fainted.");
                System.out.println("You won!");
                System.out.println(pokemon + " gained 100 xp");
                location.haveVisited();
            }
            else {
                System.out.println("Gary's pokemon is too strong!");
                fainted();
            }
        }
    }

    /**
     * Prints current location and town map when user inputs to use town map
     */
    private void useTownMap() {
        System.out.println("Town Map:");
        System.out.println("                 Plains 3");
        System.out.println("                    ||");
        System.out.println("                 Plains 2");
        System.out.println("                    ||");
        System.out.println("                 Plains 1");
        System.out.println("                    ||");
        System.out.println("Player's Home - Town Square - Professor Oak's Lab");
        System.out.println("                    ||");
        System.out.println("               Rival's House\n");
        System.out.println("You are at:" + location.getName());
    }

    /**
     * Updates pokemon's status
     */
    private void useRareCandy() {
        if (pokemon.equals("None")) {       //Checks if user has pokemon
            System.out.println("You don't have a pokemon to use this candy on.");
        }
        else {
            System.out.println("Rare candy is used on " + pokemon);
            evolve();
        }
    }

    /**
     * Removes item from the room and adds it to playerItem
     * If the input data is not in the room, prints out no item
     * @param itemName string of user input
     */
    public void takeItem(String itemName) {
        String item = itemName.substring(4).trim();         // Gets the item name by removing "take" from input
        if (location.getItems().contains(item)) {           // Checks if the item input is in the room
            playerItem.add(item);
            location.grabItem(item);
        }
        else {
            System.out.println("There is no item \"" + item + "\" in the room");
        }
    }

    /**
     * Removes item from user and adds it to the room
     * @param itemName user input
     */
    public void dropItem(String itemName) {
        if (!playerItem.contains(itemName)) {                   // Checks if the player does not have the item
            System.out.println("You don't have \"" + itemName + "\"");
        }
        else if (location.getItems().contains(itemName)) {      // Checks if the item is already in the room
            System.out.println("The item \"" + itemName + "\" is already in this room!");
        }
        else {
            playerItem.remove(itemName);
            location.putItem(itemName);
        }
    }

    /**
     * Method that prints inventory
     */
    private void printInventory() {
        if (playerItem.size() == 0) {       // Checks if player has inventory
            System.out.println("You don't have any items in your inventory.");
        }
        else {
            System.out.println(playerItem);
        }
    }

    /**
     * Method to evolve pokemon
     */
    public void evolve() {
        System.out.println("What? " + pokemon + " is evolving!");
        if (pokemon.equalsIgnoreCase("charmander")) {
            System.out.println(pokemon + " has evolved to Charmeleon!");
            pokemon = "Charmeleon";
        }
        else if (pokemon.equalsIgnoreCase("squirtle")) {
            System.out.println(pokemon + " has evolved to Wartortle!");
            pokemon = "Wartortle";
        }
        else if (pokemon.equalsIgnoreCase("bulbasaur")) {
            System.out.println(pokemon + " has evolved to Ivysaur!");
            pokemon = "Ivysaur";
        }
        else if (pokemon.equalsIgnoreCase("charmeleon")) {
            System.out.println(pokemon + " has evolved to Charizard!");
            pokemon = "Charizard";
        }
        else if (pokemon.equalsIgnoreCase("wartortle")) {
            System.out.println(pokemon + " has evolved to Blastoise!");
            pokemon = "Blastoise";
        }
        else if (pokemon.equalsIgnoreCase("ivysaur")) {
            System.out.println(pokemon + " has evolved to Venusaur!");
            pokemon = "Venusaur";
        }
    }
}
