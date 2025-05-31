package app.finwave.tat.menu;

import app.finwave.tat.event.chat.CallbackQueryEvent;
import app.finwave.tat.event.handler.EventListener;
import app.finwave.tat.event.handler.HandlerRemover;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.HashSet;
import java.util.function.Consumer;

public abstract class AbstractButtonsLayout {
    protected AbstractMessageMenu<? extends AbstractButtonsLayout> menu;
    protected HashSet<HandlerRemover> removers = new HashSet<>();

    protected void init(AbstractMessageMenu<? extends AbstractButtonsLayout> menu) {
        this.menu = menu;
    }

    public HandlerRemover addButton(InlineKeyboardButton button, Consumer<CallbackQueryEvent> listener) {
        HandlerRemover remover = menu.registerButton(button, listener);
        removers.add(remover);

        return remover;
    }

    public void removeAll() {
        removers.forEach(HandlerRemover::remove);
    }

    public abstract InlineKeyboardMarkup build();
}
