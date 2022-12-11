package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Connector extends AreaEntity {
    private static final int NO_KEY_ID = 0;
    private int keyID;
    private ConnectorStats stats;
    private String destinationAreaName;
    private DiscreteCoordinates destinationCoordinates;
    private Sprite[] sprites = new Sprite[4];
    private boolean takeSpace;
    public enum ConnectorStats{
        OPEN,
        CLOSE,
        LOCKED,
        INVISIBLE,
    }
    public Connector(Area area, DiscreteCoordinates coordinates, Orientation orientation, ConnectorStats stats, String destinationAreaName, DiscreteCoordinates destinationCoordinates){
        this(area,coordinates,orientation,stats,destinationAreaName,destinationCoordinates,NO_KEY_ID);
    }
    public Connector(Area area, DiscreteCoordinates coordinates, Orientation orientation, ConnectorStats stats, String destinationAreaName, DiscreteCoordinates destinationCoordinates, int keyID){
        super(area,orientation,coordinates);
        this.keyID = keyID;
        this.stats = stats;
        this.destinationAreaName = destinationAreaName;
        this.destinationCoordinates = destinationCoordinates;
        takeSpace = true;
        orientation = orientation.opposite();
        sprites[0] = null;
        sprites[1] = new Sprite("icrogue/door_"+orientation.ordinal(),(orientation.ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
        sprites[2] = new Sprite("icrogue/lockedDoor_"+orientation.ordinal(),(orientation.opposite().ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
        sprites[3] = new Sprite("icrogue/invisibleDoor_"+orientation.ordinal(),(orientation.opposite().ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
    }

    @Override
    public boolean takeCellSpace() {
        return takeSpace;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return stats.equals(ConnectorStats.LOCKED);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!stats.equals(ConnectorStats.OPEN)){
            sprites[stats.ordinal()].draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public int getKeyID(){
        return keyID;
    }
    public void setKeyID(int keyID){
        this.keyID = keyID;
    }

    public void setDestinationAreaName(String destinationAreaName){
        this.destinationAreaName = destinationAreaName;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coordinates = getCurrentMainCellCoordinates();
        return List.of(coordinates , coordinates.jump(new Vector((getOrientation().ordinal()+1)%2, getOrientation().ordinal()%2)));
    }
    public void setStats(ConnectorStats stats){
            setStats(stats,0);
    }
    public void setStats(ConnectorStats stats,int keyID){
        this.stats = stats;
        this.takeSpace = !stats.equals(ConnectorStats.OPEN);
        this.keyID = keyID;
    }

    public void switchDoor(){
        if (stats.equals(ConnectorStats.CLOSE)){
            setStats(ConnectorStats.OPEN);
        } else if (stats.equals(ConnectorStats.OPEN)) {
            setStats(ConnectorStats.CLOSE);
        }
    }

    public void openDoor(){
        if (stats.equals(ConnectorStats.CLOSE)){
            setStats(ConnectorStats.OPEN);
        }
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this,isCellInteraction);
    }
    public DiscreteCoordinates getDestinationCoordinates(){
        return new DiscreteCoordinates(destinationCoordinates.x,destinationCoordinates.y);
    }

    public DiscreteCoordinates getDestinationRoomCoords(){
        return new DiscreteCoordinates(Integer.parseInt(String.valueOf(destinationAreaName.charAt(14))),Integer.parseInt(String.valueOf(destinationAreaName.charAt(15))));
    }
}
