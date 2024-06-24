package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.Message;
import app.finwave.tat.event.ChatEvent;

public class NewChannelPostEvent extends ChatEvent<Message> {

    public NewChannelPostEvent(int id, long userId, long chatId, Message data) {
        super(id, userId, chatId, data);
    }
}
