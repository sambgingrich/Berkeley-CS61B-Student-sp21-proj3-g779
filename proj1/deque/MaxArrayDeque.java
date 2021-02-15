package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque {
    private Comparator comp;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        comp = c;
    }

    public T max() {
        T biggest = (T) get(0);
        for (int i = 1; i <= size(); i++) {
            if (comp.compare((T) get(i), biggest) > 0) {
                biggest = (T) get(i);
            }
        }
        return biggest;
    }

    public T max(Comparator<T> c) {
        T biggest = (T) get(0);
        for (int i = 1; i <= size(); i++) {
            if (c.compare((T) get(i), biggest) > 0) {
                biggest = (T) get(i);
            }
        }
        return biggest;
    }
}
