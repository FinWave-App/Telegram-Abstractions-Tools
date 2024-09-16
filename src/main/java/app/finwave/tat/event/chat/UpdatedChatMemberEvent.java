package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.ChatMemberUpdated;
import app.finwave.tat.event.ChatEvent;
import com.pengrad.telegrambot.model.Update;

public class UpdatedChatMemberEvent extends ChatEvent<ChatMemberUpdated> {
    public UpdatedChatMemberEvent(int id, long userId, long chatId, ChatMemberUpdated chatMember) {
        super(id, userId, chatId, chatMember);
    }

    public static UpdatedChatMemberEvent fromUpdate(Update update) {
        ChatMemberUpdated chatMember = update.chatMember();

        if (chatMember == null)
            return null;

        long chatId = chatMember.chat().id();
        long userId = chatMember.from().id();

        return new UpdatedChatMemberEvent(update.updateId(), userId, chatId, chatMember);
    }
}
