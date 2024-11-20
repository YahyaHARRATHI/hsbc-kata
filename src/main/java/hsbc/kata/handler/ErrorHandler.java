package hsbc.kata.handler;

import hsbc.kata.bus.EventSubscriber;

public interface ErrorHandler {
    void handleError(Throwable error, Object event, EventSubscriber<?> subscriber);
}
