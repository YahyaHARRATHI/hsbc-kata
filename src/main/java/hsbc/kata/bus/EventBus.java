package hsbc.kata.bus;

public interface EventBus {
    void subscribe(String topic, EventListener listener);

    void addSubscriberForFilteredEvents(String topic, EventListener listener, EventFilter filter);

    void publishCoalescedEvent(String topic, Object event);

    void unsubscribe(String topic, EventListener listener);

    void publishEvent(String topic, Object event);

    void shutdown();
}