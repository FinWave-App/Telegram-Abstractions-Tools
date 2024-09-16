package app.finwave.tat;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import app.finwave.tat.handlers.AbstractChatHandler;
import app.finwave.tat.handlers.AbstractGlobalHandler;
import app.finwave.tat.handlers.AbstractUserHandler;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class BotCore {
    protected final String token;
    protected final TelegramBot bot;
    protected UpdatesProcessor updatesProcessor;

    public ArrayList<Runnable> tasks = new ArrayList<>();

    protected ReentrantLock tasksLock = new ReentrantLock();

    protected ScheduledExecutorService tasksService = Executors.newScheduledThreadPool(1);

    public BotCore(String token, long taskRunPeriod) {
        this.token = token;
        this.bot = new TelegramBot(token);

        this.tasksService.scheduleAtFixedRate(this::runTask, 0, taskRunPeriod, TimeUnit.MILLISECONDS);
        this.setUpdatesProcessor(new UpdatesProcessor());
    }

    public BotCore(String token) {
        this(token, 125);
    }

    public void setUpdatesProcessor(UpdatesProcessor updatesProcessor) {
        this.updatesProcessor = updatesProcessor;
        this.bot.setUpdatesListener(updatesProcessor);
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

        tasksLock.lock();

        try {
            tasks.add(runnable);
        }finally {
            tasksLock.unlock();
        }

        return future;
    }

    public void runTask() {
        tasksLock.lock();

        try {
            if (tasks.isEmpty())
                return;

            tasks.remove(0).run();
        }finally {
            tasksLock.unlock();
        }
    }
}
