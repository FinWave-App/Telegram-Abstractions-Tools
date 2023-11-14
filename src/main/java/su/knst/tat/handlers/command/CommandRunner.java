package su.knst.tat.handlers.command;

import su.knst.tat.event.chat.NewMessageEvent;

public interface CommandRunner {
    void run(String[] args, NewMessageEvent event);
}
