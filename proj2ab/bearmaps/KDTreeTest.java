package bearmaps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** @author Gerry Bong
 *  Code for this test file based on Josh Hug's explanation
 *  https://www.youtube.com/watch?v=KsA5Kfs1gTg&feature=youtu.be
 */

public class KDTreeTest {
    private static Random rnd = new Random(5);

    /*  Test the tree built in lecture and ensure method returns (1, 5)
     *  as closest node
     */
    @Test
    public void testLectureTree() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        NaivePointSet nps = new NaivePointSet(List.of(p1, p2, p3, p4, p5, p6, p7));

        Point expected = kd.nearest(0, 7);
        Point actual = nps.nearest(0, 7);
        Point correct = new Point(1, 5);

        assertEquals(actual, correct);
        assertEquals(expected, actual);
    }

    private List<Point> randomPoints(int N) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            points.add(new Point(rnd.nextDouble(), rnd.nextDouble()));
        }
        return points;
    }

    /*  Creates 10,000 points and run 1000 nearest operations
     *  and checks that the output of the KDTree is the same as the
     *  naive approach.
     */
    @Test
    public void test10000EntriesAnd1000Queries() {
        List<Point> points = randomPoints(10000);
        KDTree kd = new KDTree(points);
        NaivePointSet nps = new NaivePointSet(points);

        List<Point> queries = randomPoints(1000);
        for (Point p : queries) {
            Point kdNearest = kd.nearest(p.getX(), p.getY());
            Point npsNearest = nps.nearest(p.getX(), p.getY());
            assertEquals(npsNearest, kdNearest);
        }
    }

    @Test
    public void testTiming() {
        List<Point> points = randomPoints(100000);
        KDTree kd = new KDTree(points);
        NaivePointSet nps = new NaivePointSet(points);

        List<Point> queries = randomPoints(10000);

        long start = System.currentTimeMillis();
        for (Point p : queries) {
            Point npsNearest = nps.nearest(p.getX(), p.getY());
        }
        long end = System.currentTimeMillis();
        long naiveTime = end - start;
        System.out.println("Total time elapsed for NaiveNearest: "
                + (naiveTime) / 1000.0 + " seconds.");

        long start2 = System.currentTimeMillis();
        for (Point p : queries) {
            Point kdNearest = kd.nearest(p.getX(), p.getY());
        }
        long end2 = System.currentTimeMillis();
        long kdTime = end2 - start2;
        System.out.println("Total time elapsed for kdTree: " + (kdTime) / 1000.0 + " seconds.");
        assertTrue(1.0 * kdTime / naiveTime < 0.1);
    }
}
