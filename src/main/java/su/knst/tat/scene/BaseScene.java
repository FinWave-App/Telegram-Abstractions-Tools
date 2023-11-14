package su.knst.tat.scene;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import su.knst.tat.event.ChatEvent;
import su.knst.tat.event.handler.EventHandler;
import su.knst.tat.handlers.ChatHandler;
import su.knst.tat.utils.Message;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class BaseScene<T> {
    protected ChatHandler chatHandler;
    protected EventHandler<ChatEvent<?>> eventHandler;
    protected long chatId;

    public BaseScene(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
        this.eventHandler = new EventHandler<>();
        this.chatId = chatHandler.getChatId();
    }

    public void start() {
        chatHandler.getEventHandler().setChild(eventHandler);
    }

    public void start(T arg) {
        start();
    }

    public void stop() {
        chatHandler.getEventHandler().resetChild();
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public EventHandler<ChatEvent<?>> getEventHandler() {
        return eventHandler;
    }
}
