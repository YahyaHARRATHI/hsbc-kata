package hsbc.kata.statistics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsCalculator {

    public static Statistics calculate(Integer[] values) {
        if (values == null || values.length == 0) {
            return new Statistics(0.0, 0, 0.0, Collections.emptyList());
        }

        List<Integer> sortedValues = Arrays.stream(values).sorted().collect(Collectors.toList());
        double mean = sortedValues.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        int mode = findMode(sortedValues);
        double median = calculateMedian(sortedValues);

        return new Statistics(mean, mode, median, sortedValues);
    }

    private static int findMode(List<Integer> values) {
        Map<Integer, Long> frequencyMap = values.stream()
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));

        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    private static double calculateMedian(List<Integer> values) {
        int size = values.size();
        if (size % 2 == 0) {
            return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
        } else {
            return values.get(size / 2);
        }
    }
}

