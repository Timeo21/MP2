package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;


import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0Room extends ICRogueRoom {
    private DiscreteCoordinates coordinates;

    public Level0Room(DiscreteCoordinates coordinates) {
        super("icrogue/Level0Room", coordinates);
        this.coordinates = coordinates;
    }

    @Override
    public String getTitle() {
        return "icrogue/level0"+coordinates.x+coordinates.y;
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this,"icrogue/Level0Room"));
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(4,4);
    }
}
