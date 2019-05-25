package bearmaps.proj2ab;

import java.util.List;

/** @author Gerry Bong
 *  Source: Josh Hug's Pseudocode video and lectures
 *  https://www.youtube.com/watch?v=KsA5Kfs1gTg&feature=youtu.be
 */

public class KDTree {

    private List<Point> points;
    private Node root;
    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;

    private class Node {
        Node left;
        Node right;
        Point position;
        boolean orientation;     // direction of partition

        Node(Point position, boolean orientation) {
            this.position = position;
            this.orientation = orientation;   // root is vertical
            left = null;
            right = null;
        }
    }

    /* A constructor for KDTree that builds a tree using
       BSTmap. Treat equal item as greater than (arbitrary tie breaker),
       Move to the right side.
     */
    public KDTree(List<Point> points) {
        this.points = points;
        root = new Node(points.get(0), VERTICAL);
        for (Point p : points) {
            insert(p, root);
        }
    }

    /* Compares the x or y coordinate of Point p and the position of
       Node N depending on the orientation of the Node.
       If orientation is VERTICAL (true), compare x coordinates.
       If orientation is HORIZONTAL (false), compare y coordinates.
     */
    private int compareXY(Point p, Node n, boolean orientation) {
        int returnVal = 0;
        if (orientation == VERTICAL) {
            returnVal = Double.compare(p.getX(), n.position.getX());
        } else if (orientation == HORIZONTAL) {
            returnVal = Double.compare(p.getY(), n.position.getY());
        }
        return returnVal;
    }

    /* Insert a new Point into the existing tree. Create a new Node if there
       is no similar Point present in the tree.
     */
    public Node insert(Point p, Node n) {
        return insertHelper(p, n, n.orientation);
    }

    private Node insertHelper(Point p, Node n, boolean parentOrientation) {
        if (n == null) {
            return new Node(p, !parentOrientation);
        }

        int cmp = compareXY(p, n, n.orientation);

        if (cmp < 0) {
            n.left = insertHelper(p, n.left, n.orientation);
        } else if (cmp == 0 && compareXY(p, n, !n.orientation) == 0) {
            n.position = p;
        } else if (cmp >= 0) {
            n.right = insertHelper(p, n.right, n.orientation);
        }
        return n;
    }

    /* Returns the closest point to the inputted coordinates.
     * This should take O(logN) time on average, where N is the number of points.
     */

    public Point nearest(double x, double y) {
        if (points == null) {
            throw new NullPointerException();
        }
        Point goal = new Point(x, y);
        Node bestNode = nearestHelper(root, goal, root);
        return bestNode.position;
    }

    /* Helper method that returns the nearest Node to a goal Point.
     * Recursively traverses the tree, updating current node as the
     * best node if the node is closest to the goal.
     * Method determines which child of the current node is the
     * good or bad side. Always traverses down the good side but
     * method may choose to prune the bad side.
     */
    private Node nearestHelper(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }
        if (Point.distance(n.position, goal)
                < Point.distance(best.position, goal)) {
            best = n;
        }
        int cmp = compareXY(goal, n, n.orientation);

        Node goodSide = goodSideHelper(n, cmp);
        Node badSide = badSideHelper(n, cmp);

        best = nearestHelper(goodSide, goal, best);
        if (compareBadSideWithBest(n, goal, best) < 0) {
            best = nearestHelper(badSide, goal, best);
        }
        return best;
    }

    // Helper method to determine which child node is good side.
    private Node goodSideHelper(Node n, int cmp) {
        if (cmp < 0) {
            return n.left;
        } else {
            return n.right;
        }
    }

    //Helper method to determine which child node is bad side.
    private Node badSideHelper(Node n, int cmp) {
        if (cmp < 0) {
            return n.right;
        } else {
            return n.left;
        }
    }

    /* Helper method to decide if bad side can be pruned. Returns an int.
       Bad side is pruned if perpendicular distance from goal to bad side is
       more than distance from goal to the best node.
       Perpendicular distance can be improved to diagonal distance for
       optimization.
     */
    private int compareBadSideWithBest(Node n, Point goal, Node best) {
        double goalToN = 0;  //Straight distance to the bad side of node
        double goalToBest = Point.distance(goal, best.position);

        // goalToN is the perpendicular distance from goal to the bad side
        // of a node.
        if (n.orientation == VERTICAL) {
            Point p = new Point(n.position.getX(), goal.getY());
            goalToN = Point.distance(goal, p);
        } else if (n.orientation == HORIZONTAL) {
            Point p = new Point(goal.getX(), n.position.getY());
            goalToN = Point.distance(goal, p);
        }
        return Double.compare(goalToN, goalToBest);
    }
}
