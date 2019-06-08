package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** This is project 3 of CS 61B Spring 2019 course at UC Berkeley. The tasks is to generate
 * a randomized map with rooms and hallways, similar to a 2D maze in a video game.
 * @author Gerry Bong
 * Received instructions and guidance from Josh Hug, UC Berkeley professor.
 */

import static byow.Core.Constants.NORTH;
import static byow.Core.Constants.SOUTH;
import static byow.Core.Constants.EAST;
import static byow.Core.Constants.WEST;

public class MapGenerator {
    private Random RANDOM;

    private static final int maxRoomSize = 7;
    private static final int maxHallLength = 6;
    private static final double maxWorldLoadFactor = 0.35;
    private static final double PercentageToBuildRoom = 0.42;

    private Position lastHallwayEndPosition;
    private double worldLoadFactor = 0;
    private List<Room> existingRooms;
    public MapGenerator(int seed) {
        RANDOM = new Random(seed);
        existingRooms = new ArrayList<>();
    }

    /** Draws a room with random dimensions
     * @param world the world tiles
     * @param bearing the direction that the position is facing
     * @param start the starting Position (x, y)
     */
    public Room drawRoom (TETile[][] world, int bearing, Position start) {
        Position first = firstPositionFromStart(start, bearing);
        int height = RandomUtils.uniform(RANDOM, 1, maxRoomSize);
        int width = RandomUtils.uniform(RANDOM, 1, maxRoomSize);

        // maximum amount of travel in a direction
        int maxNorth = maxNorth(world, first);
        int maxSouth = maxSouth(world, first);
        int maxEast = maxEast(world, first);
        int maxWest = maxWest(world, first);
        int goNorth, goSouth, goEast, goWest;
        goNorth = goSouth = goEast = goWest = 0;

        // select the minimum between random number or the max allowable distance
        if (bearing == NORTH) {
            goNorth = Math.min(height - 1, maxNorth - 1);
            goEast = Math.min(RandomUtils.uniform(RANDOM, width), maxEast - 1);
            goWest = Math.min(width - goEast - 1, maxWest - 1);

        } else if (bearing == SOUTH) {
            goSouth = Math.min(height - 1, maxSouth - 1);
            goWest = Math.min(RandomUtils.uniform(RANDOM, width), maxWest - 1);
            goEast = Math.min(width - goWest - 1, maxEast - 1);

        } else if (bearing == EAST) {
            goEast = Math.min(width - 1, maxEast - 1);
            goNorth = Math.min(RandomUtils.uniform(RANDOM, height), maxNorth - 1);
            goSouth = Math.min(height - goNorth - 1, maxSouth - 1);

        } else if (bearing == WEST) {
            goWest = Math.min(width - 1, maxWest - 1);
            goSouth = Math.min(RandomUtils.uniform(RANDOM, height), maxSouth - 1);
            goNorth = Math.min(height - goSouth - 1, maxNorth - 1);
        }

        //pick another location to draw room if there is no space
//        if ((goNorth + goSouth) <= 0 || (goEast + goWest) <= 0 ) {
//            int rd = RandomUtils.uniform(RANDOM, existingRooms.size());
//            Position rdEntrance = addEntranceToRoom(world, existingRooms.get(rd));
//            int entranceBearing = existingRooms.get(rd).getLastEntranceDirection();
//            return drawRoom(world, entranceBearing, rdEntrance);
//        }
        //low left corner position of the room
        Position lowLeft = new Position(first.getX() - goWest, first.getY() - goSouth);
        Position topRight = new Position(first.getX() + goEast, first.getY() + goNorth);

        // entrance direction is the reverse bearing of the start Position
        Room newRoom = new Room(lowLeft, goNorth + goSouth + 1, goEast + goWest + 1);
        newRoom.addEntrance(Constants.reverse(bearing), start);
        existingRooms.add(newRoom);

        //draw tiles
        drawRectangularTiles(world, lowLeft, topRight, Tileset.FLOOR);
        drawWallsAroundRoom(world, newRoom);
        addWorldLoadFactor(world, newRoom.getHeight() + 2, newRoom.getWidth() + 2);

        System.out.println("Drew a room with height: " + goSouth + goNorth + " and width: " + goEast + goWest);
        return newRoom;
    }

