package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int numberOfExpt;
    private double[] percThresholds;

    //perform T independent experiments on an N-by-N grid
    // Generate an array of T percolation thresholds at the
    // end of the T experiments
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N < 1 || T <= 0) {
            throw new IllegalArgumentException();
        }
        numberOfExpt = T;
        percThresholds = new double[T];
        for (int i = 0; i < numberOfExpt; i++) {
            Percolation percItem;
            percItem = pf.make(N);
            while (!percItem.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                percItem.open(row, col);
            }
            // add *1.0 to convert value to double
            percThresholds[i] = percItem.numberOfOpenSites() * 1.0 / (N * N);
        }
    }

    //sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percThresholds);
    }

    //sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percThresholds);
    }

    //low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.pow(numberOfExpt, 0.5);
    }

    //high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.pow(numberOfExpt, 0.5);
    }
}
