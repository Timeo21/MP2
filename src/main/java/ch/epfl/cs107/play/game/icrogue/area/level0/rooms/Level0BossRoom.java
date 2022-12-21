package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.DarkLord;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0BossRoom extends Level0EnemyRoom{

    public Level0BossRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
    }

    @Override
    protected void createArea() {
        super.createArea();
        addEnemy(new DarkLord(this, Orientation.RIGHT,new DiscreteCoordinates(8,5)));
    }

    public void createBoss(int connectorIndex){
        switch (connectorIndex){
            //E
            case 0 -> addEnemy(new DarkLord(this, Orientation.RIGHT,new DiscreteCoordinates(8,5)));
            //N
            case 1 -> addEnemy(new DarkLord(this, Orientation.UP,new DiscreteCoordinates(5,8)));
            //W
            case 2 -> addEnemy(new DarkLord(this, Orientation.LEFT,new DiscreteCoordinates(1,5)));
            //S
            case 3 -> addEnemy(new DarkLord(this, Orientation.DOWN,new DiscreteCoordinates(5,1)));
        }
    }
}
