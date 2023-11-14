package su.knst.tat.event.user;

import com.pengrad.telegrambot.model.PreCheckoutQuery;
import su.knst.tat.event.Event;
import su.knst.tat.event.UserEvent;

public class PreCheckoutQueryEvent extends UserEvent<PreCheckoutQuery> {
    public PreCheckoutQueryEvent(int id, long userId, PreCheckoutQuery data) {
        super(id, userId, data);
    }
}
