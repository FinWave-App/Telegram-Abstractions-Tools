package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.Message;
import app.finwave.tat.event.ChatEvent;
import com.pengrad.telegrambot.model.Update;

public class NewMessageEvent extends ChatEvent<Message> {
    public NewMessageEvent(int id, long userId, long chatId, Message message) {
        super(id, userId, chatId, message);
    }

    public static NewMessageEvent fromUpdate(Update update) {
        Message message = update.message();

        if (message == null)
            return null;

        long chatId = message.chat().id();
        long userId = message.from().id();

        return new NewMessageEvent(update.updateId(), userId, chatId, message);
    }
}
