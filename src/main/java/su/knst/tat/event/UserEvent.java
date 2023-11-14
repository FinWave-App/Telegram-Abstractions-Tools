package su.knst.tat.event;

import su.knst.tat.event.Event;

public class UserEvent<T> extends Event<T> {
    public final long userId;
    public UserEvent(int id, long userId, T data) {
        super(id, data);

        this.userId = userId;
    }
}
