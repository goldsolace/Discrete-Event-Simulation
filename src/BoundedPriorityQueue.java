import java.util.NoSuchElementException;

/**
 * Abstract PlanarShape which are comparable with behaviour to calculate area and distance from origin.
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */

public class BoundedPriorityQueue<E extends Comparable<E>> extends LinkedList<E> {

    int capacity;

    public BoundedPriorityQueue(int capacity) {
        super();
        this.capacity = capacity;
    }

    /**
     * Insert an element in ascending sorted order
     *
     * @param element to insert
     */
    private void insertInOrder(E element) {
        Node<E> current = getSentinel();
        while ((current = current.next) != getSentinel()) {
            if (element.compareTo(current.element) < 0) {
                insert(element, current);
                return;
            }
        }
        append(element);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean isFull() {
        return size() == capacity;
    }

    public int capcity() {
        return capacity;
    }

    public int remainingCapacity() {
        return capacity - size();
    }

    public boolean offer(E e) {
        if (isFull()) throw new IllegalStateException("Queue is at max capacity");
        insertInOrder(e);
        return true;
    }

    public E poll() {
        return remove();
    }

    public E peek() {
        if (size() == 0) throw new NoSuchElementException("Queue is empty");
        return getSentinel().next.element;
    }
}