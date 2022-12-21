package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.FireBall;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DarkLord extends ICRogueActor {
    private Animation[] walkingAnimation;
    private Animation[] shieldAnimation;
    private Animation[] attackAnimation;
    private Orientation orientation;
    private List<Turret> turrets = new ArrayList<>();

    private Phase phase;
    private boolean inDisplacement;
    private static final float PHASE_COOLDOWN = 4f;
    private static final float ATTACK_COOLDOWN = 2f;
    private float phaseTimeWaited;
    private float attackTimeWaited;
    private DiscreteCoordinates position;
    private DiscreteCoordinates destination;
    private enum Phase{
        START,
        ATTACK,
        SHIELD,
        SUMMON;
    }
    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.orientation = orientation;
        life = 10;
        phase = Phase.START;
        this.position = position;

        Sprite[][] sprites = Sprite.extractSprites("zelda/darkLord",3,1.7f,1.7f,this,32,32,new Vector(-0.4f,0),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        walkingAnimation = Animation.createAnimations(5,sprites);

        sprites = Sprite.extractSprites("zelda/darkLord.spell",3,1.7f,1.7f,this,32,32,new Vector(-0.4f,0),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        attackAnimation = Animation.createAnimations(5,sprites,false);

        sprites = Sprite.extractSprites("custom/darkLordShield",3,1.7f,1.7f,this,32,32,new Vector(-0.4f,0),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        shieldAnimation = Animation.createAnimations(5,sprites);

        area.registerActor(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for(Animation animation : walkingAnimation) animation.update(deltaTime);
        for(Animation animation : attackAnimation) animation.update(deltaTime);
        for(Animation animation : shieldAnimation) animation.update(deltaTime);
        List<Turret> copy = new ArrayList<>(turrets);
        for (Turret turret : copy) if (turret.isDead) turrets.remove(turret);
        position = getCurrentMainCellCoordinates();
        switch (phase){
            case START:
                    if (ICRogue.getPlayerCoords().equals(new DiscreteCoordinates(5,8))){
                        changePosition(new DiscreteCoordinates(5,2));
                        orientation = Orientation.LEFT;
                        orientate(orientation);
                    }
                    if (ICRogue.getPlayerCoords().equals(new DiscreteCoordinates(8,5))){
                        changePosition(new DiscreteCoordinates(2,5));
                        orientation = Orientation.RIGHT;
                        orientate(orientation);
                    }
                    if (ICRogue.getPlayerCoords().equals(new DiscreteCoordinates(5,1))){
                        changePosition(new DiscreteCoordinates(5,7));
                        orientation = Orientation.DOWN;
                        orientate(orientation);
                    }
                    if (ICRogue.getPlayerCoords().equals(new DiscreteCoordinates(1,5))){
                        changePosition(new DiscreteCoordinates(7,5));
                        orientation = Orientation.UP;
                        orientate(orientation);
                    }
                    phase = Phase.ATTACK;
                    return;
            case ATTACK:
                break;
            case SHIELD:
        }
        phaseTimeWaited += deltaTime;
        if (phaseTimeWaited > PHASE_COOLDOWN){
            phaseTimeWaited = 0;
            nextPhase();
        }
        attackTimeWaited += deltaTime;
        if (attackTimeWaited > ATTACK_COOLDOWN){
            attackTimeWaited = 0;
            attack();
        }
        if(inDisplacement){
            goToDestination(destination);
        } else {
            randomMove();
        }
    }
    @Override
    public boolean isCellInteractable() {
        return !isDead;
    }

    @Override
    public boolean isViewInteractable() {
        return !isDead;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates(),getCurrentMainCellCoordinates().jump(Orientation.UP.toVector()));
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        switch (phase){
            case SHIELD:
                shieldAnimation[orientation.ordinal()].draw(canvas);
                break;
            default:
                walkingAnimation[orientation.ordinal()].draw(canvas);
        }
    }
    private void  nextPhase(){
        switch (phase){
            case ATTACK -> phase = Phase.SHIELD;
            case SHIELD -> phase = Phase.SUMMON;
            case SUMMON -> phase = Phase.ATTACK;
        }
    }
    public boolean isShielding(){
        return phase.equals(Phase.SHIELD);
    }

    private void moveUp(){
        orientate(Orientation.UP);
        move(15);
    }
    private void moveDown(){
        orientate(Orientation.DOWN);
        move(15);
    }
    private void moveLeft(){
        orientate(Orientation.LEFT);
        move(15);
    }
    private void moveRight(){
        orientate(Orientation.RIGHT);
        move(15);
    }
    private void goToDestination(DiscreteCoordinates destination){
        if (position.x < destination.x){
            moveRight();
        } else if (position.x > destination.x) {
            moveLeft();
        } else if (position.y > destination.y) {
            moveDown();
        } else if (position.y < destination.y) {
            moveUp();
        } else {
            inDisplacement = false;
        }
    }

    private void randomMove(){
        int random = RandomHelper.enemyGenerator.nextInt(7)+1;
        switch (orientation){
            case RIGHT:
            case LEFT:
                destination = new DiscreteCoordinates(position.x,random);
                break;
            case DOWN:
            case UP:
                destination = new DiscreteCoordinates(random, position.y);
        }
        inDisplacement = true;
    }

    private void attack(){
        switch (phase){
            case ATTACK:
                int fireballNumber = RandomHelper.enemyGenerator.nextInt(5)+1;
                for (int i = 0; i < fireballNumber; i++) {
                    int coords = RandomHelper.enemyGenerator.nextInt(8)+1;
                    switch (orientation){
                        case RIGHT:
                            new FireBall(getOwnerArea(),Orientation.RIGHT,new DiscreteCoordinates(position.x, coords),null,10);
                            break;
                        case LEFT:
                            new FireBall(getOwnerArea(),Orientation.LEFT,new DiscreteCoordinates(position.x, coords),null,10);
                            break;
                        case DOWN:
                            new FireBall(getOwnerArea(),Orientation.DOWN,new DiscreteCoordinates(coords, position.y),null,10);
                            break;
                        case UP:
                            new FireBall(getOwnerArea(),Orientation.UP,new DiscreteCoordinates(coords, position.y),null,10);
                    }
                }
                break;
            case SHIELD:
                break;
            case SUMMON:
                int summonNumber = RandomHelper.enemyGenerator.nextInt(4)+1;
                boolean isEmpty = true;
                for (int i = 0; i < summonNumber; i++) {
                    if (turrets.size() < 3){
                        int coordsX = RandomHelper.enemyGenerator.nextInt(8)+1;
                        int coordsY = RandomHelper.enemyGenerator.nextInt(8)+1;
                        for (Turret turret : turrets) {
                            if (turret.getCoords().equals(new DiscreteCoordinates(coordsX,coordsY))) isEmpty = false;
                        }
                        if (isEmpty){
                            turrets.add(new Turret(getOwnerArea(),Orientation.RIGHT,new DiscreteCoordinates(coordsX, coordsY), List.of(Orientation.values())));
                        }
                    }
                }
                break;
        }
    }
}