    private void addWorldLoadFactor(TETile[][] world, int totalHeight, int totalWidth) {
        worldLoadFactor += (totalHeight * totalWidth * 1.0) / (world[0].length * world.length);
    }

    /** Returns the distance from the start Position to the North edge of the world or
     *  the first non-empty tile encountered.
     *  Starts at 1 to skip walls around start position.
     */
//    private int maxNorth(TETile[][] world, Position start) {
//        int nonEmptyTile = 0;
//        for (int y = start.getY() + 1; y < world[0].length; y += 1) {
//            if (world[start.getX()][y] != Tileset.NOTHING) {
//                return nonEmptyTile;
//            }
//            nonEmptyTile += 1;
//        }
//        return nonEmptyTile;
//    }
    private int maxNorth(TETile[][] world, Position start) {
        int nonEmptyTile = 1;
        for (int y = start.getY() + 2; y < world[0].length - 1; y += 1) {
            if (world[start.getX()][y] != Tileset.NOTHING) {
                break;
            }
            nonEmptyTile += 1;
        }
        return nonEmptyTile;
    }

    /** Returns the distance from the start Position to the South edge of the world or
     *  the first non-empty tile encountered.
     */
//    private int maxSouth(TETile[][] world, Position start) {
//        int nonEmptyTile = 0;
//        for (int y = start.getY() - 1; y >= 0; y -= 1) {
//            if (world[start.getX()][y] != Tileset.NOTHING) {
//                return nonEmptyTile;
//            }
//            nonEmptyTile += 1;
//        }
//        return nonEmptyTile;
//    }
    private int maxSouth(TETile[][] world, Position start) {
        int nonEmptyTile = 1;
        for (int y = start.getY() - 2; y >= 0; y -= 1) {
            if (world[start.getX()][y] != Tileset.NOTHING) {
                break;
            }
            nonEmptyTile += 1;
        }
        return nonEmptyTile;
    }

    /** Returns the distance from the start Position to the East edge of the world or
     *  the first non-empty tile encountered.
     */
//    private int maxEast(TETile[][] world, Position start) {
//        int nonEmptyTile = 0;
//        for (int x = start.getX() + 1; x < world.length; x += 1) {
//            if (world[x][start.getY()] != Tileset.NOTHING) {
//                return nonEmptyTile;
//            }
//            nonEmptyTile += 1;
//        }
//        return nonEmptyTile;
//    }
    private int maxEast(TETile[][] world, Position start) {
        int nonEmptyTile = 1;
        for (int x = start.getX() + 2; x < world.length - 1; x += 1) {
            if (world[x][start.getY()] != Tileset.NOTHING) {
                break;
            }
            nonEmptyTile += 1;
        }
        return nonEmptyTile;
    }

    /** Returns the distance from the start Position to the West edge of the world or
     *  the first non-empty tile encountered.
     */
    private int maxWest(TETile[][] world, Position start) {
        int nonEmptyTile = 1;
        for (int x = start.getX() - 2; x >= 0; x -= 1) {
            if (world[x][start.getY()] != Tileset.NOTHING) {
                return nonEmptyTile;
            }
            nonEmptyTile += 1;
        }
        return nonEmptyTile;
    }

