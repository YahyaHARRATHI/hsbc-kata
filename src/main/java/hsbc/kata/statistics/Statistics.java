package hsbc.kata.statistics;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class Statistics {
    private final double mean;
    private final int mode;
    private final double median;
    private final List<Integer> data;

    public Statistics() {
        this.mean = 0;
        this.mode = 0;
        this.median = 0;
        this.data = Collections.emptyList();
    }

    public Statistics(double mean, int mode, double median, List<Integer> data) {
        this.mean = mean;
        this.mode = mode;
        this.median = median;
        this.data = data;
    }

    public List<Integer> getPercentiles(int percentile) {
        int index = (int) Math.ceil((percentile / 100.0) * data.size()) - 1;
        return index >= 0 && index < data.size() ? Collections.singletonList(data.get(index)) : Collections.emptyList();
    }
}
