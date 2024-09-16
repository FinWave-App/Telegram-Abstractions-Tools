package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.Message;
import app.finwave.tat.event.ChatEvent;
import com.pengrad.telegrambot.model.Update;

public class EditedChannelPostEvent extends ChatEvent<Message> {
    public EditedChannelPostEvent(int id, long userId, long chatId, Message editedChannelPost) {
        super(id, userId, chatId, editedChannelPost);
    }

    public static EditedChannelPostEvent fromUpdate(Update update) {
        Message editedChannelPost = update.editedChannelPost();

        if (editedChannelPost == null)
            return null;

        long chatId = editedChannelPost.chat().id();
        long userId = editedChannelPost.from().id();

        return new EditedChannelPostEvent(update.updateId(), userId, chatId, editedChannelPost);
    }
}