    /** Draw walls around a rectangular room or hallway
     */
    public void drawWallsAroundRoom(TETile[][] world, Room r) {
        Position llc =r.getLowLeft(); // low left corner of room
        Position lowLeft = new Position (llc.getX() - 1, llc.getY() - 1);
        Position lowRight = new Position (llc.getX() + r.getWidth(), lowLeft.getY());
        Position topLeft = new Position (llc.getX() - 1, llc.getY() + r.getHeight());
        Position topRight = new Position (llc.getX() + r.getWidth(), llc.getY() + r.getHeight());

        //draw four walls around
        drawRectangularTiles(world, topLeft, topRight, Tileset.WALL);
        drawRectangularTiles(world, lowLeft, lowRight, Tileset.WALL);
        drawRectangularTiles(world, lowLeft, topLeft, Tileset.WALL);
        drawRectangularTiles(world, lowRight, topRight, Tileset.WALL);
        //draw entrances
        for (Position e: r.getEntrances().values()) {
            world[e.getX()][e.getY()] = Tileset.FLOOR;
        }
    }
    /** Adds an entrance to a random side. Position of entrance is randomized along side.
     * Maximum of one entrance per side.
     * @param r room
     */
    public Position addEntranceToRoom(TETile[][] world, Room r) {
        // escape method if all entrances are filled
        if (r.getEntrances().size() == 4) {
            throw new IllegalArgumentException("Room has no more empty side");
        }
        double[] sidesNoEntrance = getSidesWithoutEntrances(world, r);

        // randomly select a side without entrance
        int randomSide = RandomUtils.discrete(RANDOM, sidesNoEntrance);

        // determine starting position of the side (bottom or left most)
        Position start = new Position(0, 0);
        int length = 0;
        int newSideLength = 0;

        if (randomSide == NORTH) {
            length = r.getHeight();
            start.updateX(r.getLowLeft().getX());
            start.updateY(r.getLowLeft().getY() + length);
            newSideLength = r.getWidth();

        } else if (randomSide == SOUTH) {
            length = r.getHeight();
            start.updateX(r.getLowLeft().getX());
            start.updateY(r.getLowLeft().getY() - 1);
            newSideLength = r.getWidth();

        } else if (randomSide == EAST) {
            length = r.getWidth();
            start.updateX(r.getLowLeft().getX() + length);
            start.updateY(r.getLowLeft().getY());
            newSideLength = r.getHeight();

        } else if (randomSide == WEST) {
            length = r.getWidth();
            start.updateX(r.getLowLeft().getX() - 1);
            start.updateY(r.getLowLeft().getY());
            newSideLength = r.getHeight();
        }

        // select a random position from the side
        Position newEntranceP = getRandomPointOnALine(start, newSideLength, randomSide);
        r.addEntrance(randomSide, newEntranceP);

        r.updateLastEntranceDirection(randomSide);

        return newEntranceP;
    }

    /** Generate an array of "openness" in each direction. "Openness" is the probability of
     *  building a room in the direction, calculated using the ratio of open space in
     *  one direction compared to the other directions.
     *
     * @param world the 2D array of tiles that represents the world
     * @param r the room to be checked
     * @return array of probabilities of moving in 4 directions
     */
    public double[] getSidesWithoutEntrances(TETile[][] world, Room r) {
        double[] sides = new double[4];
        Position ll = r.getLowLeft();
        int llX = ll.getX();
        int llY = ll.getY();

        // if room does not have an entrance in a side then
        if (r.getEntrances().containsKey(NORTH)) {
            sides[NORTH] = 0.0;
        } else {
            Position test = new Position(llX, llY + r.getHeight());
            sides[NORTH]  = maxNorth(world, test) * 1.0;
        }
        if (r.getEntrances().containsKey(EAST)) {
            sides[EAST] = 0.0;
        } else {
            Position test = new Position(llX + r.getWidth(), llY);
            sides[EAST]  = maxEast(world, test) * 1.0;
        }
        if (r.getEntrances().containsKey(SOUTH)) {
            sides[SOUTH] = 0.0;
        } else {
            Position test = new Position(llX, llY - 1);
            sides[SOUTH]  = maxSouth(world, test) * 1.0;
        }
        if (r.getEntrances().containsKey(WEST)) {
            sides[WEST] = 0.0;
        } else {
            Position test = new Position(llX - 1, llY);
            sides[WEST]  = maxWest(world, test) * 1.0;
        }
        double totalFreedom = 0.0;
        for(double s : sides) {
            totalFreedom += s;
        }
        for(int i = 0; i < sides.length; i += 1) {
            sides[i] = sides[i] / totalFreedom;
        }

        return sides;
    }

