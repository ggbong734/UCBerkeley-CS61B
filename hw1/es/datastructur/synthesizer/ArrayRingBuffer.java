package es.datastructur.synthesizer;
import java.util.Iterator;
import java.util.NoSuchElementException;

//Make sure to that this class and all of its methods are public
//Make sure to add the override tag for all overridden methods
//Make sure to make this class implement BoundedQueue<T>

public class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    @Override
    // return size of the buffer
    public int capacity() {
        return rb.length;
    }

    @Override
    //return number of items currently in the buffer
    public int fillCount() {
        return fillCount;
    }

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        // Enqueue the item. Don't forget to increase fillCount and update
        //       last.
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last += 1;
        if (last == rb.length) {
            last = 0;
        }
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        // Dequeue the first item. Don't forget to decrease fillCount and
        //       update first.
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T removed = rb[first];
        rb[first] = null;
        first += 1;
        if (first == rb.length) {
            first = 0;
        }
        fillCount -= 1;
        return removed;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        // Return the first item. None of your instance variables should
        //       change.
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return rb[first];
    }

    // When you get to part 4, implement the needed code to support
    //       iteration and equals.
    @Override
    public Iterator<T> iterator() {
        return new ArrayRBIterator();
    }

    private class ArrayRBIterator implements Iterator<T> {
        T[] rbIterator;
        int index;
        int count;
        int fillCountIterator;

        ArrayRBIterator() {
            rbIterator = rb;
            index = first;
            count = 0;
            fillCountIterator = fillCount;
        }

        @Override
        public boolean hasNext() {
            while (count < fillCountIterator && rbIterator[index] != null) {
                index = (index + 1 == fillCountIterator ? 0 : index + 1);
                count += 1;
            }
            return count < fillCountIterator;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T toReturn = rbIterator[index];
            index = (index + 1 == fillCountIterator ? 0 : index + 1);
            count += 1;
            return toReturn;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == this.getClass()) {
            ArrayRingBuffer<T> newO = (ArrayRingBuffer<T>) o;
            if (newO.fillCount() == this.fillCount()) {
                for (T element : this) {
                    for (T elementO : (ArrayRingBuffer<T>) o) {
                        if (element != elementO) {
                            return false;
                        }
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }
}
