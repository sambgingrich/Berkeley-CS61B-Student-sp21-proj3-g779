package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator comp;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        comp = c;
    }

    public T max() {
        T biggest = get(0);
        for (int i = 1; i < size(); i++) {
            if (comp.compare(biggest, get(i)) > 0) {
                biggest = get(i);
            }
        }
        return biggest;
    }

    public T max(Comparator<T> c) {
        T biggest = get(0);
        for (int i = 1; i <= size(); i++) {
            if (c.compare(get(i), biggest) > 0) {
                biggest = (get(i));
            }
        }
        return biggest;
    }
}
