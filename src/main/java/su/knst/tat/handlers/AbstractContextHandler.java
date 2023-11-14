package su.knst.tat.handlers;

import su.knst.tat.BotCore;
import su.knst.tat.event.Event;
import su.knst.tat.event.handler.EventHandler;

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
