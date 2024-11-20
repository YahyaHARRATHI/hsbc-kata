package hsbc.kata.bus;

import lombok.Getter;

@Getter
public class FilteredSubscriber {

    private final EventListener listener;
    private final EventFilter filter;

    public FilteredSubscriber(EventListener listener, EventFilter filter) {
        this.listener = listener;
        this.filter = filter;
    }
}
