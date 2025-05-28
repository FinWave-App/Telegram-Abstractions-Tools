package app.finwave.tat.event.handler;

import app.finwave.tat.event.ChatEvent;
import app.finwave.tat.event.Event;
import app.finwave.tat.utils.Stack;

import java.util.ArrayList;
import java.util.HashMap;

public class EventHandler<T extends Event<?>> {
    protected HashMap<Class<T>, ArrayList<EventListener<T>>> eventsListeners = new HashMap<>();

    protected HashMap<Class<T>, EventValidator<T>> eventsValidators = new HashMap<>();
    protected EventValidator<T> globalEventsValidator;

    protected HashMap<Class<T>, PostEventListener<T>> postEventsListeners = new HashMap<>();

    protected Stack<EventHandler<T>> handlerChildStack = new Stack<>();

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

    public <X extends T> HandlerRemover setPostEventsListener(Class<X> type, PostEventListener<X> validator) {
        postEventsListeners.put((Class<T>)type, (PostEventListener<T>) validator);

        return () -> postEventsListeners.remove(type);
    }

    public HandlerRemover setValidator(EventValidator<T> validator) {
        globalEventsValidator = validator;

        return () -> globalEventsValidator = null;
    }

    public boolean fire(T event) {
        Class<? extends Event> eventClass = event.getClass();
        boolean someoneAccept = false;

        try {
            if (globalEventsValidator != null && !globalEventsValidator.validate(event))
                return someoneAccept;

            if (eventsValidators.containsKey(eventClass) && !eventsValidators.get(eventClass).validate(event))
                return someoneAccept;

            EventHandler<T> child = handlerChildStack.peek();

            if (child != null) {
                someoneAccept = child.fire(event);

                return someoneAccept;
            }

            ArrayList<EventListener<T>> listeners = eventsListeners.get(eventClass);

            if (listeners == null)
                return someoneAccept;

            for (EventListener<T> listener : new ArrayList<>(listeners)) {
                boolean status = listener.event(event);

                if (status)
                    someoneAccept = true;
            }

            return someoneAccept;
        }finally {
            postEventsListeners.get(eventClass).postEvent(event, someoneAccept);
        }
    }

    public void pushChild(EventHandler<T> handler) {
        this.handlerChildStack.push(handler);
    }

    public void popChild() {
        this.handlerChildStack.pop();
    }

    public <X extends T> int getEventSubscribers(Class<X> eventType) {
        return eventsListeners.get(eventType).size();
    }
}
