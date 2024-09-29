package app.finwave.tat;

import app.finwave.tat.event.ChatEvent;
import app.finwave.tat.event.Event;
import app.finwave.tat.event.UserEvent;
import app.finwave.tat.event.chat.*;
import app.finwave.tat.handlers.AbstractContextHandler;
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

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class UpdatesProcessor implements UpdatesListener {
    protected HashMap<Long, AbstractChatHandler> chats = new HashMap<>();
    protected HashMap<Long, AbstractUserHandler> users = new HashMap<>();

    protected AbstractGlobalHandler abstractGlobalHandler;
    protected Function<Long, ? extends AbstractChatHandler> chatHandlerGenerator;
    protected Function<Long, ? extends AbstractUserHandler> userHandlerGenerator;

    protected Function<Update, Boolean> updateValidator;

    protected GetUpdates updatesRequest = new GetUpdates().allowedUpdates("message", "message_reaction", "edited_channel_post", "callback_query");

    public void setAbstractGlobalHandler(AbstractGlobalHandler abstractGlobalHandler) {
        this.abstractGlobalHandler = abstractGlobalHandler;

        abstractGlobalHandler.start();
    }

    public void setChatHandlerGenerator(Function<Long, ? extends AbstractChatHandler> chatHandlerGenerator) {
        this.chatHandlerGenerator = chatHandlerGenerator;
    }

    public void setUserHandlerGenerator(Function<Long, ? extends AbstractUserHandler> userHandlerGenerator) {
        this.userHandlerGenerator = userHandlerGenerator;
    }

    protected AbstractChatHandler getOrCreateChatHandler(long chatId) {
        if (chatHandlerGenerator == null)
            return null;

        if (!chats.containsKey(chatId)) {
            AbstractChatHandler handler = chatHandlerGenerator.apply(chatId);
            handler.start();

            chats.put(chatId, handler);

            return handler;
        }

        return chats.get(chatId);
    }

    protected AbstractUserHandler getOrCreateUserHandler(long userId) {
        if (userHandlerGenerator == null)
            return null;

        if (!users.containsKey(userId)) {
            AbstractUserHandler handler = userHandlerGenerator.apply(userId);

            users.put(userId, handler);

            return handler;
        }

        return users.get(userId);
    }

    public void setUpdateValidator(Function<Update, Boolean> updateValidator) {
        this.updateValidator = updateValidator;
    }

    protected void fireEvent(Event<?> event) {
        if (event == null)
            return;

        if (event instanceof ChatEvent<?> chatEvent) {
            var contextHandler = getOrCreateChatHandler(chatEvent.chatId);

            if (contextHandler == null)
                return;

            contextHandler.getEventHandler().fire(chatEvent);
        }else if (event instanceof UserEvent<?> userEvent) {
            var contextHandler = getOrCreateUserHandler(userEvent.userId);

            if (contextHandler == null)
                return;

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

            lastUpdateId = update.updateId();
        }

        return lastUpdateId;
    }
}
