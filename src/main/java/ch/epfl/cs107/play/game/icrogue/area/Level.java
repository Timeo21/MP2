package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.cs107.play.signal.logic.Logic.FALSE;
import static ch.epfl.cs107.play.signal.logic.Logic.TRUE;

public abstract class Level {
    protected ICRogueRoom[][] map;
    DiscreteCoordinates roomSpawnCoords;
    DiscreteCoordinates bossRoomCoordinate;
    String startRoomName;
    private Connector connector;
    int height;
    int width;
    private Logic logic = FALSE;
    private int[] roomsDistribution;
    protected MapState[][] mapStates;
    static int roomsToPlace;
    public Level(boolean randomMap, DiscreteCoordinates startRoomCoords, int[] roomsDistribution, int width, int height){
        this(randomMap,startRoomCoords,roomsDistribution,width,height,new DiscreteCoordinates(2,3));
    }

    public Level(boolean randomMap, DiscreteCoordinates startRoomCoords, int[] roomsDistribution, int width, int height, DiscreteCoordinates bossRoomCoordinate){
        this.width = width;
        this.height = height;
        this.bossRoomCoordinate = bossRoomCoordinate;
        this.roomSpawnCoords = startRoomCoords;
        this.roomsDistribution = roomsDistribution;
        if (randomMap){
            int nbRooms = 0;
            for (int i : roomsDistribution){
                nbRooms += i;
            }
            this.height = nbRooms;
            this.width = nbRooms;
            roomsToPlace = nbRooms;
            map = new ICRogueRoom[nbRooms][nbRooms];
            mapStates = generateRandomRoomPlacement();
            printMap(mapStates);
            generateRandomMap(mapStates);
        } else {
            map = new ICRogueRoom[width][height];
            generateFixedMap();
        }
    }
    protected MapState[][] generateRandomRoomPlacement() {
        List<DiscreteCoordinates> freeSlotsPositions = new ArrayList<>();
        int roomToAdd;
        // 1.
        MapState[][] mapStates = new MapState[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                mapStates[i][j] = MapState.NULL;
            }
        }
        // 2.
        mapStates[width / 2][height / 2] = MapState.PLACED;

        // 3.
        while (roomsToPlace > 0) {
            boolean addNewRoom = false;
            //(a)
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (mapStates[i][j].equals(MapState.PLACED)){
                        addNewRoom = false;
                        if (i-1 >= 0){
                            if(mapStates[i-1][j].equals(MapState.NULL)){
                                freeSlotsPositions.add(new DiscreteCoordinates(i-1,j));
                                addNewRoom = true;
                            }
                        }
                        if (i+1 <= height){
                            if(mapStates[i+1][j].equals(MapState.NULL)){
                                freeSlotsPositions.add(new DiscreteCoordinates(i+1,j));
                                addNewRoom = true;
                            }
                        }
                        if (j-1 >= 0){
                            if(mapStates[i][j-1].equals(MapState.NULL)){
                                freeSlotsPositions.add(new DiscreteCoordinates(i,j-1));
                                addNewRoom = true;
                            }
                        }
                        if (j+1 <= width){
                            if(mapStates[i][j+1].equals(MapState.NULL)){
                                freeSlotsPositions.add(new DiscreteCoordinates(i,j+1));
                                addNewRoom = true;
                            }
                        }
                        // (d)
                        if (addNewRoom){
                            mapStates[i][j] = MapState.EXPLORED;
                            System.out.println("explored");
                        }
                    }
                }
            }
            if (addNewRoom){

                // (b)
                int toPlaceNow = RandomHelper.roomGenerator.nextInt(0, Math.min(roomsToPlace, freeSlotsPositions.size()))+1;
                // (c)
                for (int k = 0; k < toPlaceNow; k++) {
                    roomsToPlace--;
                    roomToAdd = RandomHelper.roomGenerator.nextInt(0, freeSlotsPositions.size());
                    mapStates[freeSlotsPositions.get(roomToAdd).x][freeSlotsPositions.get(roomToAdd).y] = MapState.PLACED;
                    freeSlotsPositions.remove(roomToAdd);
                }
                freeSlotsPositions.clear();
            }
        }

        // 4.
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (mapStates[i][j].equals(MapState.PLACED)){
                    if (i-1 >= 0){
                        if(mapStates[i-1][j].equals(MapState.NULL)){
                            freeSlotsPositions.add(new DiscreteCoordinates(i-1,j));
                        }
                    }
                    if (i+1 <= height){
                        if(mapStates[i+1][j].equals(MapState.NULL)){
                            freeSlotsPositions.add(new DiscreteCoordinates(i+1,j));
                        }
                    }
                    if (j-1 >= 0){
                        if(mapStates[i][j-1].equals(MapState.NULL)){
                            freeSlotsPositions.add(new DiscreteCoordinates(i,j-1));
                        }
                    }
                    if (j+1 <= width){
                        if(mapStates[i][j+1].equals(MapState.NULL)){
                            freeSlotsPositions.add(new DiscreteCoordinates(i,j+1));
                        }
                    }
                }
            }
        }
        roomToAdd = RandomHelper.roomGenerator.nextInt(0, freeSlotsPositions.size());
        mapStates[freeSlotsPositions.get(roomToAdd).x][freeSlotsPositions.get(roomToAdd).y] = MapState.BOSS_ROOM;

        return mapStates;
    }

    private void printMap(MapState [][] map) {
        System.out.println("Generated map:");
        System.out.print(" | ");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        System.out.print("--|-");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print("--");
        }
        System.out.println();
        for (int i = 0; i < map.length; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[j][i] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    protected enum MapState{
        NULL,
        PLACED,
        EXPLORED,
        BOSS_ROOM,
        CREATED;
        @Override
        public String toString(){
            return Integer.toString(ordinal());
        }
    }

    protected abstract void generateFixedMap();
    protected abstract void generateRandomMap(MapState[][] mapStates);



    protected abstract void setUpConnector(MapState[][] roomsPlacement, ICRogueRoom room);

    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        map[coords.x][coords.y] = room;
    }

    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        this.connector =  map[coords.x][coords.y].getConnector().get(connector.getIndex());
        this.connector.setDestinationAreaName(destination);
    }

    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        this.connector =  map[coords.x][coords.y].getConnector().get(connector.getIndex());
        this.connector.setStats(Connector.ConnectorStats.CLOSE);
        setRoomConnectorDestination(coords,destination,connector);
    }

    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        this.connector =  map[coords.x][coords.y].getConnector().get(connector.getIndex());
        this.connector.setStats(Connector.ConnectorStats.LOCKED,keyId);
    }

    public String[][] addArea(ICRogue icRogue){
        String[][] titleMap = new String[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[i][j] != null){
                    icRogue.addArea(map[i][j]);
                    titleMap[i][j] = map[i][j].getTitle();
                }
            }
        }
        return titleMap;
    }

    public DiscreteCoordinates getStartRoomCoords(){
        return roomSpawnCoords;
    }

    protected void setStartRoomName(DiscreteCoordinates coords){
        startRoomName = "icrogue/level0"+coords.x+coords.y;
    }

    public void update(float deltaTime){
        ICRogueRoom bossRoom = map[bossRoomCoordinate.x][bossRoomCoordinate.y];
        if ((bossRoom != null) && (bossRoom.logic == TRUE && logic == FALSE)) {
            logic = TRUE;
            System.out.println("Win");
        }
    }
}
