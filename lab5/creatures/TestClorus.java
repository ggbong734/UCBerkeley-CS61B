package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the plip class
 *  @author Gerry Bong
 */

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.move();
        assertEquals(1.94, c.energy(), 0.01);
        c.stay();
        assertEquals(1.93, c.energy(), 0.01);
        c.stay();
        assertEquals(1.92, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(1.8);
        Clorus babyC = c.replicate();
        assertEquals(0.9, c.energy(), 0.01);
        assertEquals(c.energy(), babyC.energy(), 0.01);
        assertNotSame(c, babyC);
    }

    @Test
    public void testChoose() {

        // No empty adjacent spaces; stay.
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        // Energy >= 1; replicate towards an empty space.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> topEmpty = new HashMap<Direction, Occupant>();
        topEmpty.put(Direction.TOP, new Empty());
        topEmpty.put(Direction.BOTTOM, new Impassible());
        topEmpty.put(Direction.LEFT, new Impassible());
        topEmpty.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> allEmpty = new HashMap<Direction, Occupant>();
        allEmpty.put(Direction.TOP, new Empty());
        allEmpty.put(Direction.BOTTOM, new Empty());
        allEmpty.put(Direction.LEFT, new Empty());
        allEmpty.put(Direction.RIGHT, new Empty());

        actual = c.chooseAction(allEmpty);
        Action unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);


        // Energy < 1; Clorus will move to an empty space.
        c = new Clorus(.99);

        actual = c.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.MOVE, Direction.TOP);

        assertEquals(expected, actual);


        // Energy < 1; Clorus will move to a random empty space.
        c = new Clorus(.99);

        actual = c.chooseAction(allEmpty);
        expected = new Action(Action.ActionType.STAY);

        assertNotEquals(expected, actual);
    }

    @Test
    public void testChoose2() {
        // A Plip nearby, Clorus will attack.
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> topPlip = new HashMap<Direction, Occupant>();
        topPlip.put(Direction.TOP, new Plip());
        topPlip.put(Direction.BOTTOM, new Empty());
        topPlip.put(Direction.LEFT, new Empty());
        topPlip.put(Direction.RIGHT, new Empty());

        Action actual = c.chooseAction(topPlip);
        Action expected = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expected, actual);

        // Surrounded by Plips, Clorus will stay.
        c = new Clorus(0.99);
        HashMap<Direction, Occupant> manyPlip = new HashMap<Direction, Occupant>();
        manyPlip.put(Direction.TOP, new Plip());
        manyPlip.put(Direction.BOTTOM, new Plip());
        manyPlip.put(Direction.LEFT, new Plip());
        manyPlip.put(Direction.RIGHT, new Plip());

        actual = c.chooseAction(manyPlip);
        expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        //Empty adjacent space and no Plip to attack, Clorus will move.
        c = new Clorus(0.99);
        HashMap<Direction, Occupant> oneEmpty = new HashMap<Direction, Occupant>();
        oneEmpty.put(Direction.TOP, new Clorus());
        oneEmpty.put(Direction.BOTTOM, new Impassible());
        oneEmpty.put(Direction.LEFT, new Empty());
        oneEmpty.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(oneEmpty);
        expected = new Action(Action.ActionType.MOVE, Direction.LEFT);

        assertEquals(expected, actual);

    }
}
