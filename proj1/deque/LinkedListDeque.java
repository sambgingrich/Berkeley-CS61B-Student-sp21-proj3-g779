package deque;
import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private ThingNode sentinel;
    private int size;

    private class ThingNode {
        private ThingNode previous;
        private T item;
        private ThingNode next;

        ThingNode(ThingNode p, T  i, ThingNode n) {
            previous = p;
            item = i;
            next = n;
        }
    }

    public LinkedListDeque() {
        sentinel = new ThingNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.previous = sentinel.next;
        size = 0;
    }

    /* returns an iterator into self */
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private ThingNode iteratorPos;
        LinkedListIterator() {
            iteratorPos = sentinel.next;
        }

        public boolean hasNext() {
            return iteratorPos.next != sentinel;
        }

        public T next() {
            T returnItem = iteratorPos.item;
            if (hasNext()) {
                iteratorPos = iteratorPos.next;
            }
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (other.getClass() != this.getClass()) {
            if (!(other instanceof Deque)) {
                return false;
            }
        }

        Deque<T> o = (Deque<T>) other;
        if (o.size() != size()) {
            return false;
        }

        for (int i = 0; i < size(); i++) {
            T otherItem = o.get(i);
            T thisItem = this.get(i);
            if (!otherItem.equals(thisItem)) {
                return false;
            }
        }
        return true;
    }

    /** Adds item of type T to the front of the deque. */
    @Override
    public void addFirst(T item) {
        sentinel.next.previous = new ThingNode(sentinel, item, sentinel.next);
        sentinel.next = sentinel.next.previous;
        size += 1;
    }

    /** Adds item of type T to the back of the deque. */
    @Override
    public void addLast(T item) {
        sentinel.previous.next = new ThingNode(sentinel.previous, item, sentinel);
        sentinel.previous = sentinel.previous.next;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line. */
    @Override
    public void printDeque() {
        ThingNode p  = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            System.out.print(p.item + " ");
        }
        System.out.println();
    }

    /**Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.*/
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T returnVal = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.previous = sentinel;
            size -= 1;
            return returnVal;
        }
    }

    /**Removes and returns the item at the back of the deque.
     * If no such item exists, returns null */
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T returnVal = sentinel.previous.item;
            sentinel.previous = sentinel.previous.previous;
            sentinel.previous.next = sentinel;
            size -= 1;
            return returnVal;
        }
    }

    /** Gets the item at that given index, where 0 is the front,
     *  1 is the next item, and so forth. If no such item exists,
     *  returns null. Does not alter the Deque.
     */
    @Override
    public T get(int index) {
        int i =  0;
        ThingNode p = sentinel.next;
        while (i < index) {
            i += 1;
            p = p.next;
        }
        return p.item;
    }

    /** Same as get, but recursive. */
    public T getRecursive(int index) {
        return getRecursivehelper(index, sentinel.next);
    }

    private T getRecursivehelper(int index, ThingNode current) {
        if (index == 0) {
            return current.item;
        }
        return getRecursivehelper(index - 1, current.next);
    }

}





























