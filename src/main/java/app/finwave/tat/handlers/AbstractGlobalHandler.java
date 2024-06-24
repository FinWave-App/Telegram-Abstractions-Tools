package app.finwave.tat.handlers;

import app.finwave.tat.BotCore;
import app.finwave.tat.event.Event;

public abstract class AbstractGlobalHandler extends AbstractContextHandler<Event<?>> {
    public AbstractGlobalHandler(BotCore core) {
        super(core);
    }

    public abstract void start();
}
