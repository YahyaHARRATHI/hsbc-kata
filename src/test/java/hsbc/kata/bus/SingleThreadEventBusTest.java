package hsbc.kata.bus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class SingleThreadEventBusTest {

    private SingleThreadEventBusImpl eventBus;
    private EventListener mockListener;

    @BeforeEach
    public void setUp() {
        eventBus = new SingleThreadEventBusImpl();
        mockListener = mock(EventListener.class);
    }

    @Test
    public void shouldAddListenerToTopicWhenSubscribeIsCalled() {
        String topic = "testTopic";
        eventBus.subscribe(topic, mockListener);

        eventBus.publishEvent(topic, "event1");
        eventBus.publishEvent(topic, "event2");

        verify(mockListener, times(1)).onEvent("event2");
        verify(mockListener, times(1)).onEvent("event1");
    }

    @Test
    public void shouldRemoveListenerFromTopicWhenUnsubscribeIsCalled() {
        String topic = "unsubscribeTopic";
        eventBus.subscribe(topic, mockListener);

        eventBus.unsubscribe(topic, mockListener);

        Object event = new Object();
        eventBus.publishEvent(topic, event);

        verify(mockListener, never()).onEvent(event);
    }
}
