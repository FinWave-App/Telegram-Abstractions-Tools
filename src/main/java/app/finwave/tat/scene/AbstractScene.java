package app.finwave.tat.scene;

import app.finwave.tat.event.chat.CallbackQueryEvent;
import app.finwave.tat.handlers.AbstractChatHandler;
import app.finwave.tat.event.ChatEvent;
import app.finwave.tat.event.handler.EventHandler;
import app.finwave.tat.handlers.scened.ScenedAbstractChatHandler;

public abstract class AbstractScene<T> {
    protected ScenedAbstractChatHandler chatHandler;
    protected EventHandler<ChatEvent<?>> eventHandler;
    protected long chatId;

    public AbstractScene(ScenedAbstractChatHandler chatHandler) {
        this.chatHandler = chatHandler;
        this.eventHandler = new EventHandler<>();
        this.chatId = chatHandler.getChatId();

        eventHandler.setPostEventsListener(CallbackQueryEvent.class, (event, someoneHandled) -> {
            if (!someoneHandled) {
                chatHandler.deleteMessage(event.data.maybeInaccessibleMessage().messageId());
                return;
            }

            chatHandler.answerCallbackQuery(event.data.id());
        });
    }

    public abstract void start(T arg);

    public abstract void stop();

    public abstract String name();

    public AbstractChatHandler getChatHandler() {
        return chatHandler;
    }

    public EventHandler<ChatEvent<?>> getEventHandler() {
        return eventHandler;
    }
}
