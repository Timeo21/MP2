package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame {
    private Window window;
    private FileSystem fileSystem;

    public final static float CAMERA_SCALE_FACTOR = 11f;

    private ICRoguePlayer player;

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if ( super.begin(window,fileSystem)){
            this.window = window;
            this.fileSystem = fileSystem;
            initLevel();
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        if (getCurrentArea().getKeyboard().get(Keyboard.R).isPressed()){
            initLevel();
        }
        super.update(deltaTime);
    }

    private void initLevel(){

        /*
        addArea(new Level0Room(new DiscreteCoordinates(0,0)));
        ICRogueRoom area = (ICRogueRoom)setCurrentArea("icrogue/level000",true);
        DiscreteCoordinates coordinates = area.getPlayerSpawnPosition();
        player = new ICRoguePlayer(area, Orientation.UP,coordinates,"zelda/player");
        player.enterArea(area,coordinates);
         */

        Level0 level0 = new Level0(4,2,new DiscreteCoordinates(0,0),this);
        ICRogueRoom area = (ICRogueRoom) setCurrentArea("icrogue/level010",true);
        DiscreteCoordinates coordinates = area.getPlayerSpawnPosition();
        player = new ICRoguePlayer(area, Orientation.UP,coordinates,"zelda/player");
        player.enterArea(area,coordinates);
    }

    @Override
    public String getTitle() {
        return "ICRogue";
    }
}
