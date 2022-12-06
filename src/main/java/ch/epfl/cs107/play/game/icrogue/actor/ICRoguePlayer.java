package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor {
    private Sprite[] idleSprites = new Sprite[4];
    private Animation[] walkAnimations;
    private Animation[] staffAnimations;
    private Orientation orientation;
    private boolean fireBalling;
    private Area area;
    private ICRoguePlayerInteractionHandler playerInteractionHandler;
    private boolean hasStaff;
    private final static int MOVE_DURATION = 8;
    private List<Key> inventory = new ArrayList<>();

    int cooldown = 0;

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

        // Walking Animations
        Sprite[][] animationSprites = Sprite.extractSprites("zelda/player", 4, 0.75f, 1.5f, this, 16, 32, new Vector(0.15f, -0.15f),
                new Orientation[]{Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});

        walkAnimations = Animation.createAnimations(MOVE_DURATION/2, animationSprites);

        // Using Staff Animations
        animationSprites = Sprite.extractSprites("zelda/player.staff_water", 4, 1.5f, 1.5f, this, 32, 32,new Vector(-0.20f, -0.15f),
                new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});

        staffAnimations = Animation.createAnimations(2,animationSprites);

        // Idle sprite
        for (int i = 0; i < 4; i++) {
            idleSprites[i] = new Sprite(spriteName,.75f,1.5f,this, new RegionOfInterest(0,i*32,16,32),new Vector(0.15f, -0.15f));
        }

        resetMotion();
    }

    @Override
    public void update(float deltaTime) {
        // Shooting fireball cooldown
        if (fireBalling){
            if (cooldown > 10){
                fireBalling = false;
                cooldown = 0;
            } else {
                cooldown++;
            }
        }

        for (Animation animation : walkAnimations) {
            animation.update(deltaTime);
        }
        for (Animation animation : staffAnimations) {
            animation.update(deltaTime);
        }

        Keyboard keyboard= getOwnerArea().getKeyboard();
        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        fireBallIfPressed(keyboard.get(Keyboard.X));

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
        // Press X key, has the staff and is not on cooldown
        if(button.isPressed() && hasStaff && !fireBalling){
            fireBalling = true;
            //if moving spawning the fireball on case forward to avoid setting his back on fire
            if (isInDisplacement()){
                new FireBall(getOwnerArea(),orientation,getCurrentMainCellCoordinates().jump(orientation.toVector()));
            } else new FireBall(getOwnerArea(),orientation,getCurrentMainCellCoordinates());
        }
    }
    @Override
    public void draw(Canvas canvas) {
        if (fireBalling){
            switch (orientation){
                case UP -> staffAnimations[0].draw(canvas);
                case RIGHT -> staffAnimations[1].draw(canvas);
                case DOWN -> staffAnimations[2].draw(canvas);
                case LEFT -> staffAnimations[3].draw(canvas);
            }
        } else if (isInDisplacement()){
            switch (orientation){
                case UP -> walkAnimations[0].draw(canvas);
                case RIGHT -> walkAnimations[1].draw(canvas);
                case DOWN -> walkAnimations[2].draw(canvas);
                case LEFT -> walkAnimations[3].draw(canvas);
            }
        } else {
            switch (orientation){
                case DOWN -> idleSprites[0].draw(canvas);
                case RIGHT -> idleSprites[1].draw(canvas);
                case UP -> idleSprites[2].draw(canvas);
                case LEFT -> idleSprites[3].draw(canvas);
            }
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

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        return super.changePosition(newPosition);
    }

    public class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler{
        public void interactWith(Key key, boolean isCellInteraction) {
            inventory.add(key);
            area.unregisterActor(key);
        }
        public void interactWith(Staff staff, boolean isCellInteraction) {
            hasStaff = true;
            area.unregisterActor(staff);
        }
        public void interactWith(Connector connector, boolean isCellInteraction) {
            if (connector.takeCellSpace()){
                for (Key key : inventory){
                    if (key.getId() == connector.getKeyID()){
                        connector.setStats(Connector.ConnectorStats.OPEN);
                        inventory.remove(key);
                        System.out.println("Door opened");
                        return;
                    }
                }
                System.out.println("You need key number "+connector.getKeyID());
            } else {
                if (!isInDisplacement()){
                    changePosition(connector.destinationCoordinates);
                }
            }

        }
    }
}


