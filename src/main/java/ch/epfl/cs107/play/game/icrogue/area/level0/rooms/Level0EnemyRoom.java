package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Level0EnemyRoom extends Level0Room{
    private List<ICRogueActor> enemies = new ArrayList<>();
    public Level0EnemyRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        logic = FALSE;
    }
    @Override
    public void update(float deltaTime) {
        checkCompletion();
        super.update(deltaTime);
    }

    /**
     * Add the enemy to the enemies list of the room
     * @param enemy (ICRogueActor) : Enemy to add
     */
    protected void addEnemy(ICRogueActor enemy){
        enemies.add(enemy);
    }

    /**
     * Check if the room is complete
     */
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
