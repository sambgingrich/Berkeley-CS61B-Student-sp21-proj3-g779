package deque;
import java.util.Comparator;

/*public class MaxArrayDeque<T> extends ArrayDeque {
    public MaxArrayDeque(Comparator<T> c) {
        super();
        Comparator comp = c;
    }

    public T max() {
        return max(comp);
    }

    public T max(Comparator<T> c) {
        T biggest = get(0);
        for (int i = 1; i <= size(); i++) {
            if (c.compare(get(i), biggest) > 0) {
                biggest = get(i);
            }
        }
        return biggest;
    }
}
*/