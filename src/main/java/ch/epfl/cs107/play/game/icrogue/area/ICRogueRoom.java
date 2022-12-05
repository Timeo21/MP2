package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area {
    private String behavoirName;
    private ICRogueBehavior behavior;
    protected DiscreteCoordinates roomCoordinates;
    private List<DiscreteCoordinates> positions;
    private List<Orientation> orientations;
    private List<Connector> connectors = new ArrayList<>();

    public ICRogueRoom(List<DiscreteCoordinates> connectorsCoordinates, List<Orientation> orientations, String behaviorName, DiscreteCoordinates roomCoordinates){
        this.roomCoordinates = roomCoordinates;
        this.behavoirName = behaviorName;
        this.positions = connectorsCoordinates;
        this.orientations = orientations;
        connectors.add(new Connector(this, positions.get(0),orientations.get(0), Connector.ConnectorStats.INVISIBLE,"level0", Level0Room.Level0Connectors.W.getDestination()));
        connectors.add(new Connector(this, positions.get(1),orientations.get(1), Connector.ConnectorStats.INVISIBLE,"level0", Level0Room.Level0Connectors.S.getDestination()));
        connectors.add(new Connector(this, positions.get(2),orientations.get(2), Connector.ConnectorStats.INVISIBLE,"level0", Level0Room.Level0Connectors.E.getDestination()));
        connectors.add(new Connector(this, positions.get(3),orientations.get(3), Connector.ConnectorStats.INVISIBLE,"level0", Level0Room.Level0Connectors.N.getDestination()));
        /*

        new Connector(this, positions.get(2),orientations.get(2), Connector.ConnectorStats.CLOSE,"level0", Level0Room.Level0Connectors.E.getDestination());
        new Connector(this, positions.get(3),orientations.get(3), Connector.ConnectorStats.OPEN,"level0", Level0Room.Level0Connectors.N.getDestination());
        */
    }
    protected void createArea(){
        for (Connector connector : connectors) {
            registerActor(connector);
        }
    }

    /**
     * @return (float): camera scale factor, assume it is the same in x and y direction
     */
    @Override
    public float getCameraScaleFactor() {
        return ICRogue.CAMERA_SCALE_FACTOR;
    }

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
        super.update(deltaTime);
    }
    public List<Connector> getConnectors(){
        return connectors;
    }
}
