package su.knst.tat.menu;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.response.BaseResponse;
import su.knst.tat.event.chat.CallbackQueryEvent;
import su.knst.tat.event.handler.EventListener;
import su.knst.tat.event.handler.HandlerRemover;
import su.knst.tat.scene.BaseScene;
import su.knst.tat.utils.CodeGenerator;
import su.knst.tat.utils.ComposedMessage;
import su.knst.tat.utils.MessageBuilder;
import su.knst.tat.utils.Pair;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class BaseMenu {
    protected BaseScene<?> scene;
    protected ArrayList<Pair<InlineKeyboardButton, HandlerRemover>> buttons = new ArrayList<>();
    protected Message sentMessage;
    protected ComposedMessage composedMessage;
    protected int maxButtonsInRow = 2;
    protected ArrayList<Integer> buttonsInRows;

    public BaseMenu(BaseScene<?> scene, boolean useLastMessage) {
        this.scene = scene;

        if (useLastMessage) {
            sentMessage = scene.getChatHandler().getLastSentMessage();
        }
    }

    public BaseMenu(BaseScene<?> scene, Message sentMessage) {
        this.scene = scene;
        this.sentMessage = sentMessage;
    }

    public BaseMenu(BaseScene<?> scene) {
        this(scene, true);
    }

    public HandlerRemover addButton(InlineKeyboardButton button, EventListener<CallbackQueryEvent> listener) {
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

        Pair<InlineKeyboardButton, HandlerRemover> pair = new Pair<>(button, handlerRemover);

        buttons.add(pair);

        return () -> {
            if (handlerRemover != null)
                handlerRemover.remove();

            buttons.remove(pair);
        };
    }

    public HandlerRemover addButton(InlineKeyboardButton button) {
        return addButton(button, null);
    }

    public void removeAllButtons() {
        for (var pair : buttons) {
            HandlerRemover remover = pair.second();

            if (remover != null)
                remover.remove();
        }

        buttons.clear();
    }

    public BaseMenu setMessage(ComposedMessage composedMessage) {
        this.composedMessage = composedMessage;

        return this;
    }

    public BaseMenu setMaxButtonsInRow(int maxButtonsInRow) {
        this.maxButtonsInRow = maxButtonsInRow;

        return this;
    }

    public void setButtonsInRows(ArrayList<Integer> buttonsInRows) {
        this.buttonsInRows = buttonsInRows;
    }

    public void setButtonsInRows(int... buttons) {
        this.buttonsInRows = new ArrayList<>();

        for (int b : buttons)
            buttonsInRows.add(b);
    }

    public ComposedMessage getMessage() {
        return composedMessage;
    }

    public int getMaxButtonsInRow() {
        return maxButtonsInRow;
    }

    protected CompletableFuture<BaseResponse> update() {
        if (sentMessage == null || composedMessage == null)
            return CompletableFuture.failedFuture(new IllegalStateException());

        if (composedMessage.keyboard() == null)
            composedMessage = MessageBuilder.create(composedMessage).setKeyboard(buildKeyboard()).build();

        return scene.getChatHandler().editMessage(sentMessage.messageId(), composedMessage);
    }

    protected InlineKeyboardMarkup buildKeyboard() {
        InlineKeyboardMarkup result = new InlineKeyboardMarkup();

        int rows = buttonsInRows == null ? (int)Math.ceil(buttons.size() / (float) maxButtonsInRow) : buttonsInRows.size();
        int addedButtons = 0;

        for (int i = 0; i < rows; i++) {
            int buttonsInRow = buttonsInRows == null ? Math.min(maxButtonsInRow, buttons.size() - i * maxButtonsInRow) : buttonsInRows.get(i);

            InlineKeyboardButton[] row = new InlineKeyboardButton[buttonsInRow];

            for (int b = 0; b < row.length; b++) {
                row[b] = buttons.get(addedButtons).first();
                addedButtons++;
            }

            result.addRow(row);
        }

        return result;
    }

    public CompletableFuture<? extends BaseResponse> apply() {
        if (composedMessage == null)
            return CompletableFuture.failedFuture(new IllegalStateException());

        if (sentMessage != null)
            return update();

        if (composedMessage.keyboard() == null)
            composedMessage = MessageBuilder.create(composedMessage).setKeyboard(buildKeyboard()).build();

        return scene.getChatHandler().sendMessage(composedMessage).whenComplete((r, t) -> {
            if (t != null) {
                t.printStackTrace();
                return;
            }

            sentMessage = r.message();
        });
    }

    public CompletableFuture<BaseResponse> delete() {
        if (sentMessage == null)
            return CompletableFuture.failedFuture(new IllegalStateException());

        return scene.getChatHandler().deleteMessage(sentMessage.messageId()).whenComplete((r, t) -> {
            if (t == null)
                this.sentMessage = null;
        });
    }
}
