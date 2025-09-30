package app.finwave.tat;

import app.finwave.tat.event.ChatEvent;
import app.finwave.tat.event.Event;
import app.finwave.tat.event.UserEvent;
import app.finwave.tat.event.chat.*;
import com.google.common.cache.LoadingCache;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import app.finwave.tat.event.global.NewPollEvent;
import app.finwave.tat.event.user.ChosenInlineResultEvent;
import app.finwave.tat.event.user.InlineQueryEvent;
import app.finwave.tat.event.user.PreCheckoutQueryEvent;
import app.finwave.tat.event.user.ShippingQueryEvent;
import app.finwave.tat.handlers.AbstractChatHandler;
import app.finwave.tat.handlers.AbstractGlobalHandler;
import app.finwave.tat.handlers.AbstractUserHandler;
import com.pengrad.telegrambot.request.GetUpdates;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class UpdatesProcessor implements UpdatesListener {
    protected LoadingCache<Long, AbstractChatHandler> chatsCache;
    protected LoadingCache<Long, AbstractUserHandler> usersCache;

    protected AbstractGlobalHandler abstractGlobalHandler;

    protected Function<Update, Boolean> updateValidator;

    protected GetUpdates updatesRequest = new GetUpdates().allowedUpdates(
            "message", "edited_message", "message_reaction",
            "channel_post", "edited_channel_post", "inline_query",
            "chosen_inline_result", "callback_query", "shipping_query",
            "pre_checkout_query", "poll", "poll_answer",
            "chat_join_request"
    );

    public void setUsersCache(LoadingCache<Long, AbstractUserHandler> usersCache) {
        this.usersCache = usersCache;
    }

    public void setChatsCache(LoadingCache<Long, AbstractChatHandler> chatsCache) {
        this.chatsCache = chatsCache;
    }

    public void setAbstractGlobalHandler(AbstractGlobalHandler abstractGlobalHandler) {
        this.abstractGlobalHandler = abstractGlobalHandler;

        abstractGlobalHandler.start();
    }

    public void setUpdateValidator(Function<Update, Boolean> updateValidator) {
        this.updateValidator = updateValidator;
    }

    protected void fireEvent(Event<?> event) throws ExecutionException {
        if (event == null)
            return;

        if (event instanceof ChatEvent<?> chatEvent) {
            if (chatsCache == null)
                return;

            var contextHandler = chatsCache.get(chatEvent.chatId);

            contextHandler.getEventHandler().fire(chatEvent);
        }else if (event instanceof UserEvent<?> userEvent) {
            if (usersCache == null)
                return;

            var contextHandler = usersCache.get(userEvent.userId);

            contextHandler.getEventHandler().fire(userEvent);
        }else if (event instanceof NewPollEvent){
            if (abstractGlobalHandler == null)
                return;

            abstractGlobalHandler.getEventHandler().fire(event);
        }
    }

    public GetUpdates getUpdatesRequest() {
        return updatesRequest;
    }

    @Override
    public int process(List<Update> updates) {
        int lastUpdateId = CONFIRMED_UPDATES_NONE;

        for (Update update : updates) {
            lastUpdateId = update.updateId();

            if (updateValidator != null && !updateValidator.apply(update))
                continue;

            try {
                fireEvent(NewMessageEvent.fromUpdate(update));
                fireEvent(EditedMessageEvent.fromUpdate(update));
                fireEvent(MessageReactionEvent.fromUpdate(update));
                fireEvent(NewChannelPostEvent.fromUpdate(update));
                fireEvent(EditedChannelPostEvent.fromUpdate(update));
                fireEvent(CallbackQueryEvent.fromUpdate(update));
                fireEvent(PollAnswerEvent.fromUpdate(update));
                fireEvent(UpdatedBotEvent.fromUpdate(update));
                fireEvent(UpdatedChatMemberEvent.fromUpdate(update));
                fireEvent(ChatJoinRequestEvent.fromUpdate(update));
                fireEvent(InlineQueryEvent.fromUpdate(update));
                fireEvent(ChosenInlineResultEvent.fromUpdate(update));
                fireEvent(ShippingQueryEvent.fromUpdate(update));
                fireEvent(PreCheckoutQueryEvent.fromUpdate(update));
                fireEvent(NewPollEvent.fromUpdate(update));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return lastUpdateId;
    }
}
