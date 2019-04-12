/** This file constructs a data structure class for doubly linked list with circular sentinel node.
 *  This is project 1a of UC Berkeley CS 61B Spring 2019 taught by Josh Hug.
 * @param <T>
 */
public class LinkedListDeque<T> {

    private class ListNode<T> {
        public ListNode prev;
        public T item;
        public ListNode next;

        public ListNode(ListNode p, T i, ListNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private int size;
    private ListNode<Integer> sentinel;

    /* Creates an empty LinkedListDeque */
    public LinkedListDeque() {
        sentinel = new ListNode<>(null, 99, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /* Creates a LinkedListDeque with an item
    public LinkedListDeque(T item){
        sentinel = new ListNode<>(sentinel, 99, sentinel);
        sentinel.next = new ListNode<T>(sentinel, item, sentinel);
        size = 1;
    }*/

    /* Add item to the front of the list */
    public void addFirst(T item) {
        ListNode<T> p = sentinel.next;
        sentinel.next = new ListNode<T>(sentinel, item, sentinel.next);
        p.prev = sentinel.next;
        size += 1;

    }

    /*Add item to the end of the list */
    public void addLast(T item) {
        ListNode<T> p = sentinel.prev;
        sentinel.prev = new ListNode<T>(sentinel.prev, item, sentinel);
        p.next = sentinel.prev;
        size += 1;
    }

    /* Return true if Deque is empty, false otherwise */
    public boolean isEmpty() {
        return ((sentinel.next == sentinel) && (size == 0));
    }

    /* Return the number of items in the Deque */
    public int size() {
        return size;
    }

    /* Prints the items in the deque from first to last, separated by space */
    public void printDeque() {
        ListNode<Integer> p = sentinel;
        while (p.next != sentinel) {
            System.out.print(p.next.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    /* Removes and returns the first item at the front of the deque.
       If not such item exists, return null */
    public T removeFirst() {
        if (sentinel.next == sentinel){
            return null;
        }
        size -= 1;
        ListNode<T> p = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return p.item;
    }

    /* Removes and returns the last item at the back of the deque.
       If not such item exists, return null */
    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }
        size -= 1;
        ListNode<T> p = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return p.item;
    }

    /* Gets the item at the given index, where 0 is the front,
    1 is the next item, and so forth. If no such item exists, returns null.*/
    public T get(int index) {
        if ((index < 0) || (index > size - 1)) {
            return null;
        }
        int counter = 0;
        ListNode<T> p = sentinel.next;
        while (counter != index) {
            p = p.next;
            counter += 1;
        }
        return p.item;
    }

    /** Creates a deep copy of other
     * @source Josh Hug https://www.youtube.com/watch?v=JNroRiEG7U4
     */
    public LinkedListDeque(LinkedListDeque other) {
        sentinel = new ListNode<>(null, 99, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
        for(int i = 0; i < other.size(); i += 1) {
            addLast((T) other.get(i));
        }
    }

    /* Gets the item at the given index using recursion */
    public T getRecursive(int index) {
        if ((index < 0) || (index > size - 1)) {
            return null;
        }
        ListNode<T> p = sentinel.next;
        if (index == 0) {
            return p.item;
        } else {
            LinkedListDeque<T> res = new LinkedListDeque<>(this);
            res.removeFirst();
            return res.getRecursive(index - 1);
        }
    }

}