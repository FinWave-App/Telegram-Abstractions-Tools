package su.knst.tat.handlers;

import su.knst.tat.BotCore;
import su.knst.tat.event.Event;

public abstract class AbstractGlobalHandler extends AbstractContextHandler<Event<?>> {
    public AbstractGlobalHandler(BotCore core) {
        super(core);
    }

    public abstract void start();
}
