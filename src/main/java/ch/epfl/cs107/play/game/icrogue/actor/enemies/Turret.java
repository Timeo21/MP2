package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
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
    private Area area;
    private DiscreteCoordinates position;
    private Orientation orientation;
    private Sprite sprite;
    private List<Orientation> shootDirections;
    private final float COOLDOWN = 2.f;
    private float timeWaited = 0.f;


    public Turret(Area area, Orientation orientation, DiscreteCoordinates position, List<Orientation> shootDirections) {
        super(area, orientation, position);
        this.area = area;
        this.position = position;
        this.orientation = orientation;
        this.shootDirections = shootDirections;
        sprite = new Sprite("icrogue/static_npc", 1.5f, 1.5f,
                this , null , new Vector(-0.25f, 0));
        area.registerActor(this);
    }

    public void die(){
        isDead = true;
        area.unregisterActor(this);
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
        timeWaited += deltaTime;
        if (timeWaited >= COOLDOWN) {
            for (Orientation shootDirection : shootDirections) {
                new Arrow(getOwnerArea(), shootDirection, getCurrentMainCellCoordinates());
                timeWaited = 0.f;
            }
        }
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if(!isDead) {
            sprite.draw(canvas);
        }
    }

}
