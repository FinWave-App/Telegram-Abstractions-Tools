package app.finwave.tat.event.user;

import com.pengrad.telegrambot.model.InlineQuery;
import app.finwave.tat.event.UserEvent;

public class InlineQueryEvent extends UserEvent<InlineQuery> {
    public InlineQueryEvent(int id, long userId, InlineQuery data) {
        super(id, userId, data);
    }
}
