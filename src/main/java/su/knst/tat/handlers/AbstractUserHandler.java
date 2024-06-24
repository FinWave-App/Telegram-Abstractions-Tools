package su.knst.tat.handlers;

import su.knst.tat.BotCore;
import su.knst.tat.event.UserEvent;

public abstract class AbstractUserHandler extends AbstractContextHandler<UserEvent<?>> {
    protected final long userId;

    public AbstractUserHandler(BotCore core, long userId) {
        super(core);
        this.userId = userId;
    }

    public abstract void start();
}
