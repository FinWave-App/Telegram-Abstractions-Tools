package su.knst.tat.event.handler;

import su.knst.tat.event.Event;

import java.util.ArrayList;
import java.util.HashMap;

public class EventHandler<T extends Event<?>> {
    protected HashMap<Class<T>, ArrayList<EventListener<T>>> eventsListeners = new HashMap<>();
    protected HashMap<Class<T>, EventValidator<T>> eventsValidators = new HashMap<>();

    protected EventHandler<T> handlerChild;

    public <X extends T> HandlerRemover registerListener(Class<X> type, EventListener<X> listener) {
        if (!eventsListeners.containsKey(type))
            eventsListeners.put((Class<T>) type, new ArrayList<>());

        eventsListeners.get(type).add((EventListener<T>) listener);

        return () -> eventsListeners.get(type).remove(listener);
    }

    public <X extends T> HandlerRemover setValidator(Class<X> type, EventValidator<X> validator) {
        eventsValidators.put((Class<T>)type, (EventValidator<T>) validator);

        return () -> eventsValidators.remove(type);
    }

    public void fire(T event) {
        if (eventsValidators.containsKey(event.getClass()) && !eventsValidators.get(event.getClass()).validate(event))
            return;

        if (handlerChild != null) {
            handlerChild.fire(event);

            return;
        }

        ArrayList<EventListener<T>> listeners = eventsListeners.get(event.getClass());

        if (listeners == null)
            return;

        for (EventListener<T> listener : new ArrayList<>(listeners))
            listener.event(event);
    }

    public void setChild(EventHandler<T> handler) {
        this.handlerChild = handler;
    }

    public void resetChild() {
        this.handlerChild = null;
    }
}
