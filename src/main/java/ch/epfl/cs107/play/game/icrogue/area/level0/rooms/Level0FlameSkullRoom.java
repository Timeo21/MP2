package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.FlameSkull;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0FlameSkullRoom extends Level0EnemyRoom{
    public Level0FlameSkullRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
    }
    @Override
    protected void createArea() {
        addEnemy(new FlameSkull(this,Orientation.DOWN,new DiscreteCoordinates(4,4)));
        super.createArea();
    }
}
