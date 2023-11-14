package su.knst.tat.handlers;

import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Poll;
import su.knst.tat.BotCore;
import su.knst.tat.event.Event;

public abstract class GlobalHandler extends AbstractContextHandler<Event<?>> {
    public GlobalHandler(BotCore core) {
        super(core);
    }

    public abstract void start();
}
