package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Arrays;
import java.util.List;

public class Level0TurretRoom extends Level0EnemyRoom{
    private List<DiscreteCoordinates> positions;
    public Level0TurretRoom(DiscreteCoordinates coordinates, List<DiscreteCoordinates> positions){
        super(coordinates);
        this.positions = positions;
    }
    public Level0TurretRoom(DiscreteCoordinates coordinates) {
        this(coordinates,Arrays.asList(new DiscreteCoordinates(1,8),new DiscreteCoordinates(8,1)));
    }

    @Override
    protected void createArea() {
        for (DiscreteCoordinates position : positions){
            addEnemy(new Turret(this, Orientation.UP,position, Arrays.asList(Orientation.DOWN,Orientation.RIGHT,Orientation.UP,Orientation.LEFT)));
        }
        super.createArea();
    }
}
