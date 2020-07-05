/** This file constructs the Array Deque List class.
 * This is part of project 1A of UC Berkeley CS 61B by Josh Hug.
 * @param <T>
 * @author Gerry Bong   
 */
public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    /* Constructor for an empty array. Starts with size 8.*/
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    /*Computes the index immediately before a given index in a circular array */
    private int minusOne(int index) {
        return ((index - 1) + items.length) % items.length;
    }

    /*Computes the index immediately after a given index in a circular array */
    private int plusOne(int index) {
        return (index + 1) % items.length;
    }

    /* Resize an array to desired capacity */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        // expanding array (nextFirst < nextLast) or when shrinking array (nextFirst > nextLast)
        // nextLast stays at the same position
        if (((capacity > items.length) && (nextFirst < nextLast))
                || ((capacity < items.length) && (nextFirst > nextLast))) {
            System.arraycopy(items, plusOne(nextFirst),
                             a, capacity - (items.length - plusOne(nextFirst)),
                       items.length - plusOne(nextFirst));
            System.arraycopy(items, 0, a, 0, nextLast);
            nextFirst = capacity - (items.length - nextFirst);
            items = a;


        } else if ((capacity > items.length) && (nextFirst > nextLast)) {
            // expanding array (nextFirst > nextLast)
            // nextFirst and nextLast stay at the same position
            System.arraycopy(items, plusOne(nextFirst),
                    a, capacity - (items.length - plusOne(nextFirst)),
                    items.length - plusOne(nextFirst));
            items = a;

        } else {
            //shrinking array (nextLast > nextFirst)
            //nextFirst and nextLast are changed
            int overIndex = ((nextLast / capacity) * capacity);
            System.arraycopy(items, overIndex, a, 0, nextLast - overIndex);
            nextFirst = nextFirst - overIndex;
            nextLast = nextLast - overIndex;
            items = a;
        }
    }


    /* Adds an item to the end of the array */
    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        size += 1;
        nextLast = plusOne(nextLast);
    }

    /* Adds an item to the front of the array */
    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        size += 1;
        nextFirst = minusOne(nextFirst);
    }

    /* Returns true if deque is empty, false otherwise */
    public boolean isEmpty() {
        return (size == 0);
    }

    /* Returns the number of items in the deque */
    @Override
    public int size() {
        return size;
    }

    /* Gets the item at the given index, where 0 is the front,
    1 is the next item, and so forth. If no such item exists, returns null.*/
    @Override
    public T get(int index) {
        if ((index < 0) || (index > size - 1)) {
            return null;
        }
        int position = (nextFirst + 1 + index) % items.length;
        return items[position];
    }
    // Get the first item in the array
    private T getFirst() {
        return get(0);
    }

    // Get the last item in the array
    private T getLast() {
        return get(size - 1);
    }

    /* Computes the usage factor of an array */
    private double usageFactor() {
        return 1.0 * size / items.length;
    }

    /* Removes and returns the item at the front of the deque.
       If no such item exists, returns null*/
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T returnItem = getFirst();
        nextFirst = plusOne(nextFirst);
        items[nextFirst] = null;
        size -= 1;

        if ((items.length >= 16) && (usageFactor() < 0.25)) {
            resize(items.length / 2);
        }
        return returnItem;
    }

    /* Removes and returns the item at the back of the deque.
       If no such item exists, returns null*/
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T returnItem = getLast();
        nextLast = minusOne(nextLast);
        items[nextLast] = null;
        size -= 1;

        if ((items.length >= 16) && (usageFactor() < 0.25)) {
            resize(items.length / 2);
        }
        return returnItem;
    }

    /* Constructor method that creates a copy of other */
    public ArrayDeque(ArrayDeque other) {
        items = (T[]) new Object[other.items.length];
        nextFirst = other.nextFirst;
        nextLast = plusOne(nextFirst);

        for (int i = 0; i < other.size; i++) {
            addLast((T) other.get(i));
        }
    }

    /* Prints the items in the array deque from first to last, separated by space */
    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }
}
