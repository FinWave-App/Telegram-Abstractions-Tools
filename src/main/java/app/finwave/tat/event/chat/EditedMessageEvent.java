package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.Message;
import app.finwave.tat.event.ChatEvent;
import com.pengrad.telegrambot.model.Update;

public class EditedMessageEvent extends ChatEvent<Message> {
    public EditedMessageEvent(int id, long userId, long chatId, Message editedMessage) {
        super(id, userId, chatId, editedMessage);
    }

    public static EditedMessageEvent fromUpdate(Update update) {
        Message editedMessage = update.editedMessage();

        if (editedMessage == null)
            return null;

        long chatId = editedMessage.chat().id();
        long userId = editedMessage.from().id();

        return new EditedMessageEvent(update.updateId(), userId, chatId, editedMessage);
    }
}
