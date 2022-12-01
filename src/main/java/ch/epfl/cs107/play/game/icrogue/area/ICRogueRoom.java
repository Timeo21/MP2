package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public abstract class ICRogueRoom extends Area {
    private String behavoirName;
    private ICRogueBehavior behavior;
    private DiscreteCoordinates coordinates;

    public ICRogueRoom(String behavioName, DiscreteCoordinates coordinates){
        this.coordinates = coordinates;
        this.behavoirName = behavioName;
    }
    protected abstract void createArea();

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
}
