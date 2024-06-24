package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.ChatJoinRequest;
import app.finwave.tat.event.ChatEvent;

public class ChatJoinRequestEvent extends ChatEvent<ChatJoinRequest> {

    public ChatJoinRequestEvent(int id, long userId, long chatId, ChatJoinRequest data) {
        super(id, userId, chatId, data);
    }
}
