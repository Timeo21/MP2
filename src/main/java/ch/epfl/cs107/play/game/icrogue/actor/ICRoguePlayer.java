package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.FlameSkull;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.MedKit;
import ch.epfl.cs107.play.game.icrogue.actor.items.SpeedShoes;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.FireBall;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor {
    private final Animation[] walkAnimations;
    private final Animation[] staffAnimations;
    private final Animation[] swordAnimations;
    private final ICRoguePlayerInteractionHandler playerInteractionHandler;
    private final List<Key> inventory = new ArrayList<>();
    private final static int MOVE_DURATION = 8;
    private boolean fireBalling;
    private boolean isDamage;
    private boolean hasStaff = true;
    private boolean attacking;
    private int coin;
    private boolean godMode;
    private float speedBonus = 1;
    private final static float COOLDOWN = .5f;
    private float immunityTime;
    private final static float IMMUNITY_DURA = .5f;
    private float fireBallCooldown;
    private Sprite[] idleSprites = new Sprite[4];
    private Orientation orientation;
    private TextGraphics coinText;
    private Sprite[][] lifeSprite;
    private Sprite coinSprite;
    private boolean itemInteraction;
    private boolean canAttack;

    private DiscreteCoordinates[] switchRoomInfo = new DiscreteCoordinates[2];
    private boolean isChangingRoom = false;

    /**
     * Get the information required for switching room
     * @return (Coordinate[]) : List containing the coordinates of the destination room and in the room
     */
    public DiscreteCoordinates[] getSwitchRoomInfo() {
        return switchRoomInfo;
    }

    /**
     * get if the player is switching rooom
     * @return (boolean) : the player is changing room
     */
    public boolean isChangingRoom() {
        return isChangingRoom;
    }

    /**
     * Set if the player is changing room
     * @param isChangingRoom (boolean) : the player is changing room
     */
    public void changingRoom(boolean isChangingRoom) {
        this.isChangingRoom = isChangingRoom;
    }


    /**
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public ICRoguePlayer(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position);
        this.orientation = orientation;
        this.playerInteractionHandler = new ICRoguePlayerInteractionHandler();
        life = 5;
        coinText = new TextGraphics(Integer.toString(coin),0.6f, Color.BLACK);
        coinText.setAnchor(new Vector(9.44f, 0.37f));

        // Walking Animations
        Sprite[][] animationSprites = Sprite.extractSprites("zelda/player", 4, 0.75f, 1.5f, this, 16, 32, new Vector(0.15f, -0.15f),
                new Orientation[]{Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});

        walkAnimations = Animation.createAnimations(MOVE_DURATION/2, animationSprites);

        // Using Staff Animations
        animationSprites = Sprite.extractSprites("zelda/player.staff_water", 4, 1.5f, 1.5f, this, 32, 32,new Vector(-0.20f, -0.15f),
                new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});

        staffAnimations = Animation.createAnimations(2,animationSprites);

        // Using Sword Animations
        animationSprites = Sprite.extractSprites("zelda/player.sword", 4, 1.5f, 1.5f, this, 32, 32,new Vector(-0.20f, -0.15f),
                new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});

        swordAnimations = Animation.createAnimations(2,animationSprites,false);


        lifeSprite = Sprite.extractSprites("custom/heart",5,1,1, null,16,16,new Vector(-.05f,0),
                new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});

        coinSprite = new Sprite("custom/coin",.8f,.8f,null);
        coinSprite.setAnchor(new Vector(9.125f,0.1f));

        // Idle sprite
        for (int i = 0; i < 4; i++) {
            idleSprites[i] = new Sprite(spriteName,.75f,1.5f,this, new RegionOfInterest(0,i*32,16,32),new Vector(0.15f, -0.15f));
        }

        resetMotion();
    }

    @Override
    public void update(float deltaTime) {
        canAttack = !fireBalling && !attacking;
        if (coin > 9) {
            coinText.setText("9+");
            coinText.setAnchor(new Vector(9.35f, 0.37f));
        } else {
            coinText.setText(Integer.toString(coin));
            if (coin == 1) {
                coinText.setAnchor(new Vector(9.47f, 0.37f));
            } else {
                coinText.setAnchor(new Vector(9.44f, 0.37f));
            }
        }
        // Shooting fireball cooldown
        if (fireBalling){
            if (fireBallCooldown > COOLDOWN){
                fireBalling = false;
                fireBallCooldown = 0;
            } else {
                fireBallCooldown += deltaTime;
            }
        }
        immunityTime += deltaTime;
        if (isDamage && immunityTime > IMMUNITY_DURA){
            isDamage = false;
        }

        if(attacking){
            if (swordAnimations[orientation.ordinal()].isCompleted()){
                attacking = false;
            }
        }
        for (Animation animation : walkAnimations) animation.update(deltaTime);
        for (Animation animation : staffAnimations) animation.update(deltaTime);
        for (Animation animation : swordAnimations) animation.update(deltaTime);
        itemInteraction = getOwnerArea().getKeyboard().get(Keyboard.W).isPressed();
        Keyboard keyboard= getOwnerArea().getKeyboard();
        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        fireBallIfPressed(keyboard.get(Keyboard.X));
        godMode(keyboard.get(Keyboard.TAB));
        attackIfPressed(keyboard.get(Keyboard.SPACE));

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
                move((int) (MOVE_DURATION/speedBonus));
            }
        }
    }

    private void godMode(Button button){
        if(button.isPressed()){
            godMode = !godMode;
        }
    }

    /**
     * Make the player attacking with sword if pressed
     * @param button (Button) : button corresponding to attacking
     */
    private void attackIfPressed(Button button){
        if(button.isPressed() && canAttack){
            swordAnimations[orientation.ordinal()].reset();
            attacking = true;
        }
    }

    /**
     * Make the player shooting fireball if pressed
     * @param button (Button) : button corresponding to launching fireball
     */
    private void fireBallIfPressed(Button button){
        // Press X key, has the staff and is not on cooldown
        if(button.isPressed() && hasStaff && canAttack){
            fireBalling = true;
            //if moving spawning the fireball on case forward to avoid setting his back on fire
            if (isInDisplacement()){
                new FireBall(getOwnerArea(),orientation,getCurrentMainCellCoordinates().jump(orientation.toVector()),this,5);
            } else new FireBall(getOwnerArea(),orientation,getCurrentMainCellCoordinates(),this,5);
        }
    }
    @Override
    public void draw(Canvas canvas) {
        coinSprite.draw(canvas);
        if (!isDead){
            lifeSprite[2][5-life].draw(canvas);
            coinText.draw(canvas);
        }
        if (fireBalling){
            staffAnimations[orientation.ordinal()].draw(canvas);

        } else if (attacking){
            swordAnimations[orientation.ordinal()].draw(canvas);

        } else if (isInDisplacement()){
            walkAnimations[orientation.ordinal()].draw(canvas);
        } else {
            switch (orientation){
                case DOWN -> idleSprites[0].draw(canvas);
                case RIGHT -> idleSprites[1].draw(canvas);
                case UP -> idleSprites[2].draw(canvas);
                case LEFT -> idleSprites[3].draw(canvas);
            }
        }
    }

    /**
     * Make the player enter a new area
     * @param area (Area) : New area to enter
     * @param coordinates (Coordinate) : Coordinate to set the player entering the area
     */
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

        return true;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(playerInteractionHandler,isCellInteraction);
    }

    /**
     * Get the coordinate of the player
     * @return (Coordinate) : Coordinate of the player
     */
    public DiscreteCoordinates getCoords(){
        return getCurrentMainCellCoordinates();
    }

    /**
     * Give the player a certain amount of life back
     * @param heal (int) : amount to heal
     */
    private void heal(int heal){
        life += heal;
        if (life > 5) life = 5;
    }

    /**
     * Give a certain amount of coin to the player
     * @param coin (int) : amount of coin to give
     */
    public void addCoin(int coin){
        this.coin += coin;
    }

    @Override
    public void takeDamage(int damage) {
        if (godMode) return;
        if (!isDamage){
            super.takeDamage(damage);
            immunityTime = 0;
            isDamage = true;
        }
    }

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        return super.changePosition(newPosition);
    }

    public class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler{
        @Override
        public void interactWith(Key key, boolean isCellInteraction) {
            inventory.add(key);
            key.collect();
        }
        @Override
        public void interactWith(Staff staff, boolean isCellInteraction) {
            if (itemInteraction){
                hasStaff = true;
                staff.collect();
            }
        }
        @Override
        public void interactWith(Connector connector, boolean isCellInteraction) {
            if (!isCellInteraction && itemInteraction){
                for (Key key : inventory){
                    if (key.getId() == connector.getKeyID()){
                        if (((ICRogueRoom) getOwnerArea()).isDoorsOpen){
                            connector.setStats(Connector.ConnectorStats.OPEN);
                        } else {
                            connector.setStats(Connector.ConnectorStats.CLOSE);
                        }
                        return;
                    }
                }
                System.out.println("You need key number "+connector.getKeyID());
            } else {
                if (isCellInteraction && !isInDisplacement()){
                    switchRoomInfo[0] = connector.getDestinationRoomCoords();
                    switchRoomInfo[1] = connector.getDestinationCoordinates();
                    isChangingRoom = true;
                }
            }

        }
        @Override
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if(isCellInteraction || attacking){
                turret.die();
                coin++;
            }
        }
        @Override
        public void interactWith(FlameSkull flameSkull, boolean isCellInteraction) {
                if (attacking && flameSkull.getOrientation().opposite().equals(orientation) && !flameSkull.isDamage){
                    flameSkull.takeDamage(1);
                    addCoin(1);
                } else if (isCellInteraction) {
                    flameSkull.takeDamage(1);
                    takeDamage(1);
                }
        }

        @Override
        public void interactWith(SpeedShoes speedShoes, boolean isCellInteraction) {
            if (coin >= speedShoes.price){
                speedShoes.collect();
                speedBonus += 0.1;
                coin -= speedShoes.price;
            } else {
                System.out.println("no money");
            }
        }

        @Override
        public void interactWith(MedKit medKit, boolean isCellInteraction) {
            if (isCellInteraction){
                medKit.collect();
                heal(1);
            } else if (coin >= medKit.price){
                medKit.collect();
                coin -= medKit.price;
                heal(1);
            } else {
                System.out.println("no money");
            }
        }
    }
}