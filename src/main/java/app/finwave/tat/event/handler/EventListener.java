package app.finwave.tat.event.handler;

import app.finwave.tat.event.Event;

public interface EventListener<T extends Event<?>> {
    void event(T event);
}
