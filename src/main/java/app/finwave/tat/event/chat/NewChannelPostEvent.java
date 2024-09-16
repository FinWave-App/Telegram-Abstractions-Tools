package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.Message;
import app.finwave.tat.event.ChatEvent;
import com.pengrad.telegrambot.model.Update;

public class NewChannelPostEvent extends ChatEvent<Message> {
    public NewChannelPostEvent(int id, long userId, long chatId, Message channelPost) {
        super(id, userId, chatId, channelPost);
    }

    public static NewChannelPostEvent fromUpdate(Update update) {
        Message channelPost = update.channelPost();

        if (channelPost == null)
            return null;

        long chatId = channelPost.chat().id();
        long userId = channelPost.from().id();

        return new NewChannelPostEvent(update.updateId(), userId, chatId, channelPost);
    }
}
