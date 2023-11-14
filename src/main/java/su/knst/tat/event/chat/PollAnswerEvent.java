package su.knst.tat.event.chat;

import com.pengrad.telegrambot.model.PollAnswer;
import su.knst.tat.event.ChatEvent;
import su.knst.tat.event.Event;

public class PollAnswerEvent extends ChatEvent<PollAnswer> {

    public PollAnswerEvent(int id, long userId, long chatId, PollAnswer data) {
        super(id, userId, chatId, data);
    }
}
