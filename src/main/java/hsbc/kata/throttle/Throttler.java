package hsbc.kata.throttle;

import java.util.concurrent.*;

public class Throttler implements IThrottler {
    private final int maxOperations;
    private final long timeWindowMillis;
    private final BlockingQueue<Long> requestTimestamps;
    private final ScheduledExecutorService scheduler;
    private final ExecutorService notificationExecutor;

    private final Object lock = new Object();


    public Throttler(int maxOperations, long timeWindowMillis) {
        this.maxOperations = maxOperations;
        this.timeWindowMillis = timeWindowMillis;
        this.requestTimestamps = new LinkedBlockingQueue<>(maxOperations);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.notificationExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public ThrottleResult shouldProceed() {
        long currentTime = System.currentTimeMillis();
        // size() n'est pas thread safe
        synchronized (lock) {
            cleanupOldRequests(currentTime);
            if (requestTimestamps.size() < maxOperations) {
                requestTimestamps.offer(currentTime);
                return ThrottleResult.PROCEED;
            } else {
                return ThrottleResult.DO_NOT_PROCEED;
            }
        }
    }


    @Override
    public void notifyWhenCanProceed(Callback callback) {
        if (shouldProceed() == ThrottleResult.PROCEED) {
            callback.onProceed();
        } else {
            scheduler.schedule(() -> {
                waitUntilCanProceed(callback);
            }, 1, TimeUnit.MILLISECONDS);
        }
    }

    private void waitUntilCanProceed(Callback callback) {
        try {
            while (shouldProceed() == ThrottleResult.DO_NOT_PROCEED) {
                Thread.sleep(100);
            }
            callback.onProceed();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void cleanupOldRequests(long currentTime) {
        while (!requestTimestamps.isEmpty() && currentTime - requestTimestamps.peek() > timeWindowMillis) {
            requestTimestamps.poll();
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        notificationExecutor.shutdown();
    }
}
