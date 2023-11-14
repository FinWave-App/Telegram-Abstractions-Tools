package su.knst.tat.event.chat;

import com.pengrad.telegrambot.model.ChatMemberUpdated;
import su.knst.tat.event.ChatEvent;
import su.knst.tat.event.Event;

public class UpdatedBotEvent extends ChatEvent<ChatMemberUpdated> {

    public UpdatedBotEvent(int id, long userId, long chatId, ChatMemberUpdated data) {
        super(id, userId, chatId, data);
    }
}
