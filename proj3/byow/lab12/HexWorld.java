package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    public class Position {
        int x;
        int y;

        public Position(int xPosn, int yPosn) {
            x = xPosn;
            y = yPosn;
        }
    }

    /**
     * Computes the width of row i for a size s hexagon.
     * @param size The size of the hex.
     * @param row The row number where i = 0 is the bottom row.
     */
    public static int hexRowWidth(int size, int row) {
        int effectiveRow = row;
        if (row >= size) {
            effectiveRow = 2 * size - effectiveRow - 1;
        }
        return size + effectiveRow * 2;
    }

    /**
     * Computes relative x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero. For example, if s = 3, and i = 2, this function
     * returns -2, because the row 2 up from the bottom starts 2 to the left
     * of the start position, e.g.
     *   xxx
     *  xxxxx
     * xxxxxxx
     * xxxxxxx <-- i = 2, starts 2 spots to the left of the bottom of the hex
     *  xxxxx
     *   xxx
     *
     * @param size size of the hexagon
     * @param row row num of the hexagon, where i = 0 is the bottom
     * @return
     */
    public static int hexRowOffset(int size, int row) {
        int effectiveRow = row;
        if (row >= size) {
            effectiveRow = 2 * size - effectiveRow - 1;
        }
        return -effectiveRow;
    }

    /** Adds a row of the same tile.
     * @param world the world to draw on
     * @param p the leftmost position of the row
     * @param width the number of tiles wide to draw
     * @param t the tile to draw
     */
    public void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = p.x + xi;
            int yCoord = p.y;
            world[xCoord][yCoord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }


    /**
     * Adds a hexagon to the world.
     * @param world the world to draw on
     * @param p the bottom left coordinate of the hexagon
     * @param s the size of the hexagon
     * @param t the tile to draw
     */
    public void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        if (s < 2) {
            throw new IllegalArgumentException("hexagon size cannot be smaller than 2");
        }

        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * s; yi += 1) {
            int thisRowY = p.y + yi;

            int rowStartX = hexRowOffset(s, yi);
            Position rowStartP = new Position(rowStartX, thisRowY);
            int rowWidth = hexRowWidth(s, yi);
            addRow(world, rowStartP, rowWidth, t);
        }

    }
}

