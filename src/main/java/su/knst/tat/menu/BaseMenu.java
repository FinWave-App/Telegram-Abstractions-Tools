package su.knst.tat.menu;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import su.knst.tat.BotCore;
import su.knst.tat.event.chat.CallbackQueryEvent;
import su.knst.tat.event.handler.EventListener;
import su.knst.tat.event.handler.EventListenerRemover;
import su.knst.tat.scene.BaseScene;
import su.knst.tat.utils.CodeGenerator;
import su.knst.tat.utils.MessageBuilder;
import su.knst.tat.utils.Pair;

import java.util.ArrayList;

public class BaseMenu {
    protected BaseScene scene;
    protected ArrayList<Pair<InlineKeyboardButton, EventListenerRemover>> buttons = new ArrayList<>();
    protected Message sentMessage;
    protected su.knst.tat.utils.Message message;
    protected int maxButtonsInRow = 2;
    public BaseMenu(BaseScene scene) {
        this.scene = scene;
    }

    public EventListenerRemover addButton(InlineKeyboardButton button, EventListener<CallbackQueryEvent> listener) {
        EventListenerRemover eventListenerRemover;

        if (listener != null) {
            String code = CodeGenerator.generateRandomCode(16);

            button.callbackData(code);

            eventListenerRemover = scene.getEventHandler().registerListener(CallbackQueryEvent.class, (e) -> {
                if (e.data.data().equals(code))
                    listener.event(e);
            });
        } else {
            eventListenerRemover = null;
        }

        Pair<InlineKeyboardButton, EventListenerRemover> pair = new Pair<>(button, eventListenerRemover);

        buttons.add(pair);

        return () -> {
            if (eventListenerRemover != null)
                eventListenerRemover.remove();

            buttons.remove(pair);
        };
    }

    public EventListenerRemover addButton(InlineKeyboardButton button) {
        return addButton(button, null);
    }

    public void removeAllButtons() {
        for (var pair : buttons) {
            EventListenerRemover remover = pair.second();

            if (remover != null)
                remover.remove();
        }

        buttons.clear();
    }

    public BaseMenu setMessage(su.knst.tat.utils.Message message) {
        this.message = message;

        return this;
    }

    public BaseMenu setMaxButtonsInRow(int maxButtonsInRow) {
        this.maxButtonsInRow = maxButtonsInRow;

        return this;
    }

    public Message getSentMessage() {
        return sentMessage;
    }

    public su.knst.tat.utils.Message getMessage() {
        return message;
    }

    public int getMaxButtonsInRow() {
        return maxButtonsInRow;
    }

    public void update() {
        if (sentMessage == null || message == null)
            return;

        if (message.keyboard == null)
            message = MessageBuilder.create(message).setKeyboard(buildKeyboard()).build();

        scene.getChatHandler().editMessage(sentMessage.messageId(), message)
                .whenComplete((r, t) -> {
                    if (t != null)
                        t.printStackTrace();
                });
    }

    protected InlineKeyboardMarkup buildKeyboard() {
        InlineKeyboardMarkup result = new InlineKeyboardMarkup();

        int rows = (int)Math.ceil(buttons.size() / (float) maxButtonsInRow);

        for (int i = 0; i < rows; i++) {
            int buttonsInRow = Math.min(maxButtonsInRow, buttons.size() - i * maxButtonsInRow);

            InlineKeyboardButton[] row = new InlineKeyboardButton[buttonsInRow];

            for (int b = 0; b < row.length; b++)
                row[b] = buttons.get(i * maxButtonsInRow + b).first();

            result.addRow(row);
        }

        return result;
    }

    public void apply() {
        if (message == null)
            return;

        if (sentMessage != null)
            delete();

        if (message.keyboard == null)
            message = MessageBuilder.create(message).setKeyboard(buildKeyboard()).build();

        scene.getChatHandler().sendMessage(message).whenComplete((r, t) -> {
            if (t != null) {
                t.printStackTrace();
                return;
            }

            sentMessage = r.message();
        });
    }

    public void delete() {
        if (sentMessage == null)
            return;

        scene.getChatHandler().deleteMessage(sentMessage.messageId());
        this.sentMessage = null;
    }
}
