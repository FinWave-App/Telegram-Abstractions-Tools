package app.finwave.tat.event.handler;

import app.finwave.tat.event.Event;

public interface EventValidator <T extends Event<?>> {
    boolean validate(T event);
}