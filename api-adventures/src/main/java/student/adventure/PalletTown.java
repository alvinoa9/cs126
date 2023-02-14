package student.adventure;

import java.util.List;

public class PalletTown {
    private String name;
    private String description;
    private List<String> items;
    private boolean visited;
    private List<String> path;
    private String imageURL;
    private String videoURL;

    public String getImageURL() {
        return imageURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

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


    public List<String> getPath() {

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

        path.set(0, "Up");
        path.set(1, "Back to town");
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
