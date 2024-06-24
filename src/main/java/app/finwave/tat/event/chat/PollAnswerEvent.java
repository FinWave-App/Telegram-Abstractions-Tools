package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.PollAnswer;
import app.finwave.tat.event.ChatEvent;

public class PollAnswerEvent extends ChatEvent<PollAnswer> {

    public PollAnswerEvent(int id, long userId, long chatId, PollAnswer data) {
        super(id, userId, chatId, data);
    }
}
