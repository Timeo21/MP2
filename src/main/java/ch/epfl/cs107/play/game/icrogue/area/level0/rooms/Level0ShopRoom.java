package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.SpeedShoes;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0ShopRoom extends Level0Room{
    public Level0ShopRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
    }

    @Override
    protected void createArea() {
        new SpeedShoes(this, Orientation.DOWN,new DiscreteCoordinates(2,2));
        super.createArea();
    }
}
