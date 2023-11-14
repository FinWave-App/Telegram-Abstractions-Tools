package su.knst.tat.event.chat;

import com.pengrad.telegrambot.model.CallbackQuery;
import su.knst.tat.event.ChatEvent;
import su.knst.tat.event.Event;

public class CallbackQueryEvent extends ChatEvent<CallbackQuery> {

    public CallbackQueryEvent(int id, long userId, long chatId, CallbackQuery data) {
        super(id, userId, chatId, data);
    }
}