    /** Pick a random point along a line given the start point, bearing, and line length
     */
    public Position getRandomPointOnALine(Position start, int sideLength, int bearing) {
        int x;
        int y;
        Position p = new Position(0,0);
        if (bearing == NORTH || bearing == SOUTH) {
            x = RandomUtils.uniform(RANDOM, start.getX(), start.getX() + sideLength);
            y = start.getY();

        } else {
            x = start.getX();
            y = RandomUtils.uniform(RANDOM, start.getY(), start.getY() + sideLength);
        }
        p.updateX(x);
        p.updateY(y);
        return p;
    }

    /** Draw a Horizontal hallway. The start position is not included in the hallway.
     *  The hallway is always NORTH or SOUTH of the start tile.
     *  This can only be added if previous bearing is NORTH or SOUTH.
     *
     * @param world world of tiles
     * @param newBearing direction that hallway will move towards
     * @param start last position occupied by another room or hallway
     */
    public void drawHorizontalHallway(TETile[][] world, int pastBearing, int newBearing, Position start) {
        Position first = firstPositionFromStart(start, pastBearing);
        Position last = new Position(0, first.getY());
        int length = RandomUtils.uniform(RANDOM, 1, maxHallLength);;   // length of hallway
        if (newBearing == EAST) {
            length = Math.min(length, maxEast(world, first) - 2);
            last.updateX(first.getX() + length);
            //draw tiles
            drawRectangularTiles(world, first, last, Tileset.FLOOR);
            drawWallsAroundHallway(world, first, last, start, newBearing);
        } else if (newBearing == WEST) {
            length = Math.min(length, maxWest(world, first) - 2);
            last.updateX(first.getX() - length);
            //draw tiles
            drawRectangularTiles(world, last, first, Tileset.FLOOR);
            drawWallsAroundHallway(world, last, first, start, newBearing);
        }
        lastHallwayEndPosition = last;

        addWorldLoadFactor(world, 3, length + 2);
        System.out.println("Drew a horizontal hallway with length " + length + " and bearing " + newBearing);
    }

    /** Draw a Vertical hallway. The start position is not included in the hallway.
     *  The hallway is always EAST or WEST of the start tile.
     *  This can only be added if previous bearing is EAST or WEST.
     *
     * @param world world of tiles
     * @param newBearing direction that hallway will move towards
     * @param start last position occupied by another room or hallway
     */
    public void drawVerticalHallway(TETile[][] world, int pastBearing, int newBearing, Position start) {
        Position first = firstPositionFromStart(start, pastBearing);
        Position last = new Position(first.getX(), 0);
        int length = RandomUtils.uniform(RANDOM, 1, maxHallLength);   // length of hallway
        if (newBearing == NORTH) {
            length = Math.min(length, maxNorth(world, first) - 2);
            last.updateY(first.getY() + length);
            //draw tiles, lowleft position first
            drawRectangularTiles(world, first, last, Tileset.FLOOR);
            drawWallsAroundHallway(world, first, last, start, newBearing);

        } else if (newBearing == SOUTH) {
            length = Math.min(length, maxSouth(world, first) - 2);
            last.updateY(first.getY() - length);
            //draw tiles, lowleft position first
            drawRectangularTiles(world, last, first, Tileset.FLOOR);
            drawWallsAroundHallway(world, last, first, start, newBearing);
        }
         lastHallwayEndPosition = last;

        addWorldLoadFactor(world, length + 2, 3);
        System.out.println("Drew a vertical hallway with length " + length + " and bearing " + newBearing);
    }

