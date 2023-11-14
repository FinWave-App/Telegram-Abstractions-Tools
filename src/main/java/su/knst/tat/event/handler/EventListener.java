package su.knst.tat.event.handler;

import su.knst.tat.event.Event;

public interface EventListener<T extends Event<?>> {
    void event(T event);
}
