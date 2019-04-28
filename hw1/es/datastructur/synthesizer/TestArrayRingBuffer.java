package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void testPartOneToThree() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(7);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        arb.enqueue(4);
        arb.enqueue(5);
        arb.enqueue(6);
        int peeked = arb.peek();
        int removed = arb.dequeue();
        assertEquals(1, peeked);
        assertEquals(1, removed);
        arb.enqueue(7);
        arb.dequeue();
        arb.dequeue();
        arb.dequeue();
        arb.enqueue(8);
        arb.enqueue(9);
        arb.dequeue(); //remove 5
        arb.dequeue(); //remove 6
        arb.dequeue(); //remove 7
        int removed2 = arb.dequeue(); //8
        assertEquals(8, removed2);
    }

    @Test
    public void testPartFour() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(5);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        arb.enqueue(4);
        arb.enqueue(5);
        ArrayRingBuffer<Integer> arb2 = new ArrayRingBuffer<>(5);
        arb2.enqueue(1);
        arb2.enqueue(2);
        arb2.enqueue(3);
        arb2.enqueue(4);
        arb2.enqueue(5);
        assertTrue(arb.equals(arb2));
        ArrayRingBuffer<Integer> arb3 = new ArrayRingBuffer<>(5);
        arb3.enqueue(1);
        arb3.enqueue(2);
        arb3.enqueue(3);
        arb3.enqueue(4);
        assertFalse(arb.equals(arb3));
    }
}