    /** Returns the first position of the tile in the direction of the bearing
     *
     * @param start the start Position to be extended from
     * @param pastBearing the bearing of the last item to connect this to
     * @return the first position of the tile in the direction of the bearing
     */
    public Position firstPositionFromStart(Position start, int pastBearing) {
        Position first = new Position(start.getX(), start.getY());
        if (pastBearing == NORTH) {
            first.updateY(start.getY() + 1);
        } else if (pastBearing == SOUTH) {
            first.updateY(start.getY() - 1);
        } else if (pastBearing == EAST) {
            first.updateX(start.getX() + 1);
        } else {
            first.updateX(start.getX() - 1);
        }
        return first;
    }

    /** Draw walls around hallways, find four corners of walls,
     *  cover the entire perimeter with walls,
     *  then remove the start position.
     */
    public void drawWallsAroundHallway(TETile[][] world, Position lowL, Position topR, Position start, int bearing) {
        Position topLeft = new Position(0, 0);
        Position topRight = new Position(0, 0);
        Position lowLeft = new Position(0, 0);
        Position lowRight = new Position(0, 0);
        if (bearing == NORTH || bearing == SOUTH) {
            topLeft = new Position(topR.getX() - 1,topR.getY() + 1 );
            topRight = new Position(topR.getX() + 1, topR.getY() + 1);
            lowLeft = new Position(lowL.getX() - 1, lowL.getY() - 1);
            lowRight = new Position(lowL.getX() + 1, lowL.getY() - 1);
        } else if (bearing == EAST || bearing == WEST) {
            topLeft = new Position(lowL.getX() - 1,lowL.getY() + 1 );
            topRight = new Position(topR.getX() + 1, topR.getY() + 1);
            lowLeft = new Position(lowL.getX() - 1, lowL.getY() - 1);
            lowRight = new Position(topR.getX() + 1, topR.getY() - 1);
        }
        //draw four walls around
        drawRectangularTiles(world, topLeft, topRight, Tileset.WALL);
        drawRectangularTiles(world, lowLeft, lowRight, Tileset.WALL);
        drawRectangularTiles(world, lowLeft, topLeft, Tileset.WALL);
        drawRectangularTiles(world, lowRight, topRight, Tileset.WALL);

        //replace start position with floor
        drawRectangularTiles(world, start, start, Tileset.FLOOR);
    }

    /** Draws a rectangular shape given the low left and top right corner of the shape
     *
     * @param world the 2D array to draw tiles in
     * @param lowLeft lower left corner of the rectangle
     * @param topRight top right hand corner of the rectangle
     * @param tile the tileset to fill the rectangle with
     */
    public void drawRectangularTiles(TETile[][] world, Position lowLeft, Position topRight, TETile tile) {
        for (int x = lowLeft.getX(); x <= topRight.getX(); x += 1) {
            for (int y = lowLeft.getY(); y <= topRight.getY(); y += 1) {
                world[x][y] = tile;
            }
        }
    }

