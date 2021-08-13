
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] results;
    private final int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("arguments should be positive integers.");
        }
        results = new double[trials];
        this.trials = trials;
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            results[i] = oneTrial(p, n);
        }
    }

    private double oneTrial(Percolation p, int n) {
        while (!p.percolates()) {
            int randomRow = StdRandom.uniform(n);
            int randomCol = StdRandom.uniform(n);
            p.open(randomRow+1, randomCol+1);
        }
        return p.numberOfOpenSites() / (n * n * 1.0);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / (Math.sqrt(trials)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / (Math.sqrt(trials)));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats pS = new PercolationStats(n, trials);
        StdOut.printf("%-24s = %.7f\n", "mean", pS.mean());
        StdOut.printf("%-24s = %.7f\n", "stddev", pS.stddev());
        StdOut.printf("%-24s = [%.16f, %.16f]\n",
                      "95% confidence interval",
                      pS.confidenceLo(), pS.confidenceHi());
    }
}
