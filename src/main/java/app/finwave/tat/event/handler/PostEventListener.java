package app.finwave.tat.event.handler;

import app.finwave.tat.event.Event;

public interface PostEventListener<T extends Event<?>> {
    void postEvent(T event, boolean someoneHandled);
}
