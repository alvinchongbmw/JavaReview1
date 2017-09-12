/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats num1 num2
 *  Dependencies: Percolation.java, StdRandom, StdStats, Stopwatch in alogs4
 *
 *  This program takes two numbers a command-line argument.
 *
 *    - First number respresent the size 'n' of the grid. n x n
 *    - Creates an n-by-n grid of sites (intially all blocked) 
 *    - Reads in a second number which respresents the number of trials
 *    - In main(), it print out the stat after the number of trials
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

/**
 *   This is the constructor of PercolationStats
 *   It takes two args and create the n x n grid
 *   Runs trials number of experiments and store data in a double array
 *   The double array is use for stats calculation
 */
public class PercolationStats {
    private final double meanResult;
    private final double stdResult;
    private final double confidenceResult;
   
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) 
            throw new IllegalArgumentException("n or trials out has to be > 0");
        double[] data = new double[trials];
        int x;
        int y;
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                x = StdRandom.uniform(n)+1;
                y = StdRandom.uniform(n)+1;
                percolation.open(x, y);
            }  
            data[i] = (double) percolation.numberOfOpenSites()/ 
                      (double) (n * n);
            percolation = null;
        }
        meanResult = StdStats.mean(data);
        stdResult = StdStats.stddev(data);
        confidenceResult = 1.96 * (stdResult / Math.sqrt(trials));
    }
   // sample mean of percolation threshold
    public double mean() {
        return meanResult;
    }       
    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdResult;
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - confidenceResult;
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + confidenceResult;
    }
    // test client (described below)
    public static void main(String[] args) {
        Stopwatch watch = new Stopwatch();
        double start = watch.elapsedTime();
        int n = Integer.parseInt(args[0]);
        int trial = Integer.parseInt(args[1]);
        PercolationStats test = new PercolationStats(n, trial);
        System.out.println("mean                    = "+ test.mean());
        System.out.println("stddev                  = "+ test.stddev());
        System.out.println("95% confidence interval = ["+test.confidenceLo() 
                               + ", " + test.confidenceHi()+" ]");
        double end = watch.elapsedTime();
        end = end - start;
        System.out.println("Finished in "+end+" second");
    }
}