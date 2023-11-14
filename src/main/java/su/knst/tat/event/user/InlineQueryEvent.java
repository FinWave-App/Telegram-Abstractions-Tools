package su.knst.tat.event.user;

import com.pengrad.telegrambot.model.InlineQuery;
import su.knst.tat.event.Event;
import su.knst.tat.event.UserEvent;

public class InlineQueryEvent extends UserEvent<InlineQuery> {
    public InlineQueryEvent(int id, long userId, InlineQuery data) {
        super(id, userId, data);
    }
}
