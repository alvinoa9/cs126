package student.adventure;

import com.google.gson.Gson;
import student.server.Command;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameEngine {
    private GameMap gameMap;
    public PalletTown location;
    public int locationIndex;
    public String pokemon;
    public List<String> playerItem = new ArrayList<>();
    public String garyPokemon;
    private String message = "";

    /**
     * Default constructor
     */
    public GameEngine() {
        try {
            Gson gson = new Gson();
            Reader jsonReader = Files.newBufferedReader(Paths.get("C:\\Users\\alvin\\IdeaProjects\\api-adventures-alvinoa9\\src\\main\\resources\\pokemonRed.json"));
            gameMap = gson.fromJson(jsonReader, GameMap.class);
            location = gameMap.getPalletTown(0);
            locationIndex = 0;
            pokemon = "None";
            garyPokemon = "";
        }
        catch (Exception e) {
            message = "Error: " + e;
        }

    }

    /**
     * Method that runs the whole program using while loop to keep asking for user input until goal is reached
     */
    public void run(Command command) {
        message = "";
        play(command);                                    // Run play method
    }

    /**
     * Takes in the input and determinds the move
     * @param command input of user
     */
    public void play(Command command) {
        switch (command.getCommandName()) {
            case "exit":      // Exits program
                System.exit(0);
            case "examine":                          // Runs examine()
                examine();
                break;
            case "inventory":                        // Runs printInventory
                printInventory();
                break;
            case "use":                              // Checks input for keyword "use"
                if (command.getCommandValue().equals("rare candy")) {
                    useRareCandy();
                } else if (command.getCommandValue().equals("town map")) {
                    useTownMap();
                } else {
                    message = "You don't have \"" + command + "\"!";
                }
                break;
            case "drop":                             // Checks input for keyword "drop"
                if (playerItem.contains(command.getCommandValue())) {
                    dropItem(command.getCommandValue());
                } else {
                    message = "You don't have \"" + command.getCommandValue() + "\"!";
                }
                break;
            case "take":                             // Checks input for keyword "take"
                takeItem(command.getCommandValue());
                break;
            case "choose":
                choosePokemon(command);
                break;

        /*else if (command.getCommandName().equals("fight")) {                           // Checks input for keyword "fight"
            fight();
        }*/
            case "go":                               // Checks input for keyword "go"
                if (command.getCommandValue().startsWith("talk")) {
                    talkTo();
                }
                if (location.getPath().contains(command.getCommandValue())) { // Checks if input is a possible move
                    move(command.getCommandValue());
                } else {
                    message = "I can't go \"" + command.getCommandValue() + "\"";
                }
                break;
            default:                                                           // Prints if input is invalid
                message = "I don't understand \"" + command + "\"";
                break;
        }
    }

    /**
     * Prints out the room's variables
     */
    public void examine() {
        message = "You are on " + location.getName() + ", " + location.getDescription();
        message = "from here, you can go: " + location.getPath();

        if (!location.getItems().isEmpty()) {
            message = "Items visible: " + location.getItems();
        }

        message = "Pokemon: " + pokemon;
    }

    /**
     * Updates location when pokemon fainted
     */
    private void fainted() {
        message = "Your pokemon has fainted, you are now back at your house.";
        message = "Get your pokemon stronger.";
        this.location = gameMap.getPalletTown(0);               // Move back to player's house
        locationIndex = 0;
    }

    /**
     * Change room status after talk
     */
    private void talkTo() {
        if (!gameMap.getPalletTown(2).isVisited()) {            // Checks if player already receives pokemon
            location = gameMap.getPalletTown(2);
            locationIndex = 2;
        }
        else {
            message = "Professor Oak is not here at the moment.";
        }

    }

    /**
     * Makes user choose pokemon
     * @param command input command
     */
    private void choosePokemon(Command command) {
        pokemon = command.getCommandValue();

        switch (pokemon) {                                           // Updates User's and Rival's pokemon
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
        gameMap.getPalletTown(3).haveVisited();
        location = gameMap.getPalletTown(3);
        locationIndex = 3;

    }

    /**
     * Determines user movement based on input
     * Updates user location and index on the map
     * @param command user input
     */
    public void move(String command) {
        if (command.equalsIgnoreCase("back to town")) {
            location = gameMap.getPalletTown(4);
            locationIndex = 4;
        }
        if (command.startsWith("run")) {
            location = gameMap.getPalletTown(locationIndex - 1);
            locationIndex -= 1;
        }
        if (command.equalsIgnoreCase("fight")) {
            //location.haveVisited();
            //location.changePath();
            fight();
        }
        if (command.equalsIgnoreCase("left")) {
            location = gameMap.getPalletTown(0);
            locationIndex = 0;
        }
        if (command.equalsIgnoreCase("right")) {
            location = gameMap.getPalletTown(3);
            locationIndex = 3;
        }
        if (command.equalsIgnoreCase("down")) {
            location = gameMap.getPalletTown(1);
            locationIndex = 1;
        }
        if (command.equalsIgnoreCase("up")) {
            if (pokemon.equalsIgnoreCase("None")) {  // Prohibits user on going to plains 1 without pokemon
                message = "You need a pokemon for this area. Go to Professor Oak's lab.";
            }
            else {
                location = gameMap.getPalletTown(locationIndex + 1);
                locationIndex += 1;
            }
        }
    }

    /**
     * Simulate fights on plains 1,2,3 and updates path of location
     */
    private void fight() {
        location.changePath();

        if (location.getName().equalsIgnoreCase("Plains 1")) {
            message = "A wild pidgey has appeared!\n";
            message += "You choose " + pokemon + " \n";
            message += pokemon + " use tackle.\n";
            message += "Pidgey has fainted.\n";
            message += "You won!\n";
            message += pokemon + " gained 100 xp \n";
            evolve();
            location.haveVisited();
        }

        if (location.getName().equalsIgnoreCase("Plains 2")) {
            message = "A wild caterpie has appeared! \n";
            message += "You choose " + pokemon + " \n";
            message += pokemon + " use tackle. \n";
            message += "Caterpie has fainted. \n";
            message += "You won! \n";
            message += pokemon + " gained 100 xp \n";
            location.haveVisited();
        }

        if (location.getName().equalsIgnoreCase("Plains 3")) {
            if (pokemon.equals("Charizard") || pokemon.equals("Blastoise") || pokemon.equals("Venusaur")) {
                message += "Gary has appeared! \n";
                message += "Gary choose " + garyPokemon + " \n";
                message += "You choose " + pokemon +" \n";
                message += pokemon + " use tackle. \n";
                message += garyPokemon + " has fainted. \n";
                message += "You won! \n";
                message += pokemon + " gained 100 xp \n";
                location.haveVisited();
            }
            else {
                message = "Gary's pokemon is too strong!";
                fainted();
            }
        }
    }

    /**
     * Prints current location and town map when user inputs to use town map
     */
    private void useTownMap() {
        message = "Town Map:";
        message += "                 Plains 3";
        message += "                    ||";
        message += "                 Plains 2";
        message += "                    ||";
        message += "                 Plains 1";
        message += "                    ||";
        message += "Player's Home - Town Square - Professor Oak's Lab";
        message += "                    ||";
        message += "               Rival's House\n";
        message += "You are at:" + location.getName();
    }

    /**
     * Updates pokemon's status
     */
    private void useRareCandy() {
        if (pokemon.equals("None")) {       //Checks if user has pokemon
            message = "You don't have a pokemon to use this candy on.";
        }
        else {
            message = "Rare candy is used on " + pokemon;
            playerItem.remove("rare candy");
            evolve();
        }
    }

    /**
     * Removes item from the room and adds it to playerItem
     * If the input data is not in the room, prints out no item
     * @param itemName string of user input
     */
    public void takeItem(String itemName) {
        if (location.getItems().contains(itemName)) {           // Checks if the item input is in the room
            playerItem.add(itemName);
            location.grabItem(itemName);
        }
        else {
            message = "There is no item \"" + itemName + "\" in the room";
        }
    }

    /**
     * Removes item from user and adds it to the room
     * @param itemName user input
     */
    public void dropItem(String itemName) {
        if (!playerItem.contains(itemName)) {                   // Checks if the player does not have the item
            message = "You don't have \"" + itemName + "\"";
        }
        else if (location.getItems().contains(itemName)) {      // Checks if the item is already in the room
            message = "The item \"" + itemName + "\" is already in this room!";
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
            message = "You don't have any items in your inventory.";
        }
        else {
            message = playerItem.toString();
        }
    }

    /**
     * Method to evolve pokemon
     */
    public void evolve() {
        message = "What? " + pokemon + " is evolving!";
        if (pokemon.equalsIgnoreCase("charmander")) {
            message += pokemon + " has evolved to Charmeleon!";
            pokemon = "Charmeleon";
        }
        else if (pokemon.equalsIgnoreCase("squirtle")) {
            message += pokemon + " has evolved to Wartortle!";
            pokemon = "Wartortle";
        }
        else if (pokemon.equalsIgnoreCase("bulbasaur")) {
            message += pokemon + " has evolved to Ivysaur!";
            pokemon = "Ivysaur";
        }
        else if (pokemon.equalsIgnoreCase("charmeleon")) {
            message += pokemon + " has evolved to Charizard!";
            pokemon = "Charizard";
        }
        else if (pokemon.equalsIgnoreCase("wartortle")) {
            message += pokemon + " has evolved to Blastoise!";
            pokemon = "Blastoise";
        }
        else if (pokemon.equalsIgnoreCase("ivysaur")) {
            message += pokemon + " has evolved to Venusaur!";
            pokemon = "Venusaur";
        }
    }

    /**
     * Compiles all the possible command in the room
     * @return commandOptions map of commands
     */
    public HashMap<String, List<String>> getCommands() {
        HashMap<String, List<String>> commandOptions = new HashMap<>();
        if (locationIndex == 2) {
            String commandName = "choose";
            commandOptions.put(commandName, location.getPath());
        }
        else {
            if (!playerItem.isEmpty()) {
                String commandName = "drop";
                //List<String> commandValue = new ArrayList<>(playerItem);
                commandOptions.put(commandName, playerItem);
                String commandName2 = "use";
                List<String> commandValue = new ArrayList<>(playerItem);
                if (playerItem.contains("rare candy") && pokemon.equals("None")) {
                    commandValue.remove("rare candy");
                }
                commandOptions.put(commandName2, commandValue);
            }
            if (!(location.getItems()).isEmpty()) {
                String commandName = "take";
                //List<String> commandValue = new ArrayList<>(location.getItems());
                commandOptions.put(commandName, location.getItems());
            }
            if (!(location.getPath()).isEmpty()) {
                String commandName = "go";
                //List<String> commandValue = location.getPath();
                commandOptions.put(commandName, location.getPath());
            }
        }
        List<String> exit = new ArrayList<>();
        exit.add("");
        commandOptions.put("exit", exit);
        return commandOptions;
    }

    public String getMessage() {
        if (this.message.equals("")) {
            return location.getName() + ", " + location.getDescription();
        }
        return this.message;
    }
}
