package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Arrow extends Projectile implements Consumable, Interactor {
    private boolean isConsumed;
    private Sprite sprite;
    private ArrowInteractionHandler interactionHandler;
    private Area area;
    public Arrow(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);
        sprite = new Sprite("zelda/arrow", 1f, 1f, this ,
                new RegionOfInterest(32* orientation.ordinal(), 0, 32, 32),
                new Vector(0, 0));
        interactionHandler = new ArrowInteractionHandler();
        this.area = area;
        area.registerActor(this);
    }

    @Override
    public void update(float deltaTime) {
        if(!isConsumed){
            move(5);
        }
        super.update(deltaTime);
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void consume() {
        isConsumed = true;
        area.unregisterActor(this);
    }

    @Override
    public boolean isConsumed() {
        return isConsumed;
    }

    @Override
    public void draw(Canvas canvas){
        if(!isConsumed){
            sprite.draw(canvas);
        }
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler,isCellInteraction);
    }

    private class ArrowInteractionHandler implements ICRogueInteractionHandler{
        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if(cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.NONE)||cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.WALL)|| cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.HOLE)){
                consume();
            }
        }

        @Override
        public void interactWith(ICRoguePlayer player, boolean isCellInteraction) {
            player.isDead = true;
            consume();
        }
    }
}
