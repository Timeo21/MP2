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
import ch.epfl.cs107.play.math.*;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor {
    private final Animation[] walkAnimations;
    private final Animation[] staffAnimations;
    private final ICRoguePlayerInteractionHandler playerInteractionHandler;
    private final List<Key> inventory = new ArrayList<>();
    private final static int MOVE_DURATION = 8;
    private boolean fireBalling;
    private boolean hasStaff;
    public int coin;
    private float speedBonus = 1;
    final float COOLDOWN = .5f;
    private float timeWait;
    private Sprite[] idleSprites = new Sprite[4];
    private Orientation orientation;
    private TextGraphics coinText;
    private Sprite[][] lifeSprite;
    private Sprite coinSprite;
    public DiscreteCoordinates[] switchRoomInfo = new DiscreteCoordinates[2];
    public boolean isChangingRoom = false;


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
        coinText = new TextGraphics(Integer.toString(coin),0.6f,Color.BLACK);
        coinText.setAnchor(new Vector(9.44f, 0.37f));

        // Walking Animations
        Sprite[][] animationSprites = Sprite.extractSprites("zelda/player", 4, 0.75f, 1.5f, this, 16, 32, new Vector(0.15f, -0.15f),
                new Orientation[]{Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});

        walkAnimations = Animation.createAnimations(MOVE_DURATION/2, animationSprites);

        // Using Staff Animations
        animationSprites = Sprite.extractSprites("zelda/player.staff_water", 4, 1.5f, 1.5f, this, 32, 32,new Vector(-0.20f, -0.15f),
                new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});

        staffAnimations = Animation.createAnimations(2,animationSprites);


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
            if (timeWait > COOLDOWN){
                fireBalling = false;
                timeWait = 0;
            } else {
                timeWait += deltaTime;
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
        fireBallIfPressed(keyboard.get(Keyboard.G));

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

    private void fireBallIfPressed(Button button){
        // Press X key, has the staff and is not on cooldown
        if(button.isPressed() && hasStaff && !fireBalling){
            fireBalling = true;
            //if moving spawning the fireball on case forward to avoid setting his back on fire
            if (isInDisplacement()){
                new FireBall(getOwnerArea(),orientation,getCurrentMainCellCoordinates().jump(orientation.toVector()),this);
            } else new FireBall(getOwnerArea(),orientation,getCurrentMainCellCoordinates(),this);
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

    public DiscreteCoordinates getCoords(){
        return getCurrentMainCellCoordinates();
    }
    @Override
    public void takeDamage(int damage){
        super.takeDamage(damage);
        System.out.println("Life : "+life);
    }

    private void heal(int heal){
        life += heal;
        if (life > 5) life = 5;
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
            hasStaff = true;
            staff.collect();
        }
        @Override
        public void interactWith(Connector connector, boolean isCellInteraction) {
            if (!isCellInteraction){
                for (Key key : inventory){
                    if (key.getId() == connector.getKeyID()){
                        if (((ICRogueRoom) getOwnerArea()).isDoorsOpen){
                            connector.setStats(Connector.ConnectorStats.OPEN);
                        } else {
                            connector.setStats(Connector.ConnectorStats.CLOSE);
                        }
                        inventory.remove(key);
                        return;
                    }
                }
                System.out.println("You need key number "+connector.getKeyID());
            } else {
                if (!isInDisplacement()){
                    switchRoomInfo[0] = connector.getDestinationRoomCoords();
                    switchRoomInfo[1] = connector.getDestinationCoordinates();
                    isChangingRoom = true;
                }
            }

        }
        @Override
        public void interactWith(Turret turret, boolean isCellInteraction) {
            turret.die();
            coin++;
        }
        @Override
        public void interactWith(FlameSkull flameSkull, boolean isCellInteraction) {
            if (isCellInteraction){
                flameSkull.die();
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


