package app.finwave.tat.event.user;

import com.pengrad.telegrambot.model.ChosenInlineResult;
import app.finwave.tat.event.UserEvent;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Update;

public class ChosenInlineResultEvent extends UserEvent<ChosenInlineResult> {
    public ChosenInlineResultEvent(int id, long userId, ChosenInlineResult chosenInlineResult) {
        super(id, userId, chosenInlineResult);
    }

    public static ChosenInlineResultEvent fromUpdate(Update update) {
        ChosenInlineResult chosenInlineResult = update.chosenInlineResult();

        if (chosenInlineResult == null)
            return null;

        long userId = chosenInlineResult.from().id();

        return new ChosenInlineResultEvent(update.updateId(), userId, chosenInlineResult);
    }
}
