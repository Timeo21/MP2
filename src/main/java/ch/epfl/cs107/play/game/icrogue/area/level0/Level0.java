package ch.epfl.cs107.play.game.icrogue.area.level0;

import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level0 extends Level {
    private final int KEY_ID1 = 1;
    private final int BOSS_KEY_ID = 2;
    protected final static int[] roomDistribution = new int[]{RoomType.TURRET_ROOM.number,RoomType.SKULL_ROOM.number,RoomType.STAFF_ROOM.number,
            RoomType.BOSS_KEY.number,RoomType.SPAWN.number,RoomType.MEDKIT_ROOM.number,RoomType.SHOP_ROOM.number};
    public Level0(boolean random) {
        super(random,new DiscreteCoordinates(1,0), roomDistribution, 5, 5);
    }
    public Level0(){
        super(true,null,roomDistribution,0,0);
    }

    @Override
    protected void generateFixedMap() {
        generateFinalMap();
    }

    @Override
    protected void generateRandomMap(MapState[][] roomsPlacement) {
        List<DiscreteCoordinates> roomsCoords = new ArrayList<>();
        DiscreteCoordinates bossRoomCoords = null;
        List<Integer> indexList = new ArrayList<>();
        printMap(mapStates);
        for (int i = 0; i < mapStates.length; i++) {
            for (int j = 0; j < mapStates.length; j++) {
                if (mapStates[i][j].equals(MapState.EXPLORED) || mapStates[i][j].equals(MapState.PLACED)) {
                    roomsCoords.add(new DiscreteCoordinates(i,j));
                }
                if (mapStates[i][j].equals(MapState.BOSS_ROOM)) {
                    bossRoomCoords = new DiscreteCoordinates(i,j);
                }
            }
        }

        for (int i = 0; i < roomsCoords.size(); i++) {
            indexList.add(i);
        }


        for (int i = 0; i < roomDistribution.length; i++) {
            List<Integer> index = RandomHelper.chooseKInList(roomDistribution[i], indexList);
            for (Integer Int: index) {
                indexList.remove(Int);
            }
            for (int j : index) {
                switch (i) {
                    case 0 -> setRoom(roomsCoords.get(j), new Level0TurretRoom(roomsCoords.get(j)));
                    case 1 -> setRoom(roomsCoords.get(j), new Level0FlameSkullRoom(roomsCoords.get(j)));
                    case 2 -> setRoom(roomsCoords.get(j), new Level0StaffRoom(roomsCoords.get(j)));
                    case 3 -> setRoom(roomsCoords.get(j), new Level0KeyRoom(roomsCoords.get(j), BOSS_KEY_ID));
                    case 4 -> setRoom(roomsCoords.get(j), new Level0Room(roomsCoords.get(j)));
                    case 5 -> setRoom(roomsCoords.get(j), new Level0MedKitRoom(roomsCoords.get(j)));
                    case 6 -> setRoom(roomsCoords.get(j), new Level0ShopRoom(roomsCoords.get(j)));
                }
                if (i == 4){
                    roomSpawnCoords = new DiscreteCoordinates(roomsCoords.get(j).x,roomsCoords.get(j).y);
                }
                mapStates[roomsCoords.get(j).x][roomsCoords.get(j).y] = MapState.CREATED;
            }
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if(mapStates[i][j].equals(MapState.CREATED)){
                    setUpConnector(roomsPlacement,super.map[i][j]);
                }
                if(mapStates[i][j].equals(MapState.BOSS_ROOM)){
                    setRoom(bossRoomCoords, new Level0FlameSkullRoom(bossRoomCoords));
                    setUpConnector(roomsPlacement,super.map[bossRoomCoords.x][bossRoomCoords.y],BOSS_KEY_ID);
                }
            }
        }
    }

    public String getStartRoomName(){
        return "icrogue/level0"+roomSpawnCoords.x+roomSpawnCoords.y;
    }

    protected enum RoomType{
        TURRET_ROOM(3),
        SKULL_ROOM(2),
        STAFF_ROOM(1),
        BOSS_KEY(1),
        SPAWN(1),
        MEDKIT_ROOM(1),
        SHOP_ROOM(1);
        protected int number;

        RoomType(int number){
            this.number = number;
        }

    }

    @Override
    public String toString() {
        return "icrogue/level0";
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
