package app.finwave.tat.event.user;

import app.finwave.tat.event.chat.ChatJoinRequestEvent;
import com.pengrad.telegrambot.model.ChatJoinRequest;
import com.pengrad.telegrambot.model.InlineQuery;
import app.finwave.tat.event.UserEvent;
import com.pengrad.telegrambot.model.Update;

public class InlineQueryEvent extends UserEvent<InlineQuery> {
    public InlineQueryEvent(int id, long userId, InlineQuery inlineQuery) {
        super(id, userId, inlineQuery);
    }

    public static InlineQueryEvent fromUpdate(Update update) {
        InlineQuery inlineQuery = update.inlineQuery();

        if (inlineQuery == null)
            return null;

        long userId = inlineQuery.from().id();

        return new InlineQueryEvent(update.updateId(), userId, inlineQuery);
    }
}
