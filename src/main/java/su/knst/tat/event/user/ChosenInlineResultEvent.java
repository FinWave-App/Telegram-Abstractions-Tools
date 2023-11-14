package su.knst.tat.event.user;

import com.pengrad.telegrambot.model.ChosenInlineResult;
import su.knst.tat.event.Event;
import su.knst.tat.event.UserEvent;

public class ChosenInlineResultEvent extends UserEvent<ChosenInlineResult> {
    public ChosenInlineResultEvent(int id, long userId, ChosenInlineResult data) {
        super(id, userId, data);
    }
}
