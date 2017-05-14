package util.preprossing;

import java.util.logging.Logger;

import logging.LoggerFactory;

public class Hungarian {
    private static final double FLOATING_POINT_EPSILON = 1E-14;
    
    private static Logger logger = LoggerFactory.getLogger(Hungarian.class.getName());

    private int n;              // number of rows and columns
    private int[][] weight;  	// the n-by-n weight matrix
    private int[] x;         	// dual variables for rows
    private int[] y;         	// dual variables for columns
    private int[] xy;           // xy[i] = j means i-j is a match
    private int[] yx;           // yx[j] = i means i-j is a match
 
    
    public static int[] run(int[][] weight)
    {
    	return new Hungarian(weight).getResult();
    }
    
    public int[] getResult()
    {
    	return this.xy;
    }
    
    public Hungarian(int[][] weight) {
        n = weight.length;
        this.weight = new int[n][n];
        for (int i = 0; i < n; i++) 
        {
            for (int j = 0; j < n; j++) 
            {
                if (!(weight[i][j] >= 0))
                    throw new IllegalArgumentException("weights must be non-negative");
                this.weight[i][j] = weight[i][j];
            }
        }

        x = new int[n];
        y = new int[n];
        xy = new int[n];
        yx = new int[n];
        for (int i = 0; i < n; i++)
            xy[i] = -1;
        for (int j = 0; j < n; j++)
            yx[j] = -1;

        while (true) {

            // build graph of 0-reduced cost edges
            FlowNetwork G = new FlowNetwork(2*n + 2);
            int s = 2*n, t = 2*n+1;
            for (int i = 0; i < n; i++) {
                if (xy[i] == -1) G.addEdge(new FlowEdge(s, i, 1.0));
                else             G.addEdge(new FlowEdge(s, i, 1.0, 1.0));
            }
            for (int j = 0; j < n; j++) {
                if (yx[j] == -1) G.addEdge(new FlowEdge(n+j, t, 1.0));
                else             G.addEdge(new FlowEdge(n+j, t, 1.0, 1.0));
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (reducedCost(i, j) == 0) {
                        if (xy[i] != j) G.addEdge(new FlowEdge(i, n+j, 1.0));
                        else            G.addEdge(new FlowEdge(i, n+j, 1.0, 1.0));
                    }
                }
            }

            // to make n^4, start from previous solution
            // (i.e., initialize flow to correspond to current matching;
            //  as a result, there will be exactly n augmenting paths
            //  over all calls to FordFulkerson because each one increases
            //  the value of the flow by 1)
            FordFulkerson ff = new FordFulkerson(G, s, t);

            // current matching
            for (int i = 0; i < n; i++)
                xy[i] = -1;
            for (int j = 0; j < n; j++)
                yx[j] = -1;
            for (int i = 0; i < n; i++) {
                for (FlowEdge e : G.adj(i)) {
                    if ((e.from() == i) && (e.flow() > 0)) {
                        xy[i] = e.to() - n;
                        yx[e.to() - n] = i;
                    }
                }
            }

            // perfect matching
            if (ff.value() == n) break;

            // find bottleneck weight
            double max = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (ff.inCut(i) && !ff.inCut(n+j) && (reducedCost(i, j) < max))
                        max = reducedCost(i, j);

            // update dual variables
            for (int i = 0; i < n; i++)
                if (!ff.inCut(i)) x[i] -= max;
            for (int j = 0; j < n; j++)
                if (!ff.inCut(n + j)) y[j] += max;

            logger.info("value = " + ff.value());
        }
        assert check();
    }

    // reduced cost of i-j
    // (subtracting off minWeight reweights all weights to be non-negative)
    private double reducedCost(int i, int j) {
        double reducedCost = weight[i][j] - x[i] - y[j];
            
        // to avoid issues with floating-point precision
        double magnitude = Math.abs(weight[i][j]) + Math.abs(x[i]) + Math.abs(y[j]);
        if (Math.abs(reducedCost) <= FLOATING_POINT_EPSILON * magnitude) return 0.0;

        assert reducedCost >= 0.0;
        return reducedCost;
    }


    // check optimality conditions
    private boolean check() {
        // check that xy[] is a permutation
        boolean[] perm = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (perm[xy[i]]) {
                logger.info("Not a perfect matching");
                return false;
            }
            perm[xy[i]] = true;
        }

        // check that all edges in xy[] have 0-reduced cost
        for (int i = 0; i < n; i++) {
            if (reducedCost(i, xy[i]) != 0) {
                logger.info("Solution does not have 0 reduced cost");
                return false;
            }
        }

        // check that all edges have >= 0 reduced cost
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (reducedCost(i, j) < 0) {
                    logger.info("Some edges have negative reduced cost");
                    return false;
                }
            }
        }
        return true;
    }
}
