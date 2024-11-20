package hsbc.kata.bus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class SingleThreadEventBusImpl implements EventBus {

    private final Map<String, List<FilteredSubscriber>> subscribers = new HashMap<>();

    public void subscribe(String topic, EventListener listener) {
        addSubscriberForFilteredEvents(topic, listener, _ -> true);
    }

    @Override
    public void addSubscriberForFilteredEvents(String topic, EventListener listener, EventFilter filter) {
        subscribers.computeIfAbsent(topic, _ -> new CopyOnWriteArrayList<>())
                .add(new FilteredSubscriber(listener, filter));
    }

    @Override
    public void publishCoalescedEvent(String topic, Object event) {
    }

    public void unsubscribe(String topic, EventListener listener) {
        List<FilteredSubscriber> topicSubscribers = subscribers.get(topic);
        if (topicSubscribers != null) {
            topicSubscribers.removeIf(subscriber -> subscriber.getListener() == listener);
            if (topicSubscribers.isEmpty()) {
                subscribers.remove(topic);
            }
        }
    }

    public void publishEvent(String topic, Object event) {
        List<FilteredSubscriber> topicSubscribers = subscribers.get(topic);
        if (topicSubscribers != null) {
            for (FilteredSubscriber subscriber : topicSubscribers) {
                if (subscriber.getFilter().filter(event)) {
                    subscriber.getListener().onEvent(event);
                }
            }
        }
    }

    @Override
    public void shutdown() {
        subscribers.clear();
    }
}
