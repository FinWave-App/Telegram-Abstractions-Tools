package su.knst.tat.handlers;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import su.knst.tat.BotCore;
import su.knst.tat.event.ChatEvent;
import su.knst.tat.utils.Message;

import java.util.concurrent.CompletableFuture;

public abstract class ChatHandler extends AbstractContextHandler<ChatEvent<?>> {
    protected final long chatId;

    public ChatHandler(BotCore core, long chatId) {
        super(core);

        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    public abstract void start();

    public CompletableFuture<SendResponse> sendMessage(Message message) {
        SendMessage request = new SendMessage(chatId, message.text)
                .parseMode(message.parseMode)
                .disableWebPagePreview(!message.webPagePreview)
                .disableNotification(!message.notification);

        if (message.keyboard != null)
            request = request.replyMarkup(message.keyboard);

        if (message.replyTo != -1)
            request = request.replyToMessageId(message.replyTo);

        return core.execute(request);
    }

    public CompletableFuture<BaseResponse> editMessage(int id, Message newMessage) {
        EditMessageText request = new EditMessageText(chatId, id, newMessage.text)
                .parseMode(newMessage.parseMode)
                .disableWebPagePreview(!newMessage.webPagePreview);

        if (newMessage.keyboard instanceof InlineKeyboardMarkup)
            request = request.replyMarkup((InlineKeyboardMarkup) newMessage.keyboard);

        return core.execute(request);
    }

    public CompletableFuture<BaseResponse> deleteMessage(int id) {
        DeleteMessage request = new DeleteMessage(chatId, id);

        return core.execute(request);
    }
}
