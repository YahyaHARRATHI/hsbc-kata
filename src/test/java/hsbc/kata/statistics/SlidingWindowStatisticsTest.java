package hsbc.kata.statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlidingWindowStatisticsTest {
    @Test
    public void testAddAndRetrieveStatistics() {
        SlidingWindowStatistics stats = new SlidingWindowStatistics(5);

        stats.add(5);
        stats.add(10);
        stats.add(15);

        Statistics latest = stats.getLatestStatistics();

        assertEquals(10.0, latest.getMean(), 0.01);
        assertEquals(5, latest.getMode());
        assertEquals(10.0, latest.getMedian(), 0.01);
    }


    @Test
    public void testModeCalculation() {
        SlidingWindowStatistics stats = new SlidingWindowStatistics(10);

        stats.add(1);
        stats.add(2);
        stats.add(2);
        stats.add(3);

        Statistics latest = stats.getLatestStatistics();

        assertEquals(2, latest.getMode());
    }

    @Test
    public void testPercentileCalculation() {
        SlidingWindowStatistics stats = new SlidingWindowStatistics(5);

        stats.add(10);
        stats.add(20);
        stats.add(30);
        stats.add(40);
        stats.add(50);

        Statistics latest = stats.getLatestStatistics();

        assertEquals(20, latest.getPercentiles(25).get(0));
        assertEquals(50, latest.getPercentiles(100).get(0));
    }
}