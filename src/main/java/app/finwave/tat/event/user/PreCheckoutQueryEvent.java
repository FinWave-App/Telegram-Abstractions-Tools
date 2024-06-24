package app.finwave.tat.event.user;

import com.pengrad.telegrambot.model.PreCheckoutQuery;
import app.finwave.tat.event.UserEvent;

public class PreCheckoutQueryEvent extends UserEvent<PreCheckoutQuery> {
    public PreCheckoutQueryEvent(int id, long userId, PreCheckoutQuery data) {
        super(id, userId, data);
    }
}
