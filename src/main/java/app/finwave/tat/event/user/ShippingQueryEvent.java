package app.finwave.tat.event.user;

import com.pengrad.telegrambot.model.ShippingQuery;
import app.finwave.tat.event.UserEvent;

public class ShippingQueryEvent extends UserEvent<ShippingQuery> {
    public ShippingQueryEvent(int id, long userId, ShippingQuery data) {
        super(id, userId, data);
    }
}
