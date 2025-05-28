package app.finwave.tat.event.handler;

import app.finwave.tat.event.Event;

public interface EventListener<T extends Event<?>> {
    boolean event(T event);
}
