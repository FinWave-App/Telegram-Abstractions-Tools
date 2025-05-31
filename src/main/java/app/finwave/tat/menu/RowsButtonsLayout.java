package app.finwave.tat.menu;

import app.finwave.tat.event.chat.CallbackQueryEvent;
import app.finwave.tat.event.handler.EventListener;
import app.finwave.tat.event.handler.HandlerRemover;
import app.finwave.tat.utils.Pair;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RowsButtonsLayout extends AbstractButtonsLayout {
    protected ArrayList<ArrayList<InlineKeyboardButton>> grid = new ArrayList<>();

    public RowsButtonsLayout() {
        insertRow();
    }

    public HandlerRemover appendRow(InlineKeyboardButton button, Consumer<CallbackQueryEvent> listener) {
        HandlerRemover remover = super.addButton(button, listener);

        ArrayList<InlineKeyboardButton> row = grid.get(grid.size() - 1);
        row.add(button);

        return () -> {
            remover.remove();
            row.remove(button);
        };
    }

    public HandlerRemover insertRow(List<Pair<InlineKeyboardButton, Consumer<CallbackQueryEvent>>> buttons) {
        insertRow();

        List<HandlerRemover> removers = buttons.stream()
                .map((p) -> appendRow(p.first(), p.second()))
                .toList();

        return () -> removers.forEach(HandlerRemover::remove);
    }

    public void insertRow() {
        grid.add(new ArrayList<>());
    }

    @Override
    public HandlerRemover addButton(InlineKeyboardButton button, Consumer<CallbackQueryEvent> listener) {
        return appendRow(button, listener);
    }

    @Override
    public void removeAll() {
        super.removeAll();

        grid.clear();
        insertRow();
    }

    @Override
    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup result = new InlineKeyboardMarkup();

        for (var row : grid) {
            if (row.isEmpty())
                continue;

            result.addRow(row.toArray(new InlineKeyboardButton[0]));
        }

        return result;
    }
}
