package app.finwave.tat.menu;

import app.finwave.tat.event.chat.CallbackQueryEvent;
import app.finwave.tat.event.handler.EventListener;
import app.finwave.tat.event.handler.HandlerRemover;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;

public class ListButtonsLayout extends AbstractButtonsLayout {
    protected ArrayList<InlineKeyboardButton> buttons = new ArrayList<>();
    protected boolean horizontal;

    public ListButtonsLayout(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public ListButtonsLayout() {
        this(false);
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    @Override
    public HandlerRemover addButton(InlineKeyboardButton button, EventListener<CallbackQueryEvent> listener) {
        HandlerRemover remover = super.addButton(button, listener);

        buttons.add(button);

        return () -> {
            remover.remove();
            buttons.remove(button);
        };
    }

    @Override
    public void removeAll() {
        super.removeAll();

        buttons.clear();
    }

    @Override
    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup result = new InlineKeyboardMarkup();

        if (horizontal) {
            result.addRow(buttons.toArray(new InlineKeyboardButton[0]));

            return result;
        }

        for (InlineKeyboardButton button : buttons) {
            InlineKeyboardButton[] row = new InlineKeyboardButton[1];
            row[0] = button;

            result.addRow(row);
        }

        return result;
    }
}
