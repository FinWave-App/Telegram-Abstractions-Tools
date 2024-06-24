package app.finwave.tat.handlers;

import app.finwave.tat.BotCore;
import app.finwave.tat.event.Event;
import app.finwave.tat.event.handler.EventHandler;

public abstract class AbstractContextHandler<T extends Event<?>> {
    protected final BotCore core;
    protected EventHandler<T> eventHandler;

    public AbstractContextHandler(BotCore core) {
        this.core = core;
        this.eventHandler = new EventHandler<>();
    }

    public BotCore getCore() {
        return core;
    }

    public EventHandler<T> getEventHandler() {
        return eventHandler;
    }
}
