package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public abstract class Level0ItemRoom extends Level0Room{
    private List<Item> items = new ArrayList<>();
    private float completeValue;
    public Level0ItemRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        logic = FALSE;
    }

    @Override
    public void update(float deltaTime) {
        checkCompletion();
        super.update(deltaTime);
    }

    /**
     * Add an item to the item list of the room
     * @param item (Item) : Item to add
     */
    protected void addItem(Item item){
        items.add(item);
    }

    /**
     * Check if the room is complete
     */
    public void checkCompletion(){
        if (logic.isOff()){
            List<Item> itemsCopy = new ArrayList<>(items);
            for (Item item : itemsCopy){
                if (item.isCollected()){
                    items.remove(item);
                }
            }
            if (itemsCopy.size() == 0) {
                logic = TRUE;
            }
        }
    }
}
