package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.ChatJoinRequest;
import app.finwave.tat.event.ChatEvent;
import com.pengrad.telegrambot.model.ChatMemberUpdated;
import com.pengrad.telegrambot.model.Update;

public class ChatJoinRequestEvent extends ChatEvent<ChatJoinRequest> {
    public ChatJoinRequestEvent(int id, long userId, long chatId, ChatJoinRequest chatJoinRequest) {
        super(id, userId, chatId, chatJoinRequest);
    }

    public static ChatJoinRequestEvent fromUpdate(Update update) {
        ChatJoinRequest chatJoinRequest = update.chatJoinRequest();

        if (chatJoinRequest == null)
            return null;

        long chatId = chatJoinRequest.chat().id();
        long userId = chatJoinRequest.from().id();

        return new ChatJoinRequestEvent(update.updateId(), userId, chatId, chatJoinRequest);
    }
}
