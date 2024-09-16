package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.CallbackQuery;
import app.finwave.tat.event.ChatEvent;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

public class CallbackQueryEvent extends ChatEvent<CallbackQuery> {
    public CallbackQueryEvent(int id, long userId, long chatId, CallbackQuery callbackQuery) {
        super(id, userId, chatId, callbackQuery);
    }

    public static CallbackQueryEvent fromUpdate(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();

        if (callbackQuery == null)
            return null;

        long chatId = callbackQuery.maybeInaccessibleMessage().chat().id();
        long userId = callbackQuery.from().id();

        return new CallbackQueryEvent(update.updateId(), userId, chatId, callbackQuery);
    }
}
