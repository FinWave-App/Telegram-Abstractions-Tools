package su.knst.tat.scene;

import su.knst.tat.event.ChatEvent;
import su.knst.tat.event.handler.EventHandler;
import su.knst.tat.handlers.AbstractChatHandler;

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
