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
    private boolean isDamage;
    private float imunityDuration;

    public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.area = area;
        this.orientation = orientation;
        imunityDuration = .7f;
        life = 3;
        Sprite[][] animationSprites = Sprite.extractSprites("zelda/flameskull", 3, 1.2f, 1.2f, this, 32, 32,new Vector(-.1f,0),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        movingAnim = Animation.createAnimations(5,animationSprites);
        animationSprites = Sprite.extractSprites("custom/damageFlamskull", 3, 1.2f, 1.2f, this, 32, 32,new Vector(-.1f,0),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        damageAnim= Animation.createAnimations(5,animationSprites);
        area.registerActor(this);
    }

    public void die(){
        isDead = true;
        area.unregisterActor(this);
    }

    @Override
    public boolean isCellInteractable() {
        return !isDead;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {
        imunityDuration -= deltaTime;
        if (imunityDuration < 0) {
            isDamage = false;
            imunityDuration = .7f;
        }
        currentPosition = getCurrentMainCellCoordinates();
        for (Animation animation : movingAnim) animation.update(deltaTime);
        for (Animation animation : damageAnim) animation.update(deltaTime);
        playerCoords = ICRogue.getPlayerCoords();
        followPlayer();

        super.update(deltaTime);
    }

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

    private boolean moveOnX(){
        if (playerCoords.x < currentPosition.x){
            orientation = Orientation.LEFT;
            orientate(orientation);
            return move(15);
        } else if (playerCoords.x > currentPosition.x) {
            orientation = Orientation.RIGHT;
            orientate(orientation);
            return move(15);
        }
        return false;
    }

    private boolean moveOnY(){
        if (playerCoords.y < currentPosition.y){
            orientation = Orientation.DOWN;
            orientate(orientation);
            return move(15);
        } else if (playerCoords.y > currentPosition.y) {
            orientation = Orientation.UP;
            orientate(orientation);
            return move(15);
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if(isDamage){
            switch (orientation){
                case UP -> damageAnim[0].draw(canvas);
                case RIGHT -> damageAnim[1].draw(canvas);
                case DOWN -> damageAnim[2].draw(canvas);
                case LEFT -> damageAnim[3].draw(canvas);
            }
        } else {
            switch (orientation){
                case UP -> movingAnim[0].draw(canvas);
                case RIGHT -> movingAnim[1].draw(canvas);
                case DOWN -> movingAnim[2].draw(canvas);
                case LEFT -> movingAnim[3].draw(canvas);
            }
        }
    }

    @Override
    public void takeDamage(int damage) {
        isDamage = true;
        super.takeDamage(damage);
    }
}
