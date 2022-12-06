package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;


import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level0Room extends ICRogueRoom {
    public Level0Room(DiscreteCoordinates coordinates) {
        super(Level0Connectors.getAllConnectorsPosition(),Level0Connectors.getAllConnectorsOrientation(),"icrogue/Level0Room", coordinates);
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this,"icrogue/Level0Room"));
        new Staff(this, Orientation.DOWN,new DiscreteCoordinates(4,3));
        new Key(this, Orientation.DOWN,new DiscreteCoordinates(6,3),1);
        super.createArea();
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(2,2);
    }

    public enum Level0Connectors implements ConnectorInRoom{
        E(new DiscreteCoordinates(9, 4), new DiscreteCoordinates(8, 5), Orientation.RIGHT),
        N(new DiscreteCoordinates(4, 9), new DiscreteCoordinates(5, 8), Orientation.UP),
        W(new DiscreteCoordinates(0, 4), new DiscreteCoordinates(1, 5), Orientation.LEFT),
        S(new DiscreteCoordinates(4, 0), new DiscreteCoordinates(5, 1), Orientation.DOWN);

        private DiscreteCoordinates destination;

        Level0Connectors(DiscreteCoordinates position, DiscreteCoordinates destination, Orientation orientation) {
            this.destination = destination;
        }

        @Override
        public int getIndex() {
            return this.ordinal();
        }

        @Override
        public DiscreteCoordinates getDestination() {
            return destination;
        }
        public static List<Orientation> getAllConnectorsOrientation(){
            return List.of(Orientation.RIGHT,Orientation.UP,Orientation.LEFT,Orientation.DOWN);
        }
        public static List<DiscreteCoordinates> getAllConnectorsPosition(){
            return List.of(new DiscreteCoordinates(9, 4),new DiscreteCoordinates(4, 9),new DiscreteCoordinates(0, 4),new DiscreteCoordinates(4, 0));
        }
    }
}
