package su.knst.tat.event.handler;

import su.knst.tat.event.Event;

public interface EventValidator <T extends Event<?>> {
    boolean validate(T event);
}