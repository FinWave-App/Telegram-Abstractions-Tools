package app.finwave.tat.scene;

import app.finwave.tat.event.chat.CallbackQueryEvent;
import app.finwave.tat.handlers.AbstractChatHandler;
import app.finwave.tat.event.ChatEvent;
import app.finwave.tat.event.handler.EventHandler;
import app.finwave.tat.handlers.scened.ScenedAbstractChatHandler;

public class BaseScene<T> {
    protected ScenedAbstractChatHandler chatHandler;
    protected EventHandler<ChatEvent<?>> eventHandler;
    protected long chatId;

    public BaseScene(ScenedAbstractChatHandler chatHandler) {
        this.chatHandler = chatHandler;
        this.eventHandler = new EventHandler<>();
        this.chatId = chatHandler.getChatId();

        eventHandler.setValidator(CallbackQueryEvent.class, event -> {
            chatHandler.answerCallbackQuery(event.data.id());

            return true;
        });
    }

    public void start() {
        chatHandler.getEventHandler().pushChild(eventHandler);
    }

    public void start(T arg) {
        start();
    }

    public void stop() {
        chatHandler.getEventHandler().popChild();
    }

    public AbstractChatHandler getChatHandler() {
        return chatHandler;
    }

    public EventHandler<ChatEvent<?>> getEventHandler() {
        return eventHandler;
    }
}
