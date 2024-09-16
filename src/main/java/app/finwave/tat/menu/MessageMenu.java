package app.finwave.tat.menu;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.response.BaseResponse;
import app.finwave.tat.event.chat.CallbackQueryEvent;
import app.finwave.tat.event.handler.EventListener;
import app.finwave.tat.event.handler.HandlerRemover;
import app.finwave.tat.scene.BaseScene;
import app.finwave.tat.utils.CodeGenerator;
import app.finwave.tat.utils.ComposedMessage;
import app.finwave.tat.utils.MessageBuilder;
import app.finwave.tat.utils.Pair;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MessageMenu<X extends AbstractButtonsLayout> {
    protected BaseScene<?> scene;

    protected int sentMessage;
    protected ComposedMessage composedMessage;

    protected X layout;

    public MessageMenu(BaseScene<?> scene, X layout, int sentMessage) {
        this.scene = scene;
        this.sentMessage = sentMessage;
        this.layout = layout;

        layout.init(this);
    }

    public MessageMenu(BaseScene<?> scene, X layout, boolean useLastMessage) {
        this(scene, layout, useLastMessage ? scene.getChatHandler().getLastSentMessageId() : -1);
    }

    public MessageMenu(BaseScene<?> scene, X layout) {
        this(scene, layout, true);
    }

    protected HandlerRemover registerButton(InlineKeyboardButton button, EventListener<CallbackQueryEvent> listener) {
        HandlerRemover handlerRemover;

        if (listener != null) {
            String code = CodeGenerator.generateRandomCode(16);

            button.callbackData(code);

            handlerRemover = scene.getEventHandler().registerListener(CallbackQueryEvent.class, (e) -> {
                if (e.data.data().equals(code))
                    listener.event(e);
            });
        } else {
            handlerRemover = null;
        }

        return () -> {
            if (handlerRemover != null)
                handlerRemover.remove();
        };
    }

    public MessageMenu setMessage(ComposedMessage composedMessage) {
        this.composedMessage = composedMessage;

        return this;
    }

    public ComposedMessage getMessage() {
        return composedMessage;
    }

    public void setSentMessage(int sentMessage) {
        this.sentMessage = sentMessage;
    }

    protected CompletableFuture<BaseResponse> update() {
        if (sentMessage == -1 || composedMessage == null)
            return CompletableFuture.failedFuture(new IllegalStateException());

        if (composedMessage.keyboard() == null)
            composedMessage = MessageBuilder.create(composedMessage).setKeyboard(layout.build()).build();

        return scene.getChatHandler().editMessage(sentMessage, composedMessage);
    }

    public CompletableFuture<? extends BaseResponse> apply() {
        if (composedMessage == null)
            return CompletableFuture.failedFuture(new IllegalStateException());

        if (sentMessage != -1)
            return update();

        if (composedMessage.keyboard() == null)
            composedMessage = MessageBuilder.create(composedMessage).setKeyboard(layout.build()).build();

        return scene.getChatHandler().sendMessage(composedMessage).whenComplete((r, t) -> {
            if (t != null) {
                t.printStackTrace();
                return;
            }

            sentMessage = r.message().messageId();
        });
    }

    public CompletableFuture<BaseResponse> delete() {
        if (sentMessage == -1)
            return CompletableFuture.failedFuture(new IllegalStateException());

        return scene.getChatHandler().deleteMessage(sentMessage).whenComplete((r, t) -> {
            if (t == null)
                this.sentMessage = -1;
        });
    }
}
