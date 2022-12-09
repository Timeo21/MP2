package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Level {
    ICRogueRoom[][] map;
    DiscreteCoordinates roomSpawnCoords;
    DiscreteCoordinates bossRoomCoordinate;
    String startRoomName;
    private Connector connector;
    int height;
    int width;
    public Level(int height, int width, DiscreteCoordinates startRoomCoords){
        this(height,width,startRoomCoords,new DiscreteCoordinates(0,0));
    }

    public Level(int height, int width, DiscreteCoordinates startRoomCoords, DiscreteCoordinates bossRoomCoordinate){
        this.width = width;
        this.height = height;
        this.bossRoomCoordinate = bossRoomCoordinate;
        map = new ICRogueRoom[width][height];
        generateFixedMap();
    }

    protected abstract void generateFixedMap();


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

    public void switchRoom(DiscreteCoordinates newRoomCoords, ICRogue icRogue){

    }

    protected void setStartRoomName(DiscreteCoordinates coords){
        startRoomName = "icrogue/level0"+coords.x+coords.y;
    }

}