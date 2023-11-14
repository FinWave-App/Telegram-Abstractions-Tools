package su.knst.tat.event.chat;

import com.pengrad.telegrambot.model.Message;
import su.knst.tat.event.ChatEvent;
import su.knst.tat.event.Event;

public class EditedChannelPostEvent extends ChatEvent<Message> {

    public EditedChannelPostEvent(int id, long userId, long chatId, Message data) {
        super(id, userId, chatId, data);
    }
}
