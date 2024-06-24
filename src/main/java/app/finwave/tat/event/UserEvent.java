package app.finwave.tat.event;

public class UserEvent<T> extends Event<T> {
    public final long userId;
    public UserEvent(int id, long userId, T data) {
        super(id, data);

        this.userId = userId;
    }
}
