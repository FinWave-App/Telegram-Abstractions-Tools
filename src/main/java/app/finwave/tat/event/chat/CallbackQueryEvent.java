package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.CallbackQuery;
import app.finwave.tat.event.ChatEvent;

public class CallbackQueryEvent extends ChatEvent<CallbackQuery> {
    public CallbackQueryEvent(int id, long userId, long chatId, CallbackQuery data) {
        super(id, userId, chatId, data);
    }
}
