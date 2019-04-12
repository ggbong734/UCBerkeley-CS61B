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
    // Test removeFirst and removeLast functions
    @Test
    public void testRemove() {
        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        assertTrue(ad2.isEmpty());
        ad2.addFirst(0);
        ad2.addFirst(7);
        ad2.addFirst(6);
        ad2.addLast(1);
        ad2.addLast(2);
        ad2.addFirst(5);
        ad2.removeFirst();
        ad2.removeLast();
        ad2.removeFirst();
        Integer[] exp2 = new Integer[]{0, 1, null, null, null, null, null, 7};
        assertArrayEquals(exp2, ad2.items);
        assertEquals(3, ad2.size);
    }

    //Test ArrayDeque copier constructor
    @Test
    public void testConstructor() {
        ArrayDeque<Integer> ad3 = new ArrayDeque<>();
        ad3.addFirst(0);
        ad3.addLast(1);
        ad3.addLast(2);
        ad3.addLast(3);
        ad3.addLast(4);
        Integer[] exp3 = new Integer[]{0, 1, 2, 3, 4, null, null, null};
        ArrayDeque<Integer> adCopy = new ArrayDeque<>(ad3);
        assertArrayEquals(exp3, adCopy.items);
    }
        //Then get ArrayDeque constructor to work

        //ArrayDeque<String> adCopy = new ArrayDeque<>(ad1);
        //assertArrayEquals(ad1.items, adCopy.items);
}