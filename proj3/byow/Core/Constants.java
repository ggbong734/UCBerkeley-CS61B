package byow.Core;

public class Constants {
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    public static int reverse(int bearing) {
        if (bearing == NORTH) {
            return SOUTH;
        } else if (bearing == SOUTH) {
            return NORTH;
        } else if (bearing == EAST) {
            return WEST;
        } else {
            return EAST;
        }
    }
}
