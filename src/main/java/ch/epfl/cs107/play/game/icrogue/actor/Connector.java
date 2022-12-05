package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Connector extends ICRogueActor{
    private static final int NO_KEY_ID = 0;
    private int keyID;
    private ConnectorStats stats;
    private String destinationAreaName;
    public DiscreteCoordinates destinationCoordinates;
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
        this.takeSpace = true;
        sprites[0] = null;
        sprites[1] = new Sprite("icrogue/door_"+orientation.ordinal(),(orientation.ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
        sprites[2] = new Sprite("icrogue/lockedDoor_"+orientation.ordinal(),(orientation.ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
        sprites[3] = new Sprite("icrogue/invisibleDoor_"+orientation.ordinal(),(orientation.ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
    }

    @Override
    public boolean takeCellSpace() {
        return takeSpace;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!stats.equals(ConnectorStats.OPEN)){
            sprites[stats.ordinal()].draw(canvas);
        }
        super.draw(canvas);
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
}