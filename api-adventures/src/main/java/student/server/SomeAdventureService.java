package student.server;

import student.adventure.GameEngine;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class SomeAdventureService implements AdventureService {
    public int id;
    public HashMap<Integer, GameEngine> instanceMap = new HashMap<>();
    private final static String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventure.db";
    private Connection dbConnection;


    @Override
    public void reset() {
        instanceMap.clear();
        id = 0;
    }

    @Override
    public int newGame() throws AdventureException {
        int currentID = id;
        instanceMap.put(id, new GameEngine());
        id++;
        return currentID;
    }

    @Override
    public GameStatus getGame(int id) {
        GameEngine game = instanceMap.get(id);
        String imageURL = game.location.getImageURL();
        String videoURL = game.location.getImageURL();
        String message = game.getMessage();
        //boolean error, int id, String message, String imageUrl, String videoUrl, AdventureState state, Map<String, List<String>> commandOptions
        GameStatus status = new GameStatus(false, id, message, imageURL, videoURL, new AdventureState(game), game.getCommands());
        return status;
    }

    @Override
    public boolean destroyGame(int id) {
        instanceMap.remove(id);
        return false;
    }

    @Override
    public void executeCommand(int id, Command command) {
        if (instanceMap.containsKey(id)) {
            GameEngine game = instanceMap.get(id);
            game.run(command);
        }
    }

    @Override
    public LinkedHashMap<String, Integer> fetchLeaderboard() {
        LinkedHashMap<String, Integer> leaderboard;
        leaderboard = getDataBaseData();
        return leaderboard;
    }

    private LinkedHashMap<String, Integer> getDataBaseData() {
        LinkedHashMap leaderboard = new LinkedHashMap();
        Connection dbConnection;
        ResultSet results;

        try {
            dbConnection = DriverManager.getConnection(DATABASE_URL);
            Statement stmt = dbConnection.createStatement();
            if (stmt.execute("SELECT name, score FROM leaderboard_aangelo2" + "ORDER BY score DESC")) {
                results = stmt.getResultSet();
            } else {
                results = null;
                System.out.println("Error");
            }

            try {
                while (results.next()) {
                    String name = results.getString("Name");
                    int score = results.getInt("Score");
                    leaderboard.put(name, score);
                }
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    return leaderboard;
    }

}
