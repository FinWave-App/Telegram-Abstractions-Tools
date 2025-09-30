package app.finwave.tat.menu;

import app.finwave.tat.event.handler.HandlerRemover;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.response.BaseResponse;
import app.finwave.tat.event.chat.CallbackQueryEvent;
import app.finwave.tat.scene.AbstractScene;
import app.finwave.tat.utils.CodeGenerator;
import app.finwave.tat.utils.ComposedMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AbstractMessageMenu<X extends AbstractButtonsLayout> {
    protected AbstractScene<?> scene;
    protected X layout;
    protected int appliedMessageId = -1;

    protected AbstractMessageMenu(AbstractScene<?> scene, X layout) {
        this.scene = scene;
        this.layout = layout;

        layout.init(this);
    }

    protected HandlerRemover registerButton(InlineKeyboardButton button, Consumer<CallbackQueryEvent> listener) {
        String code = CodeGenerator.generateRandomCode(16);

        button.callbackData(code);

        return scene.getEventHandler().registerListener(CallbackQueryEvent.class, (e) -> {
            if (!e.data.data().equals(code))
                return false;

            listener.accept(e);
            return true;
        });
    }

    protected abstract ComposedMessage buildMessage();

    protected ComposedMessage buildMessageWithKeyboard() {
        ComposedMessage message = buildMessage();

        if (message.keyboard() != null)
            return message;

        return message.toBuilder().keyboard(layout.build()).build();
    }

    protected CompletableFuture<BaseResponse> update() {
        if (appliedMessageId == -1)
            return CompletableFuture.failedFuture(new IllegalStateException("No sent message found"));

        return apply(appliedMessageId);
    }

    public CompletableFuture<BaseResponse> apply(int messageIdToUpdate) {
        return scene.getChatHandler().editMessage(messageIdToUpdate, buildMessageWithKeyboard()).whenComplete((r, t) -> {
            if (t != null || r == null || !r.isOk())
                return;

            appliedMessageId = messageIdToUpdate;
        });
    }

    public CompletableFuture<SendResponse> apply() {
        return scene.getChatHandler().sendMessage(buildMessageWithKeyboard()).whenComplete((r, t) -> {
            if (t != null || r == null || !r.isOk())
                return;

            appliedMessageId = r.message().messageId();
        });
    }
}
