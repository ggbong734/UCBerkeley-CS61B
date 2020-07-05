package byow.Core;

public class Position {
    private int x;
    private int y;

    // Position(0,0) corresponds to the bottom left of the world
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }

    public void updateX(int newX) {
        x = newX;
    }

    public void updateY(int newY) {
        y = newY;
    }
}
