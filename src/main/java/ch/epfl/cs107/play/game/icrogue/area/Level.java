package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level {
    ICRogueRoom[][] map;
    DiscreteCoordinates spawnCoordinate;
    DiscreteCoordinates bossRoomCoordinate;
    String startRoomName;
    int height;
    int width;

    public Level(int height, int width){
        this.width = width;
        this.height = height;
        map = new ICRogueRoom[width][height];
    }


    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        map[coords.x][coords.y] = room;
    }

    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){

    }

    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){

    }

    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){

    }

}
