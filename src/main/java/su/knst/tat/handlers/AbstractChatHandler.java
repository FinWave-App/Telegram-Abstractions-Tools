package su.knst.tat.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import su.knst.tat.BotCore;
import su.knst.tat.event.ChatEvent;
import su.knst.tat.utils.ComposedMessage;
import su.knst.tat.utils.Stack;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractChatHandler extends AbstractContextHandler<ChatEvent<?>> {
    protected final long chatId;
    protected Stack<Message> sentMessages = new Stack<>(100);

    public AbstractChatHandler(BotCore core, long chatId) {
        super(core);

        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    public abstract void start();

    public Message getLastSentMessage() {
        return sentMessages.peek();
    }

    public void pushLastSentMessage(Message lastSentMessage) {
        sentMessages.push(lastSentMessage);
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
                    if (t != null)
                        return;

                    sentMessages.push(r.message());
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

    public CompletableFuture<BaseResponse> deleteMessage(int id) {
        DeleteMessage request = new DeleteMessage(chatId, id);

        return core.execute(request)
                .whenComplete((r, t) -> {
                    if (t != null)
                        return;

                    sentMessages.remove((m) -> m.messageId() == id);
                });
    }
}
