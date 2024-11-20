package hsbc.kata.statistics;

import java.util.Deque;
import java.util.LinkedList;

public class SlidingWindow {

    private final int maxSize;
    private final Deque<Integer> window = new LinkedList<>();

    public SlidingWindow(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized void add(int value) {
        window.addLast(value);
        if (window.size() > maxSize) {
            window.removeFirst();
        }
    }

    public synchronized Integer[] getValues() {
        return window.toArray(new Integer[0]);
    }
}

