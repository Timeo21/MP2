package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0KeyRoom extends Level0ItemRoom{
    private int keyId;
    public Level0KeyRoom(DiscreteCoordinates coordinates, int keyId) {
        super(coordinates);
        this.keyId = keyId;
    }

    @Override
    protected void createArea() {
        new Key(this, Orientation.DOWN,new DiscreteCoordinates(5,5),keyId);
        super.createArea();
    }
}
