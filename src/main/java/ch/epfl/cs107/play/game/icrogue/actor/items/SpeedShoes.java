package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SpeedShoes extends Item{
    private Sprite[][] sprites;
    private Animation[] animations;

    public SpeedShoes(Area area, Orientation orientation, DiscreteCoordinates position,int price) {
        super(area, orientation, position,price);

        sprites = Sprite.extractSprites("custom/shoes",4,.6f,.6f,this,16,16,new Vector(.2f,.15f),
                new Orientation[] {Orientation.DOWN , Orientation.RIGHT , Orientation.UP, Orientation.LEFT});

        animations = Animation.createAnimations(5, sprites);

        area.registerActor(this);
    }

    @Override
    public void update(float deltaTime) {
        animations[2].update(deltaTime);
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        animations[2].draw(canvas);
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
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
    }
}
