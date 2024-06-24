package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.Message;
import app.finwave.tat.event.ChatEvent;

public class EditedChannelPostEvent extends ChatEvent<Message> {

    public EditedChannelPostEvent(int id, long userId, long chatId, Message data) {
        super(id, userId, chatId, data);
    }
}
