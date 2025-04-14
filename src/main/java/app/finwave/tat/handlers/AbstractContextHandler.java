package app.finwave.tat.handlers;

import app.finwave.tat.BotCore;
import app.finwave.tat.event.Event;
import app.finwave.tat.event.handler.EventHandler;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.AnswerPreCheckoutQuery;
import com.pengrad.telegrambot.response.BaseResponse;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractContextHandler<T extends Event<?>> {
    protected final BotCore core;
    protected EventHandler<T> eventHandler;

    public AbstractContextHandler(BotCore core) {
        this.core = core;
        this.eventHandler = new EventHandler<>();
    }

    public BotCore getCore() {
        return core;
    }

    public EventHandler<T> getEventHandler() {
        return eventHandler;
    }

    public CompletableFuture<BaseResponse> answerPreCheckoutQuery(String preCheckoutQueryId, String error) {
        AnswerPreCheckoutQuery request;

        if (error == null || error.isBlank()) {
            request = new AnswerPreCheckoutQuery(preCheckoutQueryId);
        }else {
            request = new AnswerPreCheckoutQuery(preCheckoutQueryId, error);
        }

        return core.execute(request, true);
    }

    public CompletableFuture<BaseResponse> answerPreCheckoutQuery(String id) {
        return answerPreCheckoutQuery(id, null);
    }
}
