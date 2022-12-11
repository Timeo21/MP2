package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import static ch.epfl.cs107.play.game.icrogue.RandomHelper.enemyGenerator;

public class FlameSkull extends ICRogueActor {
    private Area area;
    private Orientation orientation;
    private Animation[] animations;
    private DiscreteCoordinates playerCoords;
    private DiscreteCoordinates currentPosition;

    public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.area = area;
        this.orientation = orientation;
        Sprite[][] animationSprites = Sprite.extractSprites("zelda/flameskull", 3, 1.2f, 1.2f, this, 32, 32,new Vector(-.1f,0),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        animations = Animation.createAnimations(5,animationSprites);
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
        currentPosition = getCurrentMainCellCoordinates();
        for (Animation animation : animations) animation.update(deltaTime);
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
        switch (orientation){
            case UP -> animations[0].draw(canvas);
            case RIGHT -> animations[1].draw(canvas);
            case DOWN -> animations[2].draw(canvas);
            case LEFT -> animations[3].draw(canvas);
        }
    }
}
