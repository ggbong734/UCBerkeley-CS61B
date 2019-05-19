package bearmaps.hw4;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/** @author Gerry Bong
 *  this is hw4 of CS61B Sp 2019 course
 *  for with pseudocode help from Josh Hug and yngz
 *  https://github.com/yngz/cs61b/blob/master/hw4/bearmaps/hw4/AStarSolver.java
 *
 * @param <Vertex>
 */

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private SolverOutcome outcome;
    private LinkedList<Vertex> solutionArray;
    private double solutionWeight;
    private int numStatesExplored;
    private double explorationTime;

    /** Constructor which finds the solution, computing everything necessary for
     * all other methods to return their results in constant time.
     * Note that timeout passed in is in seconds.
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start,
                       Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();

        outcome = null;
        solutionArray = new LinkedList<>();
        solutionWeight = 0;
        numStatesExplored = 0;
        explorationTime = 0;

        ExtrinsicMinPQ<Vertex> fringe = new ArrayHeapMinPQ<>();
        HashMap<Vertex, Double> distTo = new HashMap<>();
        HashMap<Vertex, Vertex> edgeTo = new HashMap<>();

        // Checks if start condition is end
        if (start.equals(end)) {
            outcome = SolverOutcome.SOLVED;
            explorationTime = sw.elapsedTime();
            return;
        }

        // Insert source vertex into PQ and insert source into
        distTo.put(start, 0.0);
        fringe.add(start, distTo.get(start)
                + input.estimatedDistanceToGoal(start, end));

        //exit loop if PQ is empty or end vertex is the first of PQ
        while (fringe.size() > 0 && !fringe.getSmallest().equals(end)
                && sw.elapsedTime() < timeout) {

            Vertex p = fringe.removeSmallest();
            numStatesExplored += 1;

            //relax only outgoing edges
            List<WeightedEdge<Vertex>> neighborEdges = input.neighbors(p);
            for (WeightedEdge<Vertex> edge : neighborEdges) {

                Vertex t = edge.to();
                double edgeWeight = edge.weight();

                // relaxation fails if target vertex is already visited
                // add new distance if vertex is not relaxed
                // otherwise if new edge is better, update distance and edge
                if (!distTo.containsKey(t) || distTo.get(p) + edgeWeight < distTo.get(t)) {

                    distTo.put(t, distTo.get(p) + edgeWeight);
                    edgeTo.put(t, p);
                    // add relaxed vertex to fringe if not already present.
                    // if vertex is already in fringe, update priority.
                    double newPriority = distTo.get(t) + input.estimatedDistanceToGoal(t, end);
                    if (!fringe.contains(t)) {
                        fringe.add(t, newPriority);
                    } else {
                        fringe.changePriority(t, newPriority);
                    }
                }
            }

            //exit if fringe PQ is empty or goal vertex is the first in PQ
            //or if timeout value is exceeded.
            if (fringe.size() == 0) {
                outcome = SolverOutcome.UNSOLVABLE;
            } else if (fringe.getSmallest().equals(end)) {
                outcome = SolverOutcome.SOLVED;
                // to get the solution array and weight
                solutionWeight = distTo.get(end);
                Vertex n = fringe.getSmallest();
                while (n != null) {
                    solutionArray.addFirst(n);
                    n = edgeTo.get(n);
                }
            } else {
                outcome = SolverOutcome.TIMEOUT;
            }
            explorationTime = sw.elapsedTime();
        }
    }

    /**Returns one of SolverOutcome.SOLVED, SolverOutcome.TIMEOUT, or SolverOutcome.UNSOLVABLE.
     * Should be SOLVED if the AStarSolver was able to complete all work in the time given.
     * UNSOLVABLE if the priority queue became empty. TIMEOUT if the solver ran out of time.
     * You should check to see if you have run out of time every time you dequeue.
     */
    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    /**A list of vertices corresponding to a solution. Should be empty if result
     * was TIMEOUT or UNSOLVABLE.
     */
    @Override
    public List<Vertex> solution() {
        return solutionArray;
    }

    /**The total weight of the given solution, taking into account edge weights.
     * Should be 0 if result was TIMEOUT or UNSOLVABLE.
     */
    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    /**The total number of priority queue dequeue operations.*/
    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    /**The total time spent in seconds by the constructor.*/
    @Override
    public double explorationTime() {
        return explorationTime;
    }

}
