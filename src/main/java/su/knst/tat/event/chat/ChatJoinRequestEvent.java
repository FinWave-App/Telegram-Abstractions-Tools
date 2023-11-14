package su.knst.tat.event.chat;

import com.pengrad.telegrambot.model.ChatJoinRequest;
import su.knst.tat.event.ChatEvent;
import su.knst.tat.event.Event;

public class ChatJoinRequestEvent extends ChatEvent<ChatJoinRequest> {

    public ChatJoinRequestEvent(int id, long userId, long chatId, ChatJoinRequest data) {
        super(id, userId, chatId, data);
    }
}
