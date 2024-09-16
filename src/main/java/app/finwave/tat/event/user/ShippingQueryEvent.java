package app.finwave.tat.event.user;

import com.pengrad.telegrambot.model.ChosenInlineResult;
import com.pengrad.telegrambot.model.ShippingQuery;
import app.finwave.tat.event.UserEvent;
import com.pengrad.telegrambot.model.Update;

public class ShippingQueryEvent extends UserEvent<ShippingQuery> {
    public ShippingQueryEvent(int id, long userId, ShippingQuery shippingQuery) {
        super(id, userId, shippingQuery);
    }

    public static ShippingQueryEvent fromUpdate(Update update) {
        ShippingQuery shippingQuery = update.shippingQuery();

        if (shippingQuery == null)
            return null;

        long userId = update.shippingQuery().from().id();

        return new ShippingQueryEvent(update.updateId(), userId, shippingQuery);
    }
}
