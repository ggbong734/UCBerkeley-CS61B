import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayDequeTest {
    //Test addFirst, addLast, isEmpty, size functions
    @Test
    public void testAdd() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();
        assertTrue(ad1.isEmpty());
        ad1.addFirst("b");
        ad1.addLast("a");
        ad1.addLast("t");
        String[] exp = new String[]{"b", "a", "t", null, null, null, null, null};
        assertArrayEquals(exp, ad1.items);
        assertEquals(3, ad1.size);

        assertEquals("t", ad1.get(2));
        assertEquals("b", ad1.get(0));
    }
    // Test removeFirst and removeLast functions, test if resize is correct!
    @Test
    public void testRemove() {
        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        for(int i = 0; i < 12; i++) {
            ad2.addLast(i);
        }
        ad2.removeFirst();
        ad2.removeLast();
        Integer[] exp2 = new Integer[]{7, 8, 9, 10, null, null, null, null, null, null, 1, 2, 3, 4, 5, 6};
        assertArrayEquals(exp2, ad2.items);
        int one = ad2.get(0);
        assertEquals(1, one);
        for(int i = 0; i < 7; i++) {
            ad2.removeLast();
        }
        Integer[] exp3 = new Integer[]{null, null, 1, 2, 3, null, null, null};
        assertArrayEquals(exp3, ad2.items);
    }

    //Test ArrayDeque copier constructor
    @Test
    public void testConstructor() {
        ArrayDeque<Integer> ad4 = new ArrayDeque<>();
        ad4.addFirst(0);
        ad4.addLast(1);
        ad4.addLast(2);
        ad4.addLast(3);
        ad4.addLast(4);
        Integer[] exp4 = new Integer[]{0, 1, 2, 3, 4, null, null, null};
        ArrayDeque<Integer> adCopy = new ArrayDeque<>(ad4);
        assertArrayEquals(exp4, adCopy.items);
    }
        //Then get ArrayDeque constructor to work

        //ArrayDeque<String> adCopy = new ArrayDeque<>(ad1);
        //assertArrayEquals(ad1.items, adCopy.items);
}