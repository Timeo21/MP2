package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area implements Logic {
    private String behavoirName;
    private ICRogueBehavior behavior;
    protected DiscreteCoordinates roomCoordinates;
    private List<DiscreteCoordinates> positions;
    private List<Orientation> orientations;
    private List<Connector> connectors = new ArrayList<>();
    private boolean isVisited;
    protected Logic logic;
    public boolean isDoorsOpen;

    public ICRogueRoom(List<DiscreteCoordinates> connectorsCoordinates, List<Orientation> orientations, String behaviorName, DiscreteCoordinates roomCoordinates){
        this.roomCoordinates = roomCoordinates;
        this.behavoirName = behaviorName;
        this.positions = connectorsCoordinates;
        this.orientations = orientations;
        isVisited = false;
        logic = TRUE;
        connectors.add(new Connector(this, positions.get(0),orientations.get(0), Connector.ConnectorStats.INVISIBLE,"", Level0Room.Level0Connectors.W.getDestination()));
        connectors.add(new Connector(this, positions.get(1),orientations.get(1), Connector.ConnectorStats.INVISIBLE,"", Level0Room.Level0Connectors.S.getDestination()));
        connectors.add(new Connector(this, positions.get(2),orientations.get(2), Connector.ConnectorStats.INVISIBLE,"", Level0Room.Level0Connectors.E.getDestination()));
        connectors.add(new Connector(this, positions.get(3),orientations.get(3), Connector.ConnectorStats.INVISIBLE,"", Level0Room.Level0Connectors.N.getDestination()));
    }
    protected void createArea(){
        for (Connector connector : connectors) {
            registerActor(connector);
        }
    }

    @Override
    public String getTitle() {
        return "icrogue/level0"+ roomCoordinates.x+ roomCoordinates.y;
    }

    /**
     * @return (float): camera scale factor, assume it is the same in x and y direction
     */
    @Override
    public float getCameraScaleFactor() {
        return ICRogue.CAMERA_SCALE_FACTOR;
    }

    /**
     * Get the player spawn position
     * @return (Coordinate) : Coordinate of spawn in the starting room
     */
    public abstract DiscreteCoordinates getPlayerSpawnPosition();

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window,fileSystem)){
            behavior = new ICRogueBehavior(window,behavoirName);
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        if (!isDoorsOpen && logic.isOn()){
            for (Connector connector: connectors){
                connector.openDoor();
                isDoorsOpen = true;
            }
        }
        super.update(deltaTime);
    }

    /**
     * Set a connector to a close stat
     * @param connectorIndex (int) : index of the connector to change
     */
    public void setCloseConnector(int connectorIndex){
        connectors.get(connectorIndex).setStats(Connector.ConnectorStats.CLOSE);
    }

    /**
     * Set a connector's destination
     * @param destination (String) : Name of the destination room
     * @param connectorIndex (int) : Index of the connector to change
     */
    public void setConnectorDestination(String destination,int connectorIndex){
        connectors.get(connectorIndex).setDestinationAreaName(destination);
    }

    /**
     * Set a connector to a locked stat
     * @param connectorIndex (int) : Index of the connector to change
     * @param keyID (int) : ID of the key required to open it
     */
    public void setLockedConnector(int connectorIndex, int keyID){
        connectors.get(connectorIndex).setStats(Connector.ConnectorStats.LOCKED,keyID);
    }

    /**
     * Make room visite by the player
     */
    public void visite(){
        if (!this.isVisited) {
            this.isVisited = true;
        }
    }

    @Override
    public boolean isOn() {
        return logic.isOn();
    }
    @Override
    public boolean isOff() {
        return logic.isOff();
    }
    @Override
    public float getIntensity() {
        return logic.getIntensity();
    }


}
