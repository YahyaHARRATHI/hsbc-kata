package hsbc.kata.bus;

import hsbc.kata.handler.EventBusException;

@FunctionalInterface
public interface EventSubscriber<T> {
    void onEvent(T event) throws EventBusException;
}
