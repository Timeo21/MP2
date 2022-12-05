package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.FireBall;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor {
    private Sprite[][] sprite;
    private Sprite[] sprites = new Sprite[4];
    private Animation[] animations;
    private Orientation orientation;
    private Area area;
    private ICRoguePlayerInteractionHandler playerInteractionHandler;
    private boolean hasStaff;
    private final static int MOVE_DURATION = 8;

    /**
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public ICRoguePlayer(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position);
        this.orientation = orientation;
        this.area = area;
        this.playerInteractionHandler = new ICRoguePlayerInteractionHandler();
        this.hasStaff = false;

        sprite = Sprite.extractSprites("zelda/player",4, 0.75f, 1.5f, this , 16, 32,new Vector(0.15f, -0.15f),
                 new Orientation[] {Orientation.DOWN , Orientation.RIGHT , Orientation.UP, Orientation.LEFT});

        animations = Animation.createAnimations(MOVE_DURATION/2,sprite);
        /*
        sprites[0] = new Sprite(spriteName,.75f,1.5f,this, new RegionOfInterest(0,0,16,32),new Vector(0.15f, -0.15f));
        sprites[1] = new Sprite(spriteName,.75f,1.5f,this, new RegionOfInterest(0,32,16,32),new Vector(0.15f, -0.15f));
        sprites[2] = new Sprite(spriteName,.75f,1.5f,this, new RegionOfInterest(0,64,16,32),new Vector(0.15f, -0.15f));
        sprites[3] = new Sprite(spriteName,.75f,1.5f,this, new RegionOfInterest(0,96,16,32),new Vector(0.15f, -0.15f));

         */
/*
        sprites[0] = new Sprite("mew.fixed",1f,1f,this,new RegionOfInterest(0,0,16,21),new Vector(0.15f,0.15f));
        sprites[3] = new Sprite("mew.fixed",1f,1f,this,new RegionOfInterest(16,0,16,21),new Vector(0.15f,0.15f));
        sprites[2] = new Sprite("mew.fixed",1f,1f,this,new RegionOfInterest(32,0,16,21),new Vector(0.15f,0.15f));
        sprites[1] = new Sprite("mew.fixed",1f,1f,this,new RegionOfInterest(48,0,16,21),new Vector(0.15f,0.15f));

 */


        resetMotion();
    }

    @Override
    public void update(float deltaTime){
        for (Animation animation : animations) {
            animation.update(deltaTime);
        }
        Keyboard keyboard= getOwnerArea().getKeyboard();
        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        fireBallIfPressed(keyboard.get(Keyboard.X));
        if (keyboard.get(Keyboard.O).isPressed()) cheatIfPressed(1);
        if (keyboard.get(Keyboard.L).isPressed()) cheatIfPressed(2);
        if (keyboard.get(Keyboard.T).isPressed()) cheatIfPressed(3);

        super.update(deltaTime);
    }


    /**
     * Orientate and Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param button (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button button){
        if(button.isDown()){
            if (!isInDisplacement()) {
                this.orientation = orientation;
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    private void fireBallIfPressed(Button button){
        if(button.isPressed() && hasStaff){
            new FireBall(getOwnerArea(),orientation,getCurrentMainCellCoordinates());
        }
    }

    private void cheatIfPressed(int cheatCode){
        switch (cheatCode){
            case 1:
                for (Connector connector: ((Level0Room)area).getConnectors()){
                    connector.setStats(Connector.ConnectorStats.OPEN);
            }
                break;
            case 2:
                Connector connector0 = ((Level0Room)area).getConnectors().get(2);
                connector0.setStats(Connector.ConnectorStats.LOCKED,1);
                break;
            case 3:
                for (Connector connector: ((Level0Room)area).getConnectors()){
                    connector.switchDoor();
                }
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        /*
        switch (orientation ){
            case UP -> sprites[2].draw(canvas);
            case DOWN -> sprites[0].draw(canvas);
            case RIGHT -> sprites[1].draw(canvas);
            case LEFT -> sprites[3].draw(canvas);
        }
         */
        switch (orientation ){
            case UP -> animations[0].draw(canvas);
            case DOWN -> animations[2].draw(canvas);
            case RIGHT -> animations[1].draw(canvas);
            case LEFT -> animations[3].draw(canvas);
        }

    }

    public void enterArea(Area area, DiscreteCoordinates coordinates){
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(coordinates.toVector());
        resetMotion();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean takeCellSpace() {
        return true;
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
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
    }




    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {

        return getOwnerArea().getKeyboard().get(Keyboard.W).isPressed();
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(playerInteractionHandler,isCellInteraction);
    }

    public class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler{
        public void interactWith(Key key, boolean isCellInteraction) {
            area.unregisterActor(key);
        }
        public void interactWith(Staff staff, boolean isCellInteraction) {
            hasStaff=true;
            area.unregisterActor(staff);
        }
    }
}


