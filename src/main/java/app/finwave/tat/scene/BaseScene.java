package app.finwave.tat.scene;

import app.finwave.tat.handlers.AbstractChatHandler;
import app.finwave.tat.event.ChatEvent;
import app.finwave.tat.event.handler.EventHandler;

public class BaseScene<T> {
    protected AbstractChatHandler abstractChatHandler;
    protected EventHandler<ChatEvent<?>> eventHandler;
    protected long chatId;

    public BaseScene(AbstractChatHandler abstractChatHandler) {
        this.abstractChatHandler = abstractChatHandler;
        this.eventHandler = new EventHandler<>();
        this.chatId = abstractChatHandler.getChatId();
    }

    public void start() {
        abstractChatHandler.getEventHandler().pushChild(eventHandler);
    }

    public void start(T arg) {
        start();
    }

    public void stop() {
        abstractChatHandler.getEventHandler().popChild();
    }

    public AbstractChatHandler getChatHandler() {
        return abstractChatHandler;
    }

    public EventHandler<ChatEvent<?>> getEventHandler() {
        return eventHandler;
    }
}
