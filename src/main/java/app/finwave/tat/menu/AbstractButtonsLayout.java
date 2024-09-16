package app.finwave.tat.menu;

import app.finwave.tat.event.chat.CallbackQueryEvent;
import app.finwave.tat.event.handler.EventListener;
import app.finwave.tat.event.handler.HandlerRemover;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class AbstractButtonsLayout {
    protected MessageMenu<? extends AbstractButtonsLayout> menu;
    protected HashSet<HandlerRemover> removers = new HashSet<>();

    protected void init(MessageMenu<? extends AbstractButtonsLayout> menu) {
        this.menu = menu;
    }

    public HandlerRemover addButton(InlineKeyboardButton button, EventListener<CallbackQueryEvent> listener) {
        HandlerRemover remover = menu.registerButton(button, listener);
        removers.add(remover);

        return remover;
    }

    public void removeAll() {
        removers.forEach(HandlerRemover::remove);
    }

    public abstract InlineKeyboardMarkup build();
}
