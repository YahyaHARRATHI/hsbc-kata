package hsbc.kata.bus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class MultiThreadEventBusTest {

    private MultiThreadEventBus eventBus;

    @BeforeEach
    public void setUp() {
        eventBus = new MultiThreadEventBus();
    }

    @AfterEach
    public void tearDown() {
        eventBus.shutdown();
    }

    @Test
    public void shouldCorrectlyAddSubscriberToTopic() {
        String topic = "testTopic";
        EventListener mockListener = mock(EventListener.class);

        eventBus.subscribe(topic, mockListener);

        Assertions.assertTrue(eventBus.getSubscribers().containsKey(topic));
        Assertions.assertEquals(eventBus.getSubscribers().get(topic).get(0).getListener(), mockListener);

    }

    @Test
    public void shouldRemoveSubscriberAndDeleteTopicIfNoSubscribersRemain() {
        String topic = "testTopic";
        EventListener mockListener = mock(EventListener.class);

        eventBus.subscribe(topic, mockListener);
        eventBus.unsubscribe(topic, mockListener);

        Assertions.assertFalse(eventBus.getSubscribers().containsKey(topic));

    }

    @Test
    public void shouldHandlePublishingEventToMultipleSubscribersOnSameTopic() {
        String topic = "sharedTopic";
        EventListener mockListener1 = mock(EventListener.class);
        EventListener mockListener2 = mock(EventListener.class);
        Object event = new Object();

        eventBus.subscribe(topic, mockListener1);
        eventBus.subscribe(topic, mockListener2);
        eventBus.publishEvent(topic, event);

        try {
            sleep(100);
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        }

        verify(mockListener1, times(1)).onEvent(event);
        verify(mockListener2, times(1)).onEvent(event);
    }


    @Test
    public void shouldHandlePublishingEventToMultipleSubscribersOnSameTopic2() {
        String topic = "sharedTopic";
        EventListener mockListener1 = mock(EventListener.class);
        EventListener mockListener2 = mock(EventListener.class);
        EventListener mockListener3 = mock(EventListener.class);
        Object event = new Object();

        eventBus.subscribe(topic, mockListener1);
        eventBus.subscribe(topic, mockListener2);
        eventBus.subscribe(topic, mockListener3);
        eventBus.publishEvent("sharedTopic1", event);
        eventBus.publishEvent("sharedTopic2", event);
        eventBus.publishEvent("sharedTopic3", event);

        try {
            sleep(100);
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        }

        verify(mockListener1, times(1)).onEvent(event);
        verify(mockListener2, times(1)).onEvent(event);
        verify(mockListener3, times(1)).onEvent(event);
    }

    @Test
    public void testFilteredEventSubscription() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean received = new AtomicBoolean(false);

        eventBus.addSubscriberForFilteredEvents(
                "topic1",
                _ -> {
                    received.set(true);
                    latch.countDown();
                },
                event -> event instanceof String && ((String) event).contains("pass")
        );

        eventBus.publishEvent("topic1", "This won't pass");
        eventBus.publishEvent("topic1", "This will pass");

        latch.await();

        assertTrue(received.get());
        eventBus.shutdown();
    }
}
