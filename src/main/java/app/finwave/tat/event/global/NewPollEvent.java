package app.finwave.tat.event.global;

import app.finwave.tat.event.Event;
import app.finwave.tat.event.user.PreCheckoutQueryEvent;
import com.pengrad.telegrambot.model.Poll;
import com.pengrad.telegrambot.model.PreCheckoutQuery;
import com.pengrad.telegrambot.model.Update;

public class NewPollEvent extends Event<Poll> {
    public NewPollEvent(int id, Poll poll) {
        super(id, poll);
    }

    public static NewPollEvent fromUpdate(Update update) {
        Poll poll = update.poll();

        if (poll == null)
            return null;

        return new NewPollEvent(update.updateId(), poll);
    }
}
