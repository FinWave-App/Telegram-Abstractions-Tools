package app.finwave.tat.event;

public class ChatEvent<T> extends UserEvent<T>{
    public final long chatId;

    public ChatEvent(int id, long userId, long chatId, T data) {
        super(id, userId, data);
        this.chatId = chatId;
    }
}
