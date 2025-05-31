package app.finwave.tat.scene;

import app.finwave.tat.handlers.scened.ScenedAbstractChatHandler;
import app.finwave.tat.menu.AbstractMessageMenu;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public abstract class MenuBasedScene<T> extends AbstractScene<T> {
    protected int menuMessageId = -1;
    protected HashMap<String, AbstractMessageMenu<?>> registeredMenu = new HashMap<>();

    public MenuBasedScene(ScenedAbstractChatHandler chatHandler) {
        super(chatHandler);
    }

    protected void registerMenu(String name, AbstractMessageMenu<?> menu) {
        registeredMenu.put(name, menu);
    }

    protected CompletableFuture<? extends BaseResponse> show(String name) {
        AbstractMessageMenu<?> menu = registeredMenu.get(name);

        if (menu == null)
            throw new IllegalArgumentException("Menu not found: " + name);

        return show(menu);
    }

    protected CompletableFuture<? extends BaseResponse> show(AbstractMessageMenu<?> menu) {
        CompletableFuture<? extends BaseResponse> response = menuMessageId != -1 ? menu.apply(menuMessageId) : menu.apply();

        return response.whenComplete((r, t) -> {
            if (t != null || !r.isOk())
                return;

            if (r instanceof SendResponse sendResponse)
                menuMessageId = sendResponse.message().messageId();
        });
    }

    @Override
    public void start(T arg) {
        menuMessageId = -1;
    }

    @Override
    public void stop() {
        if (menuMessageId == -1)
            return;

        try {
            chatHandler.deleteMessage(menuMessageId).get();
        } catch (InterruptedException | ExecutionException ignored) {}
    }
}
