package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int end;
    private int start;

    /** Creates an empty deque. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        end = 5;
        start = 4;
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int iteratorPos;
        ArrayDequeIterator() {
            iteratorPos = 0;
        }
        public boolean hasNext() {
            return iteratorPos < size();
        }

        public T next() {
            T returnItem = get(iteratorPos);
            iteratorPos += 1;
            return returnItem;
        }
    }

    @Override
    public int size() {
        return end - start - 1;
    }

    private boolean isEmptyAD() {
        return size() == 0;
    }

    /** Resizes the array so it is large enough
     * to hold the entire deque. If front is true,
     * adds empty values to the front. Otherwise
     * adds values to the back of the list.
     */
    private void resize(boolean front, boolean add) {
        int oldSize = size();
        if (add) {
            T[] newArray = (T[]) new Object[items.length * 2];
            if (front) {
                start = newArray.length / 2 - 1;
                System.arraycopy(items, 1, newArray, start + 1, oldSize);
                end = start + oldSize + 1;
            } else {
                System.arraycopy(items, 0, newArray, 0, items.length);
            }
            items = newArray;
        } else {
            T[] newArray = (T[]) new Object[items.length / 2];
            System.arraycopy(items, start + 1, newArray, oldSize / 2, oldSize);
            start = oldSize / 2 - 1;
            end = start + oldSize + 1;
            items = newArray;
        }
    }

    /** Adds item of type T to the front of the deque. */
    @Override
    public void addFirst(T item) {
        if (start == 0) {
            resize(true, true);
        }
        items[start] = item;
        start -= 1;
    }

    /** Adds item of type T to the back of the deque. */
    @Override
    public void addLast(T item) {
        if (end == items.length) {
            resize(false, true);
        } /*else if (start == end && items[start] != null) {
            end += 1;
        } */
        items[end] = item;
        end += 1;
    }


    /** Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.*/
    @Override
    public void printDeque() {
        int i  = start;
        while (i <= end) {
            System.out.print(items[i]);
            i += 1;
        }
        System.out.println();
    }


    /**Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.*/
    @Override
    public T removeFirst() {
        if (isEmptyAD()) {
            return null;
        }
        T first = get(0);
        items[start + 1] = null;
        if (start != end) {
            start += 1;
        }
        //Check if array should downsize
        if (size() > 1 && items.length > size() * 4) {
            resize(true, false);
        }
        return first;
    }


    /**Removes and returns the item at the back of the deque.
     * If no such item exists, returns null */
    @Override
    public T removeLast() {
        if (isEmptyAD()) {
            return null;
        }
        T last = get(size() - 1);
        items[end - 1] = null;
        if (end > start) {
            end -= 1;
        }
        //Check if array should downsize
        if (size() > 1 && items.length > size() * 4) {
            resize(false, false);
        }
        return last;
    }


    /** Gets the item at that given index, where 0 is the front,
     *  1 is the next item, and so forth. If no such item exists,
     *  returns null. Does not alter the Deque.*/
    @Override
    public T get(int index) {
        if (index > size() || index < 0) {
            return null;
        }
        return items[start + index + 1];
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
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
}
