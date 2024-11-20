package hsbc.kata.throttle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class ThrottlerTest {

    private Throttler throttler;

    @BeforeEach
    public void setUp() {
        throttler = new Throttler(5, 1000);
    }

    @Test
    public void testShouldProceedUnderMaxRequestsLimit() {
        Throttler throttler = new Throttler(5, 1000);
        for (int i = 0; i < 5; i++) {
            ThrottleResult result = throttler.shouldProceed();
            assertEquals(ThrottleResult.PROCEED, result);
        }
    }

    @Test
    public void testTimeWindowWithDelayTime() {
        Throttler throttler = new Throttler(5, 1000);
        try {
            Thread.sleep(990);
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        }

        for (int i = 0; i < 5; i++) {
            ThrottleResult result = throttler.shouldProceed();
            assertEquals(ThrottleResult.PROCEED, result);
        }

        try {
            Thread.sleep(12);
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        }

        ThrottleResult result = throttler.shouldProceed();
        assertEquals(ThrottleResult.DO_NOT_PROCEED, result);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        }

        result = throttler.shouldProceed();
        assertEquals(ThrottleResult.PROCEED, result);
    }

    @Test
    public void testNotifyWhenCanProceedCallsCallback() throws InterruptedException {
        Throttler throttler = new Throttler(5, 1000);
        AtomicBoolean callbackCalled = new AtomicBoolean(false);

        Callback callback = () -> callbackCalled.set(true);

        throttler.notifyWhenCanProceed(callback);

        Thread.sleep(200);

        assertTrue(callbackCalled.get());

        throttler.shutdown();
    }
}
