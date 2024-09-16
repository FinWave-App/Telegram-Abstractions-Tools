package app.finwave.tat.event.chat;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.PollAnswer;
import app.finwave.tat.event.ChatEvent;
import com.pengrad.telegrambot.model.Update;

public class PollAnswerEvent extends ChatEvent<PollAnswer> {
    public PollAnswerEvent(int id, long userId, long chatId, PollAnswer pollAnswer) {
        super(id, userId, chatId, pollAnswer);
    }

    public static PollAnswerEvent fromUpdate(Update update) {
        PollAnswer pollAnswer = update.pollAnswer();

        if (pollAnswer == null)
            return null;

        long chatId = pollAnswer.voterChat().id();
        long userId = pollAnswer.user().id();

        return new PollAnswerEvent(update.updateId(), userId, chatId, pollAnswer);
    }
}
