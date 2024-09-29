package app.finwave.tat.event.chat;

import app.finwave.tat.event.ChatEvent;
import com.pengrad.telegrambot.model.MessageReactionUpdated;
import com.pengrad.telegrambot.model.Update;

public class MessageReactionEvent extends ChatEvent<MessageReactionUpdated> {
    public MessageReactionEvent(int id, long userId, long chatId, MessageReactionUpdated messageReactionUpdated) {
        super(id, userId, chatId, messageReactionUpdated);
    }

    public static MessageReactionEvent fromUpdate(Update update) {
        MessageReactionUpdated reactionUpdated = update.messageReaction();

        if (reactionUpdated == null)
            return null;

        long chatId = reactionUpdated.chat().id();
        long userId = reactionUpdated.user().id();

        return new MessageReactionEvent(update.updateId(), userId, chatId, reactionUpdated);
    }
}
