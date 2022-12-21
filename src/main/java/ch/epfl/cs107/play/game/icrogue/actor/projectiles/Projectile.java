package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends ICRogueActor implements Interactor {
    private Area area;
    private int frame;
    private int damage;
    private DiscreteCoordinates coordinates;
    protected final static int DEFAULT_DAMAGE = 1;
    protected final static int DEFAULT_MOVE_DURATION = 10;

    public Projectile(Area area, Orientation orientation, DiscreteCoordinates coordinates, int frame, int damage) {
        super(area, orientation, coordinates);
        this.frame = frame;
        this.damage = damage;
    }
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates coordinates){
        this(area,orientation,coordinates,DEFAULT_MOVE_DURATION, DEFAULT_DAMAGE);

    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return super.getCurrentCells();
    }

    public List<DiscreteCoordinates> getFieldOfViewCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(coordinates.toVector());
    }
    protected int getFrame(){
        return frame;
    }
    public int getDamage(){
        return damage;
    }
}
