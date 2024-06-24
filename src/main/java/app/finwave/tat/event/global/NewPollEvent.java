package app.finwave.tat.event.global;

import app.finwave.tat.event.Event;
import com.pengrad.telegrambot.model.Poll;

public class NewPollEvent extends Event<Poll> {
    public NewPollEvent(int id, Poll data) {
        super(id, data);
    }
}
