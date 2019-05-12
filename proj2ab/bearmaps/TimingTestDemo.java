package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug. Demonstrates how you can use either
 * System.currentTimeMillis or the Princeton Stopwatch
 * class to time code.
 */
public class TimingTestDemo {
    public static void main(String[] args) {
        NaiveMinPQ<Integer> nmq = new NaiveMinPQ<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i += 1) {
            nmq.add(100000-i, i /100000.0);
        }
        for (int j = 0; j < 100000; j += 1) {
            nmq.removeSmallest();
        }
        long end = System.currentTimeMillis();
        System.out.println("Total time elapsed: " + (end - start)/1000.0 +  " seconds.");

        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();

        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < 100000; i += 1) {
            pq.add(100000-i, i /100000.0);
        }
        for (int j = 0; j < 100000; j += 1) {
            pq.removeSmallest();
        }
        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
    }
}
