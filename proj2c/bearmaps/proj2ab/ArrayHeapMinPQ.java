package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.NoSuchElementException;

/** @author Gerry Bong
 *  Min PQ Heap implementation using ArrayList
 *  Top item of the heap has the lowest priority
 *  Hashtable is used to store the location of each key to speed up find() and contain()
 */

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    ArrayList<T> keyArr;
    ArrayList<Double> priArr;
    Hashtable<T, Integer> keyTable;                // create a HashSet so contains can run O(1)
    int arrSize;                      // size of array

    /* Constructs a new ArrayHeapMinPQ, first element of array is assigned an arbitrary null
    value. Actual elements start at index 1 which makes writing code for accessing children
    and parents simpler.*/

    public ArrayHeapMinPQ() {
        keyArr = new ArrayList<>();
        keyArr.add(null);
        priArr = new ArrayList<>();
        priArr.add(99999.0);
        arrSize = 0;
        keyTable = new Hashtable<>();
    }

    @Override
    public void add(T item, double priority) {
        arrSize += 1;
        keyArr.add(arrSize, item);
        priArr.add(arrSize, priority);
        keyTable.put(item, arrSize);
        swim(arrSize);
    }

    // swap key and priority value in index k and j of key and priority arrays
    private void swap(int k, int j) {
        T temp = keyArr.get(k);
        keyTable.replace(temp, j);            //change hashtable first
        keyTable.replace(keyArr.get(j), k);

        keyArr.set(k, keyArr.get(j));         //then swap values in heap
        keyArr.set(j, temp);

        double priTemp = priArr.get(k);       //swap values in other heap
        priArr.set(k, priArr.get(j));
        priArr.set(j, priTemp);
    }

    //swim up the tree by swapping node with parent node if parent priority is more
    private void swim(int k) {
        // this method exits when the first element of the array is reached
        // because the implementation leaves the first index blank.
        if (k <= 1) {
            return;
        }
        if (priArr.get(parent(k)) > priArr.get(k)) {
            swap(k, parent(k));
            swim(parent(k));
        }
    }

    private int parent(int k) {
        return k / 2;
    }

    private int leftChild(int k) {
        return k * 2;
    }

    private int rightChild(int k) {
        return k * 2 + 1;
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return keyTable.containsKey(item);
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T getSmallest() {
        if (arrSize == 0) {
            throw new NoSuchElementException();
        }
        return keyArr.get(1);
    }

    /* Sink the node to the right place. if there is a tie in priority, sink node to
       the left branch. always swaps with the node with lowest priority.
     */

    private void sink(int k) {
        // node is at the bottom node (leaf)
        if (leftChild(k) > arrSize) {
            return;
        } else if (rightChild(k) > arrSize) {   // node has left child but no right child
            if (priArr.get(k) > priArr.get(leftChild(k))) {
                swap(k, leftChild(k));
                sink(leftChild(k));
            }
        } else {                                // node has both left and right children
            if (priArr.get(k) > priArr.get(leftChild(k))
                    && priArr.get(leftChild(k)) <= priArr.get(rightChild(k))) {
                swap(k, leftChild(k));
                sink(leftChild(k));
            } else if (priArr.get(k) > priArr.get(rightChild(k))
                    && priArr.get(rightChild(k)) < priArr.get(leftChild(k))) {
                swap(k, rightChild(k));
                sink(rightChild(k));
            }
        }
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty.
    * Swap first item with last item, remove last item and then sink new first item */
    @Override
    public T removeSmallest() {
        if (arrSize == 0) {
            throw new NoSuchElementException();
        }
        T smallest = keyArr.get(1);
        swap(1, arrSize);
        keyTable.remove(keyArr.get(arrSize)); // remove item in hashtable
        keyArr.set(arrSize, null);  // remove last item
        priArr.set(arrSize, null);
        arrSize -= 1;
        sink(1);
        return smallest;
    }

    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return arrSize;
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    //@Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int id = keyTable.get(item); //get index of item in heap array
        priArr.set(id, priority);    // change value of priority
        swim(id);
        sink(id);
    }
//
//    /* Helper method for changePriority. Traverse down the tree until the item is found.
//    *  If the node has no child, function aborts. If */
//
//    private void changePriorityHelp(T item, double priority, int k) {
//        if (item.equals(keyArr.get(k))) {
//            priArr.set(k, priority);
//            if (priority < priArr.get(parent(k))) {
//                swim(k);
//            } else {
//                sink(k);
//            }
//
//        } else if (leftChild(k) > arrSize) {   // node has no children
//            return;
//
//        } else if (rightChild(k) > arrSize) {  // node only has left child
//                changePriorityHelp(item, priority, leftChild(k));
//
//        } else {                               // node has left and right children
//                changePriorityHelp(item, priority, leftChild(k));
//                changePriorityHelp(item, priority, rightChild(k));
//            if (priority <= priArr.get(leftChild(k)) &&
//                priority > priArr.get(rightChild(k))) {
//                changePriorityHelp(item, priority, leftChild(k));
//
//            } else if (priority <= priArr.get(rightChild(k)) &&
//                    priority > priArr.get(leftChild(k))) {
//                changePriorityHelp(item, priority, rightChild(k));
//
//            } else if (priority <= priArr.get(rightChild(k)) &&
//                    priority <= priArr.get(leftChild(k))) {
//                changePriorityHelp(item, priority, leftChild(k));
//                changePriorityHelp(item, priority, rightChild(k));
//            }
//        }
//    }
}
