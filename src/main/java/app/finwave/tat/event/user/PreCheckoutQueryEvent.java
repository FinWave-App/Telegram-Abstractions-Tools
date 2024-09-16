package app.finwave.tat.event.user;

import com.pengrad.telegrambot.model.PreCheckoutQuery;
import app.finwave.tat.event.UserEvent;
import com.pengrad.telegrambot.model.ShippingQuery;
import com.pengrad.telegrambot.model.Update;

public class PreCheckoutQueryEvent extends UserEvent<PreCheckoutQuery> {
    public PreCheckoutQueryEvent(int id, long userId, PreCheckoutQuery preCheckoutQuery) {
        super(id, userId, preCheckoutQuery);
    }

    public static PreCheckoutQueryEvent fromUpdate(Update update) {
        PreCheckoutQuery preCheckoutQuery = update.preCheckoutQuery();

        if (preCheckoutQuery == null)
            return null;

        long userId = preCheckoutQuery.from().id();

        return new PreCheckoutQueryEvent(update.updateId(), userId, preCheckoutQuery);
    }
}
