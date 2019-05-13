package bearmaps;
import java.util.List;

public class NaivePointSet implements PointSet {
    private List<Point> points;

    // constructor class, can assume points have at least size 1
    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    /* Returns the closest point to the inputted coordinates.
     * This should take θ(N) time where N is the number of points.
     */
    public Point nearest(double x, double y) {
        if (points == null) {
            throw new NullPointerException();
        }
        Point newPoint = new Point(x, y);
        Point nearestPoint = points.get(0);
        double min = Point.distance(newPoint, points.get(0));

        for (int i = 1; i < points.size(); i++) {
            double dist = Point.distance(newPoint, points.get(i));
            if (dist < min) {
                min = dist;
                nearestPoint = points.get(i);
            }
        }
        return nearestPoint;
    }
}
