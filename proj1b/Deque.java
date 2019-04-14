/** This file
 *  This is project 1b of UC Berkeley CS 61B Spring 2019 taught by Josh Hug.
 * @param <T>
 * @author Gerry Bong
 */

public interface Deque<T> {

    //No constructor in interface;
    void printDeque();
    void addLast(T item);
    void addFirst(T item);
    default boolean isEmpty() {
        return (size() == 0);
    }
    int size();
    T get(int index);
    T removeFirst();
    T removeLast();
}



