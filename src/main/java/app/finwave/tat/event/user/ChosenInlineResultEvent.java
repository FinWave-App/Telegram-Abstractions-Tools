package app.finwave.tat.event.user;

import com.pengrad.telegrambot.model.ChosenInlineResult;
import app.finwave.tat.event.UserEvent;

public class ChosenInlineResultEvent extends UserEvent<ChosenInlineResult> {
    public ChosenInlineResultEvent(int id, long userId, ChosenInlineResult data) {
        super(id, userId, data);
    }
}
