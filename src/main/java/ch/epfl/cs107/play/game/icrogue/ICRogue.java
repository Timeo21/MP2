package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame {
    private Window window;
    private FileSystem fileSystem;
    private String[][] titleMap;
    private Level level0;

    public final static float CAMERA_SCALE_FACTOR = 10f;

    private static ICRoguePlayer player;
    private static ICRogueRoom currentArea;

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
        if(player.isDead){
            System.out.println("Game Over");
            initLevel();
        }
        if (player.isChangingRoom()){
            switchRoom(player.getSwitchRoomInfo()[0]);
        }
        if (getCurrentArea().getKeyboard().get(Keyboard.R).isPressed()){
            initLevel();
        }
        if (getCurrentArea().getKeyboard().get(Keyboard.Y).isPressed()){
        }
        if(level0 != null) level0.update(deltaTime);
        super.update(deltaTime);
    }

    /**
     * Initialize the first level
     * Create the player
     * Start the game
     */
    private void initLevel(){

        level0 = new Level0(true);
        titleMap = level0.addArea(this);
        currentArea = (ICRogueRoom) setCurrentArea(level0.getStartRoomName(),true);
        DiscreteCoordinates coordinates = currentArea.getPlayerSpawnPosition();
        player = new ICRoguePlayer(currentArea, Orientation.UP,coordinates,"zelda/player");
        currentArea.visite();
        player.enterArea(currentArea,coordinates);
    }

    /**
     * Make the player change room
     * @param newRoomCoords (Coordinate) : Coordinate of the destination room
     */
    public void switchRoom(DiscreteCoordinates newRoomCoords){
        player.leaveArea();
        ICRogueRoom area = (ICRogueRoom) setCurrentArea(titleMap[newRoomCoords.x][newRoomCoords.y],false);
        player.enterArea(area,player.getSwitchRoomInfo()[1]);
        area.visite();
        player.changingRoom(false);
    }

    @Override
    public String getTitle() {
        return "ICRogue";
    }
    public static String getCurrentAreaName(){
        return currentArea.getTitle();
    }

    /**
     * Get the coordinate of the player
     * @return (Coordinate) : Coordinate of the player
     */
    public static DiscreteCoordinates getPlayerCoords(){
        return player.getCoords();
    }
}