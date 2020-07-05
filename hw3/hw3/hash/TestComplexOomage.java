package hw3.hash;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /* Create a list of Complex Oomages called deadlyList
     * that shows the flaw in the hashCode function.
     *
     * To show flaw in the hashcode algorithm: The hashcode is base 256
     * which means after the number of items in param get bigger than
     * 4, the hashcode will become zero since the hashcode will exceed
     * 2 billion (the max integer java can hold). Thus to exploit this
     * limitation, we need to set the last four items of params to a
     * fixed value while randomizing the first four items.
     * The first four items will end up adding the highest values to
     * the hashcode but will instead become zero since the number
     * limit is reached. The last four items has fixed params and
     * will equal to the same hashcode.
     * All the hashcode of this object will be of the same value.
     */

    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();
        int N = 100;
        for (int i = 0; i < N; i += 1) {
            int P = 8;
            List<Integer> params = new ArrayList<>(P);
            for (int j = 0; j < 4; j += 1) {
                params.add(StdRandom.uniform(0, 255));
            }
            for (int k = 4; k < P; k += 1) {
                params.add(30);
            }
            Oomage newCO = new ComplexOomage(params);
            //System.out.println(newCO.hashCode());
            deadlyList.add(newCO);
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
