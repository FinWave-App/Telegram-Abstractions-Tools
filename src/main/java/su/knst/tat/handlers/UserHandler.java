package su.knst.tat.handlers;

import com.pengrad.telegrambot.model.*;
import su.knst.tat.BotCore;
import su.knst.tat.event.UserEvent;

public abstract class UserHandler extends AbstractContextHandler<UserEvent<?>> {
    protected final long userId;

    public UserHandler(BotCore core, long userId) {
        super(core);
        this.userId = userId;
    }

    public abstract void start();
}
