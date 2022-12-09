package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0StaffRoom extends Level0ItemRoom{
    public Level0StaffRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
    }

    @Override
    protected void createArea() {
        Item item = new Staff(this, Orientation.DOWN,new DiscreteCoordinates(5,5));
        addItem(item);
        super.createArea();
    }

}
