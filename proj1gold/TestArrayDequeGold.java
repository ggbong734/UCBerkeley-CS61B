/** This file constructs a JUnit test file to apply randomized tests to
 *  StudentArrayDeque and ArrayDequeSolution classes.
 *  This is project 1gold of UC Berkeley CS 61B Spring 2019 taught by Josh Hug.
 * @author Gerry Bong
 */

import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void testArrayDeque() {
        /*  Randomly calls StudentArrayDeque and ArrayDequeSolution
            @source StudentArrayDeque.java file in proj1 gold
         */
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sol1 = new ArrayDequeSolution<>();

        String message = "";
        for (int i = 0; i < 10; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                sad1.addLast(i);
                sol1.addLast(i);
                message += "addLast(" + i + ")\n";
                assertEquals(message, sad1.get(sad1.size() - 1), sol1.get(sol1.size() - 1));
            } else {
                sad1.addFirst(i);
                sol1.addFirst(i);
                message += "addFirst(" + i + ")\n";
                assertEquals(message, sad1.get(0), sol1.get(0));
            }
        }

        Integer xSad;
        Integer xSol;

        for (int i = 0; i < 10; i += 1) {
            double numberBetween0And1 = StdRandom.uniform();

            if (numberBetween0And1 < 0.5) {
                xSad = sad1.removeFirst();
                xSol = sol1.removeFirst();
                message += "removeFirst()\n";
                assertEquals(message, xSad, xSol);
            } else {
                xSad = sad1.removeLast();
                xSol = sol1.removeLast();
                message += "removeLast()\n";
                assertEquals(message, xSad, xSol);
            }
        }

    }
}
