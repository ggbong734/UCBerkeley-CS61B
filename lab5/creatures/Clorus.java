package creatures;

import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * An implementation of a blue-colored predator that snacks on Plip
 *
 * @author Gerry Bong
 * Source: Josh Hugh Plip.java
 */

public class Clorus extends Creature {
    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;

    /**
     * creates clorus with energy equal to E.
     */
    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    /**
     * creates a plip with energy equal to 1.
     */
    public Clorus() {
        this(1);
    }

    /**
     * Should return a color with red = 34, blue = 231, and green = 0
     */
    public Color color() {
        return color(r, g, b);
    }

    /**
     * Clorus gains the energy of the creature it attacks.
     * Simulator cleans up the victim when it dies.
     */
    public void attack(Creature c) {
        energy += c.energy();
    }

    /**
     * Clorus should lose 0.03 units of energy when moving.
     */
    public void move() {
        energy -= 0.03;
    }


    /**
     * Clorus loses 0.01 energy when staying.
     */
    public void stay() {
        energy -= 0.01;
    }

    /**
     * Clorus and their offspring each get 50% of the energy, with none
     * lost to the process. Returns a baby Clorus.
     */
    public Clorus replicate() {
        energy = energy / 2;
        return new Clorus(this.energy);
    }

    /**
     * Clorus take exactly the following actions based on NEIGHBORS:
     * 1. If no empty adjacent spaces, STAY.
     * 2. Otherwise if a Plip is adjacent, Clorus will attack one of
     * them randomly.
     * 3. Otherwise, if energy >= 1, REPLICATE towards an empty direction
     * chosen at random.
     * 4. Otherwise, Clorus will MOVE to random empty square.
     * <p>
     * Returns an object of type Action. See Action.java for the
     * scoop on how Actions work. See SampleCreature.chooseAction()
     * for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        // Rule 1
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> adjacentPlips = new ArrayDeque<>();
        boolean anyPlip = false;
        // (Google: Enhanced for-loop over keys of NEIGHBORS?)
        // Loop through every entry (key:value pair) in the map, if value is empty,
        // add direction to emptyNeighbors array deque. If neighbor is a Plip, add
        // to the adjacentPlips deque.
        for (Map.Entry<Direction, Occupant> entry : neighbors.entrySet()) {
            Occupant oc = entry.getValue();
            if (oc.name().equals("empty")) {
                emptyNeighbors.add(entry.getKey());
            } else if (oc.name().equals("plip")) {
                anyPlip = true;
                adjacentPlips.add(entry.getKey());
            }
        }

        // Rule 1, Stay if there are no empty neighbors
        if (emptyNeighbors.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        }

        // Rule 2, randomly attack any adjacent Plip
        if (anyPlip) {
            return new Action(Action.ActionType.ATTACK,
                    HugLifeUtils.randomEntry(adjacentPlips));
        }

        // Rule 3, replicate if energy more than 1 and there is empty neighbor
        // HINT: randomEntry(emptyNeighbors)
        if (energy >= 1.0) {
            return new Action(Action.ActionType.REPLICATE,
                    HugLifeUtils.randomEntry(emptyNeighbors));
        }

        // Rule 4, move to an empty space
        return new Action(Action.ActionType.MOVE,
                HugLifeUtils.randomEntry(emptyNeighbors));
    }
}
