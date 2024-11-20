package hsbc.kata.bus;

@FunctionalInterface
public interface EventFilter {
    boolean filter(Object event);
}
