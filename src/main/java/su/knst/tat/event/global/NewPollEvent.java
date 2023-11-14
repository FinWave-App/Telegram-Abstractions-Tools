package su.knst.tat.event.global;

import com.pengrad.telegrambot.model.Poll;
import su.knst.tat.event.Event;

public class NewPollEvent extends Event<Poll> {
    public NewPollEvent(int id, Poll data) {
        super(id, data);
    }
}
