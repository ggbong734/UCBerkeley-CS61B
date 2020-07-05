package bearmaps;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {

    @Test
    public void testAddRemoveSmallest() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("first", 0.1);
        pq.add("second", 0.3);
        pq.add("third", 0.4);
        pq.add("fourth", 0.5);
        pq.add("fifth", 0.6);
        assertEquals(pq.size(), 5);
        String smallest = pq.getSmallest();
        String smallestRM = pq.removeSmallest();
        assertEquals("first", smallest);
        assertEquals(smallest, smallestRM);
        assertEquals(true, pq.contains("fourth"));
        assertEquals(pq.size(), 4);

        String smallest2 = pq.getSmallest();
        String smallestRM2 = pq.removeSmallest();
        assertEquals("second", smallest2);
        assertEquals(smallest2, smallestRM2);

        String smallest3 = pq.getSmallest();
        String smallestRM3 = pq.removeSmallest();
        assertEquals("third", smallest3);
        assertEquals(smallest3, smallestRM3);
    }

    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("fifth", 0.6);
        pq.add("third", 0.4);
        pq.add("fourth", 0.5);
        pq.add("second", 0.3);
        pq.add("first", 0.1);
        String smallest = pq.getSmallest();
        String smallestRM = pq.removeSmallest();
        assertEquals("first", smallest);
        assertEquals(smallest, smallestRM);
        pq.add("new first", 0.2);
        assertEquals("new first", pq.getSmallest());
        pq.changePriority("second", 0.1);
        String smallest2 = pq.getSmallest();
        assertEquals("second", smallest2);
        pq.changePriority("fourth", 0.1);
        assertEquals("second", pq.removeSmallest());
        assertEquals("fourth", pq.removeSmallest());
        pq.changePriority("third", 1.0);
        pq.changePriority("new first", 0.8);
        assertEquals("fifth", pq.removeSmallest());
    }

    @Test
    public void testEdgeCases1() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("first", 0.1);
        pq.add("second", 0.3);
        pq.add("third", 0.4);
        pq.add("fourth", 0.5);
        pq.changePriority("second", 1.0);
        pq.removeSmallest();
        pq.removeSmallest();
        pq.removeSmallest();
        assertEquals("second", pq.removeSmallest());
    }

}
