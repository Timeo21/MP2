package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class ICRogueActor extends MovableAreaEntity {
    public boolean isDead;
    public int life;

    public ICRogueActor(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }

    /**
     * Manage interaction between Actor and interactable
     * @param other (Interactable) : Interactable the interactor interact with
     * @param isCellInteraction (boolean) : the interactable and interactor are on the same cell
     */
    public void interactWith(Interactable other, boolean isCellInteraction) {
    }

    @Override
    public void draw(Canvas canvas) {
    }

    /**
     * Make actor lose a certain amount of life
     * @param damage (int) : amount of damage done to the target
     */
    public void takeDamage(int damage){
        life = life - damage;
        if (life <= 0) die();
    }

    /**
     * Make the actor dead and remove it
     */
    public void die(){
        isDead = true;
        leaveArea();
    }

    /**
     * Remove the actor from the area
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }
}
