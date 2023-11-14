package su.knst.tat.event;

public class Event<T> {
    public final int id;
    public final T data;

    public Event(int id, T data) {
        this.id = id;
        this.data = data;
    }
}