    /** Initiates the world generation sequence by building a room at a random
     *  location and marking the starting point with a DOOR tile.
     */
    public void generateRoomsAndHallways(TETile[][] world) {
        //fill everything with Nothing tile
        for (int x = 0; x < world.length; x += 1) {
            for (int y = 0; y < world[0].length; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        // start by building a room
        int startBearing = RandomUtils.uniform(RANDOM, 4); // select random direction
        Position start = selectRandomPointInWorld(world);
        Room newRoom = drawRoom(world, startBearing, start);
        Position newEntrance = addEntranceToRoom(world, newRoom);
        int endBearing = newRoom.getLastEntranceDirection();
        world[start.getX()][start.getY()] = Tileset.LOCKED_DOOR;
        //recursion
        generateRoomsAndHallways(world, newEntrance, endBearing);
    }
    /** Helper function to recursively build the world. Stops generating more rooms/hallways
     *  if the desired worldLoadFactor has been reached.
     *  location and marking the starting point with a DOOR tile.
     */
    private void generateRoomsAndHallways(TETile[][] world, Position start, int startBearing) {
        //base case, end recursion when load factor is reached
        if (worldLoadFactor > maxWorldLoadFactor)  {
            return;
        }
        // create probability of picking each side
        Position first = firstPositionFromStart(start, startBearing);
        int endBearing = getMostLikelyDirection(world, first);
        int distanceToWall = getDistanceToWall(world, first, endBearing);
        // if end bearing too close too wall, select a new random start point from a room
        int totalRooms = existingRooms.size();
        if (distanceToWall < 2) {
            int rd1 = RandomUtils.uniform(RANDOM, totalRooms);
            while (existingRooms.get(rd1).getEntrances().size() >= 3) { // pick rooms that have two or less entrances
                rd1 = RandomUtils.uniform(RANDOM, totalRooms);
            }
            Position rdEntrance = addEntranceToRoom(world, existingRooms.get(rd1));
            int entranceBearing = existingRooms.get(rd1).getLastEntranceDirection();
            generateRoomsAndHallways(world, rdEntrance, entranceBearing);
            return;
        }

        // pick whether to draw a room or horizontal or vertical hallway
        double rd = RandomUtils.uniform(RANDOM);
        Position newStart;

        //draw new room
        if (rd < PercentageToBuildRoom) {
            Room newRoom = drawRoom(world, startBearing, start);
            newStart = addEntranceToRoom(world, newRoom);
            endBearing = newRoom.getLastEntranceDirection();
        } else {
            if (endBearing == NORTH || endBearing == SOUTH){
                drawVerticalHallway(world, startBearing, endBearing, start);
            } else if (endBearing == EAST || endBearing == WEST) {
                drawHorizontalHallway(world, startBearing, endBearing, start);
            }
            newStart = lastHallwayEndPosition;
        }
        //recursively build world
        generateRoomsAndHallways(world, newStart, endBearing);
    }

    /** Get distance from a point p to a wall with the specified bearing
     */
    public int getDistanceToWall(TETile[][] world, Position p, int endBearing) {
        int distanceToWall = 0;
        if (endBearing == NORTH) {
            distanceToWall = maxNorth(world, p);
        } else if (endBearing == EAST) {
            distanceToWall = maxEast(world, p);
        } else if (endBearing == SOUTH) {
            distanceToWall = maxSouth(world, p);
        } else if (endBearing == WEST) {
            distanceToWall = maxWest(world, p);
        }
        return distanceToWall;
    }

    /** Select the direction with the most spaces from the starting point
     *  by comparing amount of empty spaces in each direction.
     */
    public int getMostLikelyDirection(TETile[][] world, Position start) {
        int endBearing;
        int maxNorth = maxNorth(world, start);
        int maxSouth = maxSouth(world, start);
        int maxEast = maxEast(world, start);
        int maxWest = maxWest(world, start);
        int totalFreedom = maxNorth + maxSouth + maxEast + maxWest;
        double chanceOfNorth = 1.0 * maxNorth / totalFreedom;
        double chanceOfSouth = 1.0 * maxSouth / totalFreedom;
        double chanceOfEast = 1.0 * maxEast / totalFreedom;
        double chanceOfWest = 1.0 * maxWest / totalFreedom;
        double[] probabilities = new double[]{chanceOfNorth, chanceOfEast, chanceOfSouth, chanceOfWest};
        endBearing = RandomUtils.discrete(RANDOM, probabilities);
        return endBearing;
    }

    /** Select a random point in the world, for starting generation process
     */
    public Position selectRandomPointInWorld(TETile[][] world) {
        int x = RandomUtils.uniform(RANDOM, 2, world.length - 2);
        int y = RandomUtils.uniform(RANDOM, 2, world[0].length - 2);
        return new Position(x, y);
    }
}
