package student.server;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import student.adventure.GameEngine;

/**
 * A class to represent values in a game state.
 *
 * Note: these fields should be JSON-serializable values, like Strings, ints, floats, doubles, etc.
 * Please don't nest objects, as the frontend won't know how to display them.
 *
 * Good example:
 * private String shoppingList;
 *
 * Bad example:
 * private ShoppingList shoppingList;
 */
@JsonSerialize
public class AdventureState {
    // TODO: Add any additional state your game needs to this object.
    // E.g.: If your game needs to display a life total, you could add:
    // private int lifeTotal;
    // ...and whatever constructor/getters/setters you'd need
    private String pokemon;
    private String inventory;

    public AdventureState(GameEngine game) {
        pokemon = game.pokemon;
        inventory = game.playerItem.toString();
    }

    public String getPokemon() {
        return pokemon;
    }

    public String getInventory() {
        return inventory;
    }
}
