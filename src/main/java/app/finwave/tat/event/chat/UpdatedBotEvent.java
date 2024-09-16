package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.ChatMemberUpdated;
import app.finwave.tat.event.ChatEvent;
import com.pengrad.telegrambot.model.PollAnswer;
import com.pengrad.telegrambot.model.Update;

public class UpdatedBotEvent extends ChatEvent<ChatMemberUpdated> {
    public UpdatedBotEvent(int id, long userId, long chatId, ChatMemberUpdated myChatMember) {
        super(id, userId, chatId, myChatMember);
    }

    public static UpdatedBotEvent fromUpdate(Update update) {
        ChatMemberUpdated myChatMember = update.myChatMember();

        if (myChatMember == null)
            return null;

        long chatId = myChatMember.chat().id();
        long userId = myChatMember.from().id();

        return new UpdatedBotEvent(update.updateId(), userId, chatId, myChatMember);
    }
}
