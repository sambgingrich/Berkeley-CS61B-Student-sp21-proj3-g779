package deque;

public class LinkedListDeque<T> {
    private ThingNode sentinel;
    private int size;


    private class ThingNode {
        public ThingNode previous;
        public T item;
        public ThingNode next;

        public ThingNode(ThingNode p, T  i, ThingNode n) {
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

    public LinkedListDeque(T item) {
        sentinel = new ThingNode(null, null, null);
        sentinel.next = new ThingNode(sentinel, item, sentinel);
        sentinel.previous = sentinel.next;
        size = 1;
    }

    /** Adds item of type T to the front of the deque. */
    public void addFirst(T item){
        sentinel.next = new ThingNode(sentinel, item, sentinel.next);
        size += 1;
        if (size == 1){
            sentinel.previous = sentinel.next;
        }
    }

    /** Adds item of type T to the back of the deque. */
    public void addLast(T item){
        sentinel.previous.next = new ThingNode(sentinel.previous, item , sentinel);
        sentinel.previous = sentinel.previous.next;
        size += 1;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line. */
    public void printDeque(){
        ThingNode p  = sentinel;
        while (p.next != sentinel){
            p = p.next;
            System.out.print(p.item + " ");
        }
        System.out.println();
    }

    /**Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.*/
    public T removeFirst(){
        if (sentinel.next == sentinel) {
            return null;
        } else {
            size -= 1;
            T returnval = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.previous = sentinel;
            return returnval;
        }
    }

    /**Removes and returns the item at the back of the deque.
     * If no such item exists, returns null */
    public T removeLast(){
        if (sentinel.next == sentinel) {
            return null;
        } else {
            size -= 1;
            T returnval = sentinel.previous.item;
            sentinel.previous = sentinel.previous.previous;
            sentinel.previous.next = sentinel;
            return returnval;
        }
    }

    /** Gets the item at that given index, where 0 is the front,
     *  1 is the next item, and so forth. If no such item exists,
     *  returns null. Does not alter the Deque.
     */
    public T get(int index){
        int i =  0;
        ThingNode p = sentinel.next;
        while (i < index){
            i += 1;
            p = p.next;
        }
        return p.item;
    }

    /** Same as get, but recursive. */
    public T getRecursive(int index){
        System.out.println("Not sure about this one yet");
        return null;
    }

}





























