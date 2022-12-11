package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
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
        if (player.isChangingRoom){
            switchRoom(player.switchRoomInfo[0]);
        }
        if (getCurrentArea().getKeyboard().get(Keyboard.R).isPressed()){
            initLevel();
        }
        if (getCurrentArea().getKeyboard().get(Keyboard.Y).isPressed()){
            System.out.println("");
        }
        if(level0 != null) level0.update(deltaTime);
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

        level0 = new Level0(8,8,new DiscreteCoordinates(0,0));
        titleMap = level0.addArea(this);
        ICRogueRoom area = (ICRogueRoom) setCurrentArea("icrogue/level000",true);
        DiscreteCoordinates coordinates = area.getPlayerSpawnPosition();
        player = new ICRoguePlayer(area, Orientation.UP,coordinates,"zelda/player");
        area.visite();
        player.enterArea(area,coordinates);
    }

    public void switchRoom(DiscreteCoordinates newRoomCoords){
        player.leaveArea();
        ICRogueRoom area = (ICRogueRoom) setCurrentArea(titleMap[newRoomCoords.x][newRoomCoords.y],false);
        player.enterArea(area,player.switchRoomInfo[1]);
        area.visite();
        player.isChangingRoom = false;
    }

    @Override
    public String getTitle() {
        return "ICRogue";
    }

    public static DiscreteCoordinates getPlayerCoords(){
        return player.getCoords();
    }
}