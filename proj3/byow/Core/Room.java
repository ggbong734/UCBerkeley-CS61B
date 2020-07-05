package byow.Core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private Position lowLeft;
    private int height;
    private int width;
    private int lastEntranceDirection;
    private Map<Integer, Position> entrances;

    public Room() {
        this.lowLeft = new Position(0,0);
        this.height = 0;
        this.width = 0;
        entrances = new HashMap<>();
    }

    public Room(Position lowLeft, int height, int width) {
        this.lowLeft = lowLeft;
        this.height = height;
        this.width = width;
        entrances = new HashMap<>();
    }

    public void addEntrance(int entranceDirection, Position p) {
        entrances.put(entranceDirection, p);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Position getLowLeft() {
        return lowLeft;
    }

    public Map<Integer, Position> getEntrances() {
        return entrances;
    }

    public void updateLastEntranceDirection(int newSide) {
        lastEntranceDirection = newSide;
    }

    public int getLastEntranceDirection() {return lastEntranceDirection;}
}
