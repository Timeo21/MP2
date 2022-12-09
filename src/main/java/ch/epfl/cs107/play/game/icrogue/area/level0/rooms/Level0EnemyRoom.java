package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level0EnemyRoom extends Level0Room{
    private List<ICRogueActor> enemies = new ArrayList<>();
    public Level0EnemyRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        logic = FALSE;
    }

    @Override
    protected void createArea() {
        addEnemy();
        super.createArea();
    }

    protected void addEnemy(){
        enemies.add(new Turret(this, Orientation.UP,new DiscreteCoordinates(1,8), Arrays.asList(Orientation.DOWN,Orientation.RIGHT)));
        enemies.add(new Turret(this, Orientation.UP,new DiscreteCoordinates(8,1), Arrays.asList(Orientation.UP,Orientation.LEFT)));
    }

    @Override
    public void update(float deltaTime) {
        checkCompletion();
        super.update(deltaTime);
    }

    public void checkCompletion(){
        if (logic.isOff()){
            List<ICRogueActor> enemiesCopy = new ArrayList<>(enemies);
            for (ICRogueActor enemy : enemiesCopy){
                if (enemy.isDead){
                    enemies.remove(enemy);
                }
            }
            if (enemies.size() == 0) {
                logic = TRUE;
            }
        }
    }

}
