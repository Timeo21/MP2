package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Level {
    ICRogueRoom[][] map;
    DiscreteCoordinates roomSpawnCoords;
    DiscreteCoordinates bossRoomCoordinate;
    String startRoomName;
    int height;
    int width;
    public Level(int height, int width, DiscreteCoordinates startRoomCoords, ICRogue icRogue){
        this(height,width,startRoomCoords,icRogue,new DiscreteCoordinates(0,0));
    }

    public Level(int height, int width, DiscreteCoordinates startRoomCoords,ICRogue icRogue, DiscreteCoordinates bossRoomCoordinate){
        this.width = width;
        this.height = height;
        this.bossRoomCoordinate = bossRoomCoordinate;
        map = new ICRogueRoom[width][height];
        for (ICRogueRoom[] rooms : map){
            for (ICRogueRoom room : rooms){
                if (room != null) icRogue.addArea(room);
            }
        }
    }


    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        map[coords.x][coords.y] = room;
    }

    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        new Connector(map[coords.x][coords.y], Level0Room.Level0Connectors.getAllConnectorsPosition().get(connector.getIndex()),
                Level0Room.Level0Connectors.getAllConnectorsOrientation().get(connector.getIndex()), Connector.ConnectorStats.CLOSE,destination,connector.getDestination());
    }

    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        new Connector(map[coords.x][coords.y], Level0Room.Level0Connectors.getAllConnectorsPosition().get(connector.getIndex()),
                Level0Room.Level0Connectors.getAllConnectorsOrientation().get(connector.getIndex()), Connector.ConnectorStats.LOCKED,destination,connector.getDestination());
    }

    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        map[coords.x][coords.y].getConnector().get(connector.getIndex()).setKeyID(keyId);
    }

    protected void setStartRoomName(DiscreteCoordinates coords){
        startRoomName = "icrogue/level0"+coords.x+coords.y;
    }

}
