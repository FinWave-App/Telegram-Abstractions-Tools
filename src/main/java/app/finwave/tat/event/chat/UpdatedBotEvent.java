package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.ChatMemberUpdated;
import app.finwave.tat.event.ChatEvent;

public class UpdatedBotEvent extends ChatEvent<ChatMemberUpdated> {

    public UpdatedBotEvent(int id, long userId, long chatId, ChatMemberUpdated data) {
        super(id, userId, chatId, data);
    }
}
