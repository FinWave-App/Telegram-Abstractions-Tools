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

    protected int useSentMessage;
    protected boolean useLastMessage;

    protected ComposedMessage composedMessage;

    protected X layout;

    public MessageMenu(BaseScene<?> scene, X layout, int useSentMessage) {
        this.scene = scene;
        this.useSentMessage = useSentMessage;
        this.useLastMessage = false;
        this.layout = layout;

        layout.init(this);
    }

    public MessageMenu(BaseScene<?> scene, X layout, boolean useLastMessage) {
        this(scene, layout, -1);

        this.useLastMessage = useLastMessage;
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
                if (!e.data.data().equals(code))
                    return false;

                return listener.event(e);
            });
        } else {
            handlerRemover = null;
        }

        return () -> {
            if (handlerRemover != null)
                handlerRemover.remove();
        };
    }

    public MessageMenu<X> setMessage(ComposedMessage composedMessage) {
        this.composedMessage = composedMessage;

        return this;
    }

    public X getLayout() {
        return layout;
    }

    public ComposedMessage getMessage() {
        return composedMessage;
    }

    protected int getUseMessage() {
        if (useLastMessage)
            return scene.getChatHandler().getLastSentMessageId();

        return useSentMessage;
    }

    public void setUseLastMessage(boolean useLastMessage) {
        this.useSentMessage = -1;
        this.useLastMessage = useLastMessage;
    }

    public void setUseSentMessage(int sentMessage) {
        this.useSentMessage = sentMessage;
        this.useLastMessage = false;
    }

    protected CompletableFuture<BaseResponse> update(int useMessage) {
        if (composedMessage == null)
            return CompletableFuture.failedFuture(new IllegalStateException());

        if (composedMessage.keyboard() == null)
            composedMessage = MessageBuilder.create(composedMessage).setKeyboard(layout.build()).build();

        return scene.getChatHandler().editMessage(useMessage, composedMessage);
    }

    public CompletableFuture<? extends BaseResponse> apply() {
        if (composedMessage == null)
            return CompletableFuture.failedFuture(new IllegalStateException());

        int useMessage = getUseMessage();

        if (useMessage != -1)
            return update(useMessage);

        if (composedMessage.keyboard() == null)
            composedMessage = MessageBuilder.create(composedMessage).setKeyboard(layout.build()).build();

        return scene.getChatHandler().sendMessage(composedMessage);
    }

    public CompletableFuture<BaseResponse> delete() {
        int useMessage = getUseMessage();

        if (useMessage == -1)
            return CompletableFuture.failedFuture(new IllegalStateException());

        return scene.getChatHandler().deleteMessage(useMessage);
    }
}
