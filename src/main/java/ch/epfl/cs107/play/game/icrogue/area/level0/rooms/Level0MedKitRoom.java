package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.MedKit;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0MedKitRoom extends Level0Room{
    public Level0MedKitRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
    }

    @Override
    protected void createArea() {
        super.createArea();
        new MedKit(this, Orientation.DOWN,new DiscreteCoordinates(4,4),0);
    }
}