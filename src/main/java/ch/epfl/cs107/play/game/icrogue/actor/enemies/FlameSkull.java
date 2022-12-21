package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import static ch.epfl.cs107.play.game.icrogue.RandomHelper.enemyGenerator;

public class FlameSkull extends ICRogueActor {
    private Area area;
    private Orientation orientation;
    private Animation[] movingAnim;
    private Animation[] damageAnim;
    private DiscreteCoordinates playerCoords;
    private DiscreteCoordinates currentPosition;
    public boolean isDamage;
    private float immunityDuration;
    private int speed;


    public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.area = area;
        this.orientation = orientation;
        immunityDuration = 1f;
        life = 3;
        speed = 15;
        Sprite[][] animationSprites = Sprite.extractSprites("zelda/flameskull", 3, 1.2f, 1.2f, this, 32, 32,new Vector(-.1f,0),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        movingAnim = Animation.createAnimations(5,animationSprites);
        animationSprites = Sprite.extractSprites("custom/damageFlamskull", 3, 1.2f, 1.2f, this, 32, 32,new Vector(-.1f,0),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        damageAnim= Animation.createAnimations(5,animationSprites);
        area.registerActor(this);
    }

    @Override
    public boolean isCellInteractable() {
        return !isDead;
    }

    @Override
    public boolean isViewInteractable() {
        return !isDead;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {
        immunityDuration -= deltaTime;
        if (immunityDuration < 0) {
            isDamage = false;
            immunityDuration = 1f;
        }
        currentPosition = getCurrentMainCellCoordinates();
        for (Animation animation : movingAnim) animation.update(deltaTime);
        for (Animation animation : damageAnim) animation.update(deltaTime);
        playerCoords = ICRogue.getPlayerCoords();
        if (!isDamage){
            followPlayer();
        }
        super.update(deltaTime);
    }

    /**
     * Make the skullflame go to player coordinate to follow him
     */
    private void followPlayer(){
        if (isInDisplacement()) return;
        int random = enemyGenerator.nextInt(2);
        if (random == 0){
            if(!moveOnX()){
                moveOnY();
            }
        } else {
            if(!moveOnY()){
                moveOnX();
            }
        }
    }

    /**
     *  Make the flameskull move on the X-axis to match player's X
     * @return (boolean): The flameskull has move on X-axis
     */
    private boolean moveOnX(){
        if (playerCoords.x < currentPosition.x){
            orientation = Orientation.LEFT;
            orientate(orientation);
            return move(speed);
        } else if (playerCoords.x > currentPosition.x) {
            orientation = Orientation.RIGHT;
            orientate(orientation);
            return move(speed);
        }
        return false;
    }

    /**
     *  Make the flameskull move on the Y-axis to match player's Y
     * @return (boolean): The flameskull has move on Y-axis
     */
    private boolean moveOnY(){
        if (playerCoords.y < currentPosition.y){
            orientation = Orientation.DOWN;
            orientate(orientation);
            return move(speed);
        } else if (playerCoords.y > currentPosition.y) {
            orientation = Orientation.UP;
            orientate(orientation);
            return move(speed);
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if(isDamage){
            damageAnim[orientation.ordinal()].draw(canvas);
        } else {
            movingAnim[orientation.ordinal()].draw(canvas);
        }
    }

    @Override
    public void takeDamage(int damage) {
        if(!isDamage){
            isDamage = true;
            speed -= 3;
            super.takeDamage(damage);
        }
    }
}
