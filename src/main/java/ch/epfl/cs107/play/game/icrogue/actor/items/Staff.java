package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Staff extends Item{
    private Sprite[][] sprites;
    private Animation[] animations;
    public Staff(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        sprites = Sprite.extractSprites("zelda/staff",8,1,1,this,32,32,new Vector(0,0),
                new Orientation[] {Orientation.DOWN , Orientation.RIGHT , Orientation.UP, Orientation.LEFT});
        animations = Animation.createAnimations(4, sprites);
        area.registerActor(this);
    }

    @Override
    public void update(float deltaTime) {
        animations[2].update(deltaTime);
        super.update(deltaTime);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return super.getCurrentCells();
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
    }

    @Override
    public void draw(Canvas canvas) {
        animations[2].draw(canvas);
    }
}
