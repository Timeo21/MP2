package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Turret extends ICRogueActor {

    private Sprite sprite;
    private List<Orientation> shootDirections;
    private Animation[] animations;


    public Turret(Area area, Orientation orientation, DiscreteCoordinates position, List<Orientation> shootDirections) {
        super(area, orientation, position);
        this.shootDirections = shootDirections;
        Sprite[][] sprites = Sprite.extractSprites("custom/turret", 6, .9f, .9f, this, 16, 16,new Vector(0,.05f),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        animations = Animation.createAnimations(9,sprites,false);
        area.registerActor(this);
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }


    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {
        for (Animation animation : animations) animation.update(deltaTime);
        if (animations[0].isCompleted()){
            for (Orientation shootDirection : shootDirections) {
                new Arrow(getOwnerArea(), shootDirection, getCurrentMainCellCoordinates());
            }
            animations[0].reset();
        }
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        animations[0].draw(canvas);
    }

}
