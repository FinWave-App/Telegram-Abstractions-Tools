package app.finwave.tat.menu;

import app.finwave.tat.event.chat.CallbackQueryEvent;
import app.finwave.tat.event.handler.EventListener;
import app.finwave.tat.event.handler.HandlerRemover;
import app.finwave.tat.utils.Pair;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;

public class FlexListButtonsLayout extends AbstractButtonsLayout {
    protected ArrayList<Pair<InlineKeyboardButton, Integer>> buttons = new ArrayList<>();
    protected int maxLineSize;

    public FlexListButtonsLayout(int maxLineSize) {
        this.maxLineSize = maxLineSize;
    }

    public void setMaxLineSize(int maxLineSize) {
        this.maxLineSize = maxLineSize;
    }

    public HandlerRemover addButton(InlineKeyboardButton button, int size, EventListener<CallbackQueryEvent> listener) {
        HandlerRemover remover = super.addButton(button, listener);
        var pair = Pair.of(button, size);

        buttons.add(pair);

        return () -> {
            remover.remove();
            buttons.remove(pair);
        };
    }

    @Override
    public HandlerRemover addButton(InlineKeyboardButton button, EventListener<CallbackQueryEvent> listener) {
        return this.addButton(button, 1, listener);
    }

    @Override
    public void removeAll() {
        super.removeAll();

        buttons.clear();
    }

    @Override
    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup result = new InlineKeyboardMarkup();

        ArrayList<InlineKeyboardButton> currentRow = new ArrayList<>();
        int currentRowSize = 0;

        for (var pair : buttons) {
            if (currentRowSize >= maxLineSize || !currentRow.isEmpty() && currentRowSize + pair.second() >= maxLineSize) {
                result.addRow(currentRow.toArray(new InlineKeyboardButton[0]));
                currentRow.clear();
                currentRowSize = 0;
            }

            currentRowSize += pair.second();
            currentRow.add(pair.first());
        }

        if (!currentRow.isEmpty())
            result.addRow(currentRow.toArray(new InlineKeyboardButton[0]));

        return result;
    }
}
