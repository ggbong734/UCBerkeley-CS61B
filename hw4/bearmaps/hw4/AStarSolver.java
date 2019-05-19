package bearmaps.hw4;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/** @author Gerry Bong
 *  this is hw4 of CS61B Sp 2019 course
 *  for with pseudocode help from Josh Hug
 *
 * @param <Vertex>
 */

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private ArrayHeapMinPQ<Vertex> fringe;
    private SolverOutcome outcome;
    private ArrayList<Vertex> solutionArray;
    private double solutionWeight;
    private int numStatesExplored;
    private double explorationTime;

    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, Vertex> edgeTo;
    private HashSet<Vertex> visitedVertices;


    /** Constructor which finds the solution, computing everything necessary for
     * all other methods to return their results in constant time.
     * Note that timeout passed in is in seconds.
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();

        fringe = new ArrayHeapMinPQ<>();
        solutionArray = new ArrayList<>();
        outcome = null;
        solutionWeight = 0;
        numStatesExplored = 0;
        explorationTime = 0;
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        visitedVertices = new HashSet<>();

        // Insert source vertex into PQ and insert source into
        fringe.add(start, 0);
        distTo.put(start, 0.0);

        //exit loop if PQ is empty or end vertex is the first of PQ
        while (fringe.size() != 0 && !fringe.getSmallest().equals(end)) {

            Vertex p = fringe.removeSmallest();
            visitedVertices.add(p);
            numStatesExplored += 1;
            List<WeightedEdge<Vertex>> neighborEdges = input.neighbors(p);

            //relax only outgoing edges
            for (WeightedEdge<Vertex> edge : neighborEdges) {

                Vertex s = edge.from();    // same as p (can remove)
                Vertex t = edge.to();
                double edgeWeight = edge.weight();

                // relaxation fails if target vertex is already visited
                // add new distance if vertex is not relaxed
                // otherwise if new edge is better, update distance and edge
                if (visitedVertices.contains(t)) {
                    continue;   //continue to next iteration instead of breaking loop
                } else if ((!distTo.containsKey(t))) {
                    distTo.put(t, distTo.get(s) + edgeWeight);
                    edgeTo.put(t, s);
                } else if (distTo.get(s) + edgeWeight < distTo.get(t)) {
                    distTo.replace(t, distTo.get(s) + edgeWeight);
                    edgeTo.replace(t, s);
                }

                // add relaxed vertex to fringe if not already present.
                // if vertex is already in fringe, update priority.
                double newPriority = distTo.get(t) + input.estimatedDistanceToGoal(t, end);
                if (!fringe.contains(t)) {
                    fringe.add(t, newPriority);
                } else {
                    fringe.changePriority(t, newPriority);
                }
            }

            explorationTime = sw.elapsedTime();

            //exit if goal vertex is the first in PQ
            if (fringe.getSmallest().equals(end)) {
                outcome = SolverOutcome.SOLVED;
                // to get the solution array and weight
                buildSolutionArray(end, solutionArray);
                solutionWeight = distTo.get(end);
                break;
            }

            //exit if time spent exceeds timeout value
            if (explorationTime > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                break;
            }

            //otherwise this graph is unsolvable
            outcome = SolverOutcome.UNSOLVABLE;
        }
    }

    // recursively trace the shortest path (in edgeTo array) from the end goal
    // to add the shortest path vertices to the solution array.
    private void buildSolutionArray(Vertex t, ArrayList<Vertex> solArray) {
        if (!edgeTo.containsKey(t)) {
            solArray.add(t);
        } else {
            Vertex s = edgeTo.get(t);
            buildSolutionArray(s, solArray);
            solArray.add(t);
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
