package app.finwave.tat.scene;

import app.finwave.tat.handlers.scened.ScenedAbstractChatHandler;
import app.finwave.tat.menu.MessageMenu;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MenuBasedScene<T> extends BaseScene<T> {
    protected int menuMessageId = -1;

    public MenuBasedScene(ScenedAbstractChatHandler chatHandler) {
        super(chatHandler);
    }

    public CompletableFuture<? extends BaseResponse> show(MessageMenu<?> menu) {
        menu.setUseSentMessage(menuMessageId);

        return menu.apply().whenComplete((r, t) -> {
            if (t != null || !r.isOk())
                return;

            if (r instanceof SendResponse sendResponse)
                menuMessageId = sendResponse.message().messageId();
        });
    }

    @Override
    public void start() {
        super.start();

        menuMessageId = -1;
    }

    @Override
    public void stop() {
        super.stop();

        if (menuMessageId == -1)
            return;

        try {
            chatHandler.deleteMessage(menuMessageId).get();
        } catch (InterruptedException | ExecutionException ignored) {}
    }
}
