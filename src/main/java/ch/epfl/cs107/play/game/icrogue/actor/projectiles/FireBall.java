package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.FlameSkull;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class FireBall extends Projectile implements Consumable, Interactor{
    private Sprite sprite;
    private Sprite[][] sprites;
    private Animation[] animations;
    private Area area;
    private Orientation orientation;
    private FireBallInteractionHandler interactionHandler;
    private Interactable interactable = this;
    private FireBall fireBall ;
    private DiscreteCoordinates coordinates;
    private boolean isConsumed;
    private ICRoguePlayer player;
    public FireBall(Area area, Orientation orientation, DiscreteCoordinates position, ICRoguePlayer player) {
        super(area, orientation, position, 5, 1);


        sprites = Sprite.extractSprites("zelda/fire",7, 1f, 1f, this , 16, 16,
                new Orientation[] {Orientation.DOWN , Orientation.RIGHT , Orientation.UP, Orientation.LEFT});
        animations = Animation.createAnimations(2,sprites);

        sprite = new Sprite("zelda/grass",1f,1f,this, new RegionOfInterest(0,0,16,16), new Vector(0,0));

        this.area = area;
        this.orientation = orientation;
        interactionHandler = new FireBallInteractionHandler();
        this.player = player;
        area.registerActor(this);
    }

    @Override
    public void update(float deltaTime) {
        if (!isConsumed){
            animations[2].update(deltaTime);
            move(5);
        }
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isConsumed){
            //sprite.draw(canvas);
            animations[2].draw(canvas);
        }
    }

    @Override
    public void consume() {
        if (!isConsumed) {
            area.unregisterActor(this);
            isConsumed = true;
        }
    }

    @Override
    public boolean isConsumed() {
        return isConsumed;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
    }

    @Override
    public boolean wantsCellInteraction() {
        return !isConsumed;
    }

    @Override
    public boolean wantsViewInteraction() {
        return !isConsumed;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler,isCellInteraction);
    }

    private class FireBallInteractionHandler implements ICRogueInteractionHandler{
        @Override
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if (isCellInteraction){
                turret.die();
                player.coin++;
                consume();
            }
        }

        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if(cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.NONE)
                    || cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.WALL)
                    || cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.HOLE)) {
                consume();
            }
        }
        @Override
        public void interactWith(FlameSkull flameSkull, boolean isCellInteraction) {
                flameSkull.takeDamage(1);
                player.coin += 3;
                consume();
        }
    }
}


