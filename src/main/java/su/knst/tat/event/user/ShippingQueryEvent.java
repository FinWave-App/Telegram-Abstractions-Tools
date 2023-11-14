package su.knst.tat.event.user;

import com.pengrad.telegrambot.model.ShippingQuery;
import su.knst.tat.event.Event;
import su.knst.tat.event.UserEvent;

public class ShippingQueryEvent extends UserEvent<ShippingQuery> {
    public ShippingQueryEvent(int id, long userId, ShippingQuery data) {
        super(id, userId, data);
    }
}
