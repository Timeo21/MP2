package ch.epfl.cs107.play.game.icrogue.area.level0;

import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Arrays;

public class Level0 extends Level {
    private final int KEY_ID1 = 1;
    private final int BOSS_KEY_ID = 2;
    public Level0(int height, int width, DiscreteCoordinates startRoomCoords) {
        super(height, width, startRoomCoords);
    }

    @Override
    protected void generateFixedMap() {
        generateTestMap();
    }

    private void generateTestMap(){
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0Room(room00));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);
        setRoomConnector(room00, "icrogue/level001", Level0Room.Level0Connectors.S);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level0KeyRoom(room10,1));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S);
        setRoomConnector(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2, 0);
        setRoom(room20, new Level0MedKitRoom(room20));
        setRoomConnector(room20, "icrogue/level021", Level0Room.Level0Connectors.S);
        lockRoomConnector(room20, Level0Room.Level0Connectors.E,  12);
        setRoomConnectorDestination(room20, "icrogue/level030", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3, 0);
        setRoom(room30, new Level0TurretRoom(room30));
        setRoomConnector(room30, "icrogue/level020", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room01 = new DiscreteCoordinates(0, 1);
        setRoom(room01, new Level0TurretRoom(room01));
        setRoomConnector(room01, "icrogue/level011", Level0Room.Level0Connectors.E);
        setRoomConnector(room01, "icrogue/level000", Level0Room.Level0Connectors.N);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom(room11, new Level0StaffRoom(room11));
        setRoomConnector(room11, "icrogue/level001", Level0Room.Level0Connectors.W);
        setRoomConnector(room11, "icrogue/level010", Level0Room.Level0Connectors.N);

        lockRoomConnector(room11, Level0Room.Level0Connectors.E,  1);
        setRoomConnectorDestination(room11, "icrogue/level021", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room21 = new DiscreteCoordinates(2, 1);
        setRoom(room21, new Level0ShopRoom(room21));
        setRoomConnector(room21, "icrogue/level031", Level0Room.Level0Connectors.E);
        setRoomConnector(room21, "icrogue/level020", Level0Room.Level0Connectors.N);
        setRoomConnector(room21, "icrogue/level011", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room31 = new DiscreteCoordinates(3, 1);
        setRoom(room31, new Level0FlameSkullRoom(room31));
        setRoomConnector(room31, "icrogue/level041", Level0Room.Level0Connectors.E);
        setRoomConnector(room31, "icrogue/level021", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room41 = new DiscreteCoordinates(4, 1);
        setRoom(room41, new Level0KeyRoom(room41,12));
        setRoomConnector(room41, "icrogue/level031", Level0Room.Level0Connectors.W);

    }

    private void generateFinalMap() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0TurretRoom(room00));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S);
        setRoomConnector(room10, "icrogue/level020", Level0Room.Level0Connectors.E);

        lockRoomConnector(room10, Level0Room.Level0Connectors.W,  BOSS_KEY_ID);
        setRoomConnectorDestination(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level0StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level010", Level0Room.Level0Connectors.W);
        setRoomConnector(room20, "icrogue/level030", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0FlameSkullRoom(room30));
        setRoomConnector(room30, "icrogue/level020", Level0Room.Level0Connectors.W);
        setRoomConnector(room30, "icrogue/level040", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room40 = new DiscreteCoordinates(4,0);
        setRoom(room40, new Level0KeyRoom(room40, BOSS_KEY_ID));
        setRoomConnector(room40, "icrogue/level030", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level0ShopRoom(room11));
        setRoomConnector(room11, "icrogue/level010", Level0Room.Level0Connectors.N);
    }
    public void generateLevel(){
        DiscreteCoordinates room00 = new DiscreteCoordinates(0,0);
        setRoom(room00, new Level0TurretRoom(room00,Arrays.asList(new DiscreteCoordinates(4,4),new DiscreteCoordinates(5,5))));
        setRoomConnector(room00,"icrogue/level010",Level0Room.Level0Connectors.E);
        setRoomConnector(room00,"icrogue/level001",Level0Room.Level0Connectors.S);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0KeyRoom(room10,2));
        setRoomConnector(room10,"icrogue/level000",Level0Room.Level0Connectors.W);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0MedKitRoom(room30));
        setRoomConnector(room30,"icrogue/level031",Level0Room.Level0Connectors.S);

        DiscreteCoordinates room50 = new DiscreteCoordinates(5,0);
        setRoom(room50, new Level0KeyRoom(room50,3));
        setRoomConnector(room50,"icrogue/level051",Level0Room.Level0Connectors.S);

        DiscreteCoordinates room01 = new DiscreteCoordinates(0,1);
        setRoom(room01, new Level0FlameSkullRoom(room01));
        setRoomConnector(room01,"icrogue/level000",Level0Room.Level0Connectors.N);
        setRoomConnector(room01,"icrogue/level011",Level0Room.Level0Connectors.E);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1,1);
        setRoom(room11, new Level0StaffRoom(room11));
        setRoomConnector(room11,"icrogue/level001",Level0Room.Level0Connectors.W);
        setRoomConnector(room11,"icrogue/level021",Level0Room.Level0Connectors.E);
        setRoomConnector(room11,"icrogue/level012",Level0Room.Level0Connectors.S);

        DiscreteCoordinates room21 = new DiscreteCoordinates(2,1);
        setRoom(room21, new Level0TurretRoom(room21));
        setRoomConnector(room21,"icrogue/level011",Level0Room.Level0Connectors.W);
        setRoomConnector(room21,"icrogue/level031",Level0Room.Level0Connectors.E);
        setRoomConnector(room21,"icrogue/level022",Level0Room.Level0Connectors.S);

        DiscreteCoordinates room31 = new DiscreteCoordinates(3,1);
        setRoom(room31, new Level0KeyRoom(room31,1));
        setRoomConnector(room31,"icrogue/level021",Level0Room.Level0Connectors.W);
        lockRoomConnector(room31, Level0Room.Level0Connectors.N,3);
        setRoomConnectorDestination(room31,"icrogue/level030",Level0Room.Level0Connectors.N);

        DiscreteCoordinates room51 = new DiscreteCoordinates(5,1);
        setRoom(room51, new Level0TurretRoom(room51));
        setRoomConnector(room51,"icrogue/level050",Level0Room.Level0Connectors.N);
        setRoomConnector(room51,"icrogue/level052",Level0Room.Level0Connectors.S);

        DiscreteCoordinates room02 = new DiscreteCoordinates(0,2);
        setRoom(room02, new Level0TurretRoom(room02));
        setRoomConnector(room02,"icrogue/level012",Level0Room.Level0Connectors.E);
        lockRoomConnector(room02, Level0Room.Level0Connectors.S,1);
        setRoomConnectorDestination(room02,"icrogue/level003", Level0Room.Level0Connectors.S);

        DiscreteCoordinates room12 = new DiscreteCoordinates(1,2);
        setRoom(room12, new Level0FlameSkullRoom(room12));
        setRoomConnector(room12,"icrogue/level011",Level0Room.Level0Connectors.N);
        setRoomConnector(room12,"icrogue/level002",Level0Room.Level0Connectors.W);
        setRoomConnector(room12,"icrogue/level013",Level0Room.Level0Connectors.S);

        DiscreteCoordinates room22 = new DiscreteCoordinates(2,2);
        setRoom(room22, new Level0Room(room22));
        setRoomConnector(room22,"icrogue/level021",Level0Room.Level0Connectors.N);
        setRoomConnector(room22,"icrogue/level032",Level0Room.Level0Connectors.E);

        DiscreteCoordinates room32 = new DiscreteCoordinates(3,2);
        setRoom(room32, new Level0TurretRoom(room32));
        setRoomConnector(room32,"icrogue/level022",Level0Room.Level0Connectors.W);
        setRoomConnector(room32,"icrogue/level042",Level0Room.Level0Connectors.E);
        setRoomConnector(room32,"icrogue/level033",Level0Room.Level0Connectors.S);

        DiscreteCoordinates room42 = new DiscreteCoordinates(4,2);
        setRoom(room42, new Level0StaffRoom(room42));
        setRoomConnector(room42,"icrogue/level032",Level0Room.Level0Connectors.W);
        setRoomConnector(room42,"icrogue/level052",Level0Room.Level0Connectors.E);

        DiscreteCoordinates room52 = new DiscreteCoordinates(5,2);
        setRoom(room52, new Level0FlameSkullRoom(room52));
        setRoomConnector(room52,"icrogue/level051",Level0Room.Level0Connectors.N);
        setRoomConnector(room52,"icrogue/level042",Level0Room.Level0Connectors.W);

        DiscreteCoordinates room03 = new DiscreteCoordinates(0,3);
        setRoom(room03, new Level0FlameSkullRoom(room03));
        setRoomConnector(room03,"icrogue/level002",Level0Room.Level0Connectors.N);
        lockRoomConnector(room03, Level0Room.Level0Connectors.S,2);
        setRoomConnectorDestination(room03,"icrogue/level04", Level0Room.Level0Connectors.S);

        DiscreteCoordinates room13 = new DiscreteCoordinates(1,3);
        setRoom(room13, new Level0TurretRoom(room13));
        setRoomConnector(room13,"icrogue/level012",Level0Room.Level0Connectors.N);
        lockRoomConnector(room13, Level0Room.Level0Connectors.E,12);
        setRoomConnectorDestination(room13,"icrogue/level023", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room23 = new DiscreteCoordinates(2,3);
        setRoom(room23, new Level0TurretRoom(room23, Arrays.asList(new DiscreteCoordinates(4,5),new DiscreteCoordinates(5,4),new DiscreteCoordinates(1,8),new DiscreteCoordinates(8,1))));
        setRoomConnector(room23,"icrogue/level013",Level0Room.Level0Connectors.E);

        DiscreteCoordinates room33 = new DiscreteCoordinates(3,3);
        setRoom(room33, new Level0ShopRoom(room33));
        setRoomConnector(room33,"icrogue/level032",Level0Room.Level0Connectors.N);

        DiscreteCoordinates room04 = new DiscreteCoordinates(0,4);
        setRoom(room04, new Level0KeyRoom(room04,12));
        setRoomConnector(room04,"icrogue/level003",Level0Room.Level0Connectors.N);
    }
}
