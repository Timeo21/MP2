package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame {

    public final static float CAMERA_SCALE_FACTOR = 11f;

    private ICRoguePlayer player;

    private void createAreas(){
        addArea(new Level0Room(new DiscreteCoordinates(0,0)));
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if ( super.begin(window,fileSystem)){
            createAreas();
            initLevel();
            return true;
        }
        return false;
    }

    private void initLevel(){
        ICRogueRoom area = (ICRogueRoom)setCurrentArea("icrogue/level000",true);
        DiscreteCoordinates coordinates = area.getPlayerSpawnPosition();
        player = new ICRoguePlayer(area, Orientation.DOWN,coordinates,"ghost.1");
        player.enterArea(area,coordinates);
    }

    @Override
    public String getTitle() {
        return "ICRogue";
    }
}
