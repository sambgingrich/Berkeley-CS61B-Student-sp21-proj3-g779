package deque;

import java.util.Comparator;

public class AlphabeticalComparator implements Comparator {
    public int compare(Object a, Object b) {
        return ((String) a).compareTo((String) b);
    }
}
