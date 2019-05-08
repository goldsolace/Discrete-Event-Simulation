/**
 * SortedList Class - Extension of LinkedList to support sorting elements
 * using the insertion sort algorithm.
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */

public class SortedList<E extends PlanarShape> extends LinkedList<E> {

	/**
	 * Constructs an empty sorted list.
	 */
	public SortedList() {
		super();
	}

	/**
	 * Constructs a sorted list from an existing list.
	 */
	public SortedList(LinkedList<E> linkedList) {
		super();
		insertInOrder(linkedList);
	}

	/**
	 * Insert another list in ascending sorted order
	 *
	 * @param linkedList of elements to be inserted
	 */
	public void insertInOrder(LinkedList<E> linkedList) {
		if (linkedList == null)
			return;
		Node<E> current = linkedList.getSentinel();
		while ((current = current.next) != linkedList.getSentinel()) {
			insertInOrder(current.element);
		}
	}

	/**
	 * Insert an element in ascending sorted order
	 *
	 * @param element to insert
	 */
	public void insertInOrder(E element) {
		Node<E> current = getSentinel();
		while ((current = current.next) != getSentinel()) {
			if (element.compareTo(current.element) < 0) {
				insert(element, current);
				return;
			}
		}
		append(element);
	}

	/**
	 * Override toString to print the list
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return super.toString();
	}
}
