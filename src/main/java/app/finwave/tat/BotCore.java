package app.finwave.tat;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.response.BaseResponse;
import app.finwave.tat.handlers.AbstractChatHandler;
import app.finwave.tat.handlers.AbstractGlobalHandler;
import app.finwave.tat.handlers.AbstractUserHandler;
import com.pengrad.telegrambot.response.GetMeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class BotCore {
    protected final String token;
    protected final TelegramBot bot;
    protected UpdatesProcessor updatesProcessor;

    protected ScheduledExecutorService tasksService;
    protected HashMap<Object, Long> sendTimestamps = new HashMap<>();
    protected ReentrantLock timestampLock = new ReentrantLock();

    protected CompletableFuture<User> me;

    public BotCore(String token, int corePoolSize) {
        this.token = token;
        this.bot = new TelegramBot(token);

        this.tasksService = Executors.newScheduledThreadPool(corePoolSize);
        this.setUpdatesProcessor(new UpdatesProcessor());

        this.me = execute(new GetMe()).thenApply(GetMeResponse::user);
    }

    public BotCore(String token) {
        this(token, 2);
    }

    public void setUpdatesProcessor(UpdatesProcessor updatesProcessor) {
        this.updatesProcessor = updatesProcessor;
        this.bot.setUpdatesListener(updatesProcessor, updatesProcessor.getUpdatesRequest());
    }

    public UpdatesProcessor getUpdatesProcessor() {
        return updatesProcessor;
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> CompletableFuture<R> execute(BaseRequest<T, R> request) {
        CompletableFuture<R> future = new CompletableFuture<>();

        Runnable runnable = () -> {
            try {
                R response = bot.execute(request);
                future.complete(response);
            }catch (Throwable e) {
                future.completeExceptionally(e);
            }
        };

        tasksService.schedule(runnable, sendCooldown(request), TimeUnit.MILLISECONDS);

        return future;
    }

    protected long sendCooldown(BaseRequest<?, ?> request) {
        String chatId = Optional.ofNullable(request.getParameters().get("chat_id")).map(Object::toString).orElse(null);

        long sendAfter;
        long minSentDelay = chatId == null ? 125 : 1000;

        timestampLock.lock();
        try {
            long sendTimestamp = sendTimestamps.getOrDefault(chatId, 0L);
            long now = System.currentTimeMillis();
            long lastSent = now - sendTimestamp;

            sendAfter = Math.max(minSentDelay - lastSent, 0);
            sendTimestamps.put(chatId, sendTimestamp + sendAfter);
        }finally {
            timestampLock.unlock();
        }

        return sendAfter;
    }

    public Optional<User> getMe() {
        try {
            return Optional.ofNullable(me.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
