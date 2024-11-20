package hsbc.kata.handler;

public class EventBusException extends RuntimeException {
    public EventBusException(String message, Throwable cause) {
        super(message, cause);
    }
}
