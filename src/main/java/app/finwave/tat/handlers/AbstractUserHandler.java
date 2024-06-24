package app.finwave.tat.handlers;

import app.finwave.tat.BotCore;
import app.finwave.tat.event.UserEvent;

public abstract class AbstractUserHandler extends AbstractContextHandler<UserEvent<?>> {
    protected final long userId;

    public AbstractUserHandler(BotCore core, long userId) {
        super(core);
        this.userId = userId;
    }

    public abstract void start();
}
