package hsbc.kata.throttle;

public interface IThrottler {

    ThrottleResult shouldProceed();

    void notifyWhenCanProceed(Callback callback);

    void shutdown();
}
