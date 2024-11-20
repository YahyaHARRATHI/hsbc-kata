package hsbc.kata.bus;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadEventBus implements EventBus {

    @Getter
    private final Map<String, List<FilteredSubscriber>> subscribers = new ConcurrentHashMap<>();
    private final Map<String, Object> coalescedEvents = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void subscribe(String topic, EventListener listener) {
        addSubscriberForFilteredEvents(topic, listener, _ -> true);
    }

    public void addSubscriberForFilteredEvents(String topic, EventListener listener, EventFilter filter) {
        subscribers.computeIfAbsent(topic, _ -> new CopyOnWriteArrayList<>())
                .add(new FilteredSubscriber(listener, filter));
    }

    @Override
    public void publishCoalescedEvent(String topic, Object event) {
        coalescedEvents.put(topic, event);
        notifySubscribersForCoalescedEvent(topic);
    }

    private void notifySubscribersForCoalescedEvent(String topic) {
        Object event = coalescedEvents.get(topic);
        if (event != null) {
            List<FilteredSubscriber> topicSubscribers = subscribers.get(topic);
            if (topicSubscribers != null) {
                for (FilteredSubscriber subscriber : topicSubscribers) {
                    executor.submit(() -> {
                        if (subscriber.getFilter().filter(event)) {
                            subscriber.getListener().onEvent(event);
                        }
                    });
                }
            }
        }
    }

    public void unsubscribe(String topic, EventListener listener) {
        subscribers.computeIfPresent(topic, (_, topicSubscribers) -> {
            topicSubscribers.removeIf(subscriber -> subscriber.getListener() == listener);
            return topicSubscribers.isEmpty() ? null : topicSubscribers;
        });
    }

    public void publishEvent(String topic, Object event) {
        subscribers.computeIfPresent(topic, (_, topicSubscribers) -> {
            for (FilteredSubscriber subscriber : topicSubscribers) {
                executor.submit(() -> {
                    if (subscriber.getFilter().filter(event)) {
                        subscriber.getListener().onEvent(event);
                    }
                });
            }
            return topicSubscribers;
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}

