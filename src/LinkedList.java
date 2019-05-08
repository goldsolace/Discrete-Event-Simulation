import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * LinkedList Class - Implementation of an iterable circular doubly-linked list
 * limited to planar shape objects.
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */

public class LinkedList<E extends PlanarShape> implements Iterable<E> {

    private Node<E> sentinel;
    private int size = 0;
    private int modCount = 0;

    /**
     * Constructs an empty linked list
     */
    public LinkedList() {
        sentinel = new Node<>(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    /**
     * Returns the sentinel node
     */
    protected Node<E> getSentinel() {
        return sentinel;
    }

    /**
     * Returns the size of the list
     *
     * @return the number of elements
     */
    public int size() {
        return size;
    }

    /**
     * Prepend data to the front of the list
     *
     * @param element data to prepend
     */
    public void prepend(E element) {
        insert(element, sentinel.next);
    }

    /**
     * Append data to the end of the list
     *
     * @param element data to append
     */
    public void append(E element) {
        insert(element, sentinel);
    }

    /**
     * Insert element before a node
     *
     * @param element to insert
     * @param node    node to insert before
     */
    protected void insert(E element, Node<E> node) {
        if (node == null)
            throw new NullPointerException();
        Node<E> newNode = new Node<>(node.prev, element, node);
        node.prev.next = newNode;
        node.prev = newNode;
        size++;
        modCount++;
    }

    /**
     * Remove the first element of the list
     *
     * @return the head of the list
     */
    public E remove() {
        if (size == 0)
            throw new NoSuchElementException();
        Node<E> head = sentinel.next;
        head.next.prev = sentinel;
        sentinel.next = head.next;
        head.next = null;
        head.prev = null;
        size--;
        modCount++;
        return head.element;
    }

    /**
     * Remove an element from the list
     *
     * @param node node to remove
     */
    protected E removeNode(Node<E> node) {
        if (node == null)
            throw new NullPointerException();
        if (size == 0)
            throw new NoSuchElementException();
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;
        size--;
        modCount++;
        return node.element;
    }

    /**
     * Override toString to print the list
     *
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        Node<E> current = sentinel;
        while ((current = current.next) != sentinel) {
            sb.append(current.element);
            if (current != sentinel.prev)
                sb.append("\n  ");

        }
        sb.append(" }");
        return sb.toString();
    }

    /**
     * Return an iterator for the list
     *
     * @return Iterator<E> new instance of LinkedListIterator
     */
    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    /**
     * Encapsulated LinkedListIterator
     */
    private class LinkedListIterator implements Iterator<E> {

        private Node<E> next = sentinel.next;
        private Node<E> returned;
        private int nextIndex = 0;
        private int expectedModCount = modCount;

        /**
         * Check if iterator has a next element
         *
         * @return true if another element
         */
        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        /**
         * Increment iterator to next element
         *
         * @throws NoSuchElementException if no next element
         * @return next element
         */
        @Override
        public E next() {
            checkForModCount();
            if (!hasNext())
                throw new NoSuchElementException();
            returned = next;
            next = next.next;
            nextIndex++;
            return returned.element;
        }

        /**
         * Remove the last returned element from the list
         *
         * @throws IllegalStateException if iterator has not returned an element
         */
        @Override
        public void remove() {
            checkForModCount();
            if (returned == null)
                throw new IllegalStateException();
            Node<E> lastNext = returned.next;
            removeNode(returned);
            if (next == returned)
                next = lastNext;
            else
                nextIndex--;
            returned = null;
            expectedModCount++;
        }

        /**
         * Check if list has been modified outside of iterator
         *
         * @throws ConcurrentModificationException on outside modification
         */
        private void checkForModCount() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * Encapsulated doubly linked node class to store elements
     */
    protected static class Node<E> {
        protected E element;
        protected Node<E> next;
        protected Node<E> prev;

        protected Node(Node<E> prev, E element, Node<E> next) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }
}
