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
        Keyboard keyboard = this.getKeyboard();

        if (keyboard.get(Keyboard.O).isPressed()) cheatIfPressed(1);
        if (keyboard.get(Keyboard.L).isPressed()) cheatIfPressed(2);
        if (keyboard.get(Keyboard.T).isPressed()) cheatIfPressed(3);
        if (!isDoorsOpen && logic.isOn()){
            for (Connector connector: connectors){
                connector.openDoor();
                isDoorsOpen = true;
            }
        }

        super.update(deltaTime);
    }


    private void cheatIfPressed(int cheatCode){
        switch (cheatCode){
            case 1:
                for (Connector connector: connectors){
                    connector.setStats(Connector.ConnectorStats.OPEN);
                }
                break;
            case 2:
                Connector connector0 = connectors.get(2);
                connector0.setStats(Connector.ConnectorStats.LOCKED,1);
                break;
            case 3:
                for (Connector connector: connectors){
                    connector.switchDoor();
                }
                break;
        }
    }
    protected List<Connector> getConnector(){
        return new ArrayList<>(connectors);
    }
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
