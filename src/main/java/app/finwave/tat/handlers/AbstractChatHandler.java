package app.finwave.tat.handlers;

import app.finwave.tat.BotCore;
import app.finwave.tat.utils.Pair;
import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.model.request.ReplyParameters;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import app.finwave.tat.event.ChatEvent;
import app.finwave.tat.utils.ComposedMessage;
import app.finwave.tat.utils.Stack;

import java.io.File;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

public abstract class AbstractChatHandler extends AbstractContextHandler<ChatEvent<?>> {
    protected final long chatId;
    protected ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);

    public AbstractChatHandler(BotCore core, long chatId) {
        super(core);

        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    public abstract void start();

    public CompletableFuture<SendResponse> sendInvoice(String title, String description, String payload, String currency, List<LabeledPrice> prices) {
        SendInvoice request = new SendInvoice(chatId, title, description, payload, currency, prices);

        return core.execute(request);
    }

    public CompletableFuture<SendResponse> sendFile(String url, boolean asPhoto) {
        if (asPhoto)
            return core.execute(new SendPhoto(chatId, url));

        return core.execute(new SendDocument(chatId, url));
    }

    public CompletableFuture<SendResponse> sendFile(File file, boolean asPhoto) {
        if (asPhoto)
            return core.execute(new SendPhoto(chatId, file));

        return core.execute(new SendDocument(chatId, file));
    }

    public CompletableFuture<SendResponse> sendMessage(ComposedMessage composedMessage) {
        SendMessage request = new SendMessage(chatId, composedMessage.text())
                .parseMode(composedMessage.parseMode())
                .protectContent(composedMessage.protectContent())
                .disableNotification(!composedMessage.notification());

        if (composedMessage.keyboard() != null)
            request = request.replyMarkup(composedMessage.keyboard());

        if (composedMessage.replyParameters() != null)
            request = request.replyParameters(composedMessage.replyParameters());

        if (composedMessage.linkPreviewOptions() != null)
            request = request.linkPreviewOptions(composedMessage.linkPreviewOptions());

        return core.execute(request);
    }

    public CompletableFuture<BaseResponse> editMessage(int id, ComposedMessage newComposedMessage) {
        EditMessageText request = new EditMessageText(chatId, id, newComposedMessage.text())
                .parseMode(newComposedMessage.parseMode());

        if (newComposedMessage.keyboard() != null)
            request = request.replyMarkup((InlineKeyboardMarkup) newComposedMessage.keyboard());

        if (newComposedMessage.linkPreviewOptions() != null)
            request = request.linkPreviewOptions(newComposedMessage.linkPreviewOptions());

        return core.execute(request);
    }

    public CompletableFuture<BaseResponse> answerCallbackQuery(String id, String text, boolean showAlert, String url) {
        AnswerCallbackQuery request = new AnswerCallbackQuery(id).showAlert(showAlert);

        if (text != null)
            request.text(text);

        if (url != null)
            request.url(url);

        return core.execute(request);
    }

    public CompletableFuture<BaseResponse> answerCallbackQuery(String id) {
        return answerCallbackQuery(id, null, false, null);
    }

    public ScheduledFuture<?> setActionUntil(CompletableFuture<?> future, Supplier<ChatAction> action) {
        var ref = new Object() {
            ScheduledFuture<?> schedule = null;
        };

        ref.schedule = scheduledExecutor.scheduleAtFixedRate(() -> {
            if (future.isDone()) {
                ref.schedule.cancel(false);
                return;
            }

            core.execute(new SendChatAction(chatId, action.get()));
        }, 0, 4, TimeUnit.SECONDS);

        return ref.schedule;
    }

    public ScheduledFuture<?> setActionUntil(CompletableFuture<?> future, ChatAction action) {
        return setActionUntil(future, () -> action);
    }

    public CompletableFuture<BaseResponse> deleteMessage(int id) {
        DeleteMessage request = new DeleteMessage(chatId, id);

        return core.execute(request);
    }
}
