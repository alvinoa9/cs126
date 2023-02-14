package student.adventure;
import java.util.*;

public class PalletTown {
    private String name;
    private String description;
    private List<String> items;
    private boolean visited;
    private String path;

    public String getName() {

        return name;
    }

    public String getDescription() {

        return description;
    }

    public List<String> getItems() {

        return items;
    }

    public boolean isVisited() {

        return visited;
    }


    public String getPath() {

        return path;
    }

    /**
     * Change value of visited
     */
    public void haveVisited() {

        visited = true;
    }

    /**
     * Change value of path
     */
    public void changePath() {

        path = "Up or Back to town";
    }

    /**
     * Removes item from room
     * @param item item to be removed
     */
    public void grabItem(String item) {

        items.remove(item);
    }

    /**
     * Adds item to room
     * @param item item to be added
     */
    public void putItem(String item) {

        items.add(item);
    }
}
