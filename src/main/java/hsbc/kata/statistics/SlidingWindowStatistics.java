package hsbc.kata.statistics;

import hsbc.kata.bus.MultiThreadEventBus;

public class SlidingWindowStatistics {

    private final SlidingWindow slidingWindow;
    private final MultiThreadEventBus eventBus;

    public SlidingWindowStatistics(int windowSize) {
        this.slidingWindow = new SlidingWindow(windowSize);
        this.eventBus = new MultiThreadEventBus();
    }

    public void add(int measurement) {
        slidingWindow.add(measurement);
        Statistics latestStats = StatisticsCalculator.calculate(slidingWindow.getValues());
        eventBus.publishEvent("statisticsUpdated", latestStats);
    }

    public void subscribe(StatisticsSubscriber subscriber) {
        eventBus.subscribe("statisticsUpdated", event -> {
            if (event instanceof Statistics) {
                subscriber.onStatisticsUpdated((Statistics) event);
            }
        });
    }

    public Statistics getLatestStatistics() {
        return StatisticsCalculator.calculate(slidingWindow.getValues());
    }

    public void shutdown() {
        eventBus.shutdown();
    }
}
