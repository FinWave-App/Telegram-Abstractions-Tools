package app.finwave.tat.handlers;

import app.finwave.tat.BotCore;
import app.finwave.tat.utils.Pair;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import app.finwave.tat.event.ChatEvent;
import app.finwave.tat.utils.ComposedMessage;
import app.finwave.tat.utils.Stack;

import java.util.concurrent.*;
import java.util.function.Supplier;

public abstract class AbstractChatHandler extends AbstractContextHandler<ChatEvent<?>> {
    protected final long chatId;
    protected Stack<Pair<Integer, Message>> sentMessages = new Stack<>(100);
    protected ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);

    public AbstractChatHandler(BotCore core, long chatId) {
        super(core);

        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    public abstract void start();

    public Message getLastSentMessage() {
        return sentMessages.peekOptional().map(Pair::second).orElse(null);
    }

    public int getLastSentMessageId() {
        return sentMessages.peekOptional().map(Pair::first).orElse(-1);
    }

    public void pushLastSentMessage(Message lastSentMessage) {
        sentMessages.push(new Pair<>(lastSentMessage.messageId(), lastSentMessage));
    }

    public void pushLastSentMessageId(int lastSentMessageId) {
        sentMessages.push(new Pair<>(lastSentMessageId, null));
    }

    public CompletableFuture<SendResponse> sendInvoice(String title, String description, String payload, String currency, LabeledPrice... prices) {
        SendInvoice request = new SendInvoice(chatId, title, description, payload, currency, prices);

        return core.execute(request)
                .whenComplete((r, t) -> {
                    if (t != null || r.message() == null)
                        return;

                    pushLastSentMessage(r.message());
                });
    }

    public CompletableFuture<SendResponse> sendMessage(ComposedMessage composedMessage) {
        SendMessage request = new SendMessage(chatId, composedMessage.text())
                .parseMode(composedMessage.parseMode())
                .disableWebPagePreview(!composedMessage.webPagePreview())
                .disableNotification(!composedMessage.notification());

        if (composedMessage.keyboard() != null)
            request = request.replyMarkup(composedMessage.keyboard());

        if (composedMessage.replyTo() != -1)
            request = request.replyToMessageId(composedMessage.replyTo());

        return core.execute(request)
                .whenComplete((r, t) -> {
                    if (t != null || r.message() == null)
                        return;

                    pushLastSentMessage(r.message());
                });
    }

    public CompletableFuture<BaseResponse> editMessage(int id, ComposedMessage newComposedMessage) {
        EditMessageText request = new EditMessageText(chatId, id, newComposedMessage.text())
                .parseMode(newComposedMessage.parseMode())
                .disableWebPagePreview(!newComposedMessage.webPagePreview());

        if (newComposedMessage.keyboard() instanceof InlineKeyboardMarkup)
            request = request.replyMarkup((InlineKeyboardMarkup) newComposedMessage.keyboard());

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

        return core.execute(request)
                .whenComplete((r, t) -> {
                    if (t != null)
                        return;

                    sentMessages.remove((m) -> m.first() == id);
                });
    }
}
