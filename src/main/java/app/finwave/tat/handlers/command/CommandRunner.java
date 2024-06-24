package app.finwave.tat.handlers.command;

import app.finwave.tat.event.chat.NewMessageEvent;

public interface CommandRunner {
    void run(String[] args, NewMessageEvent event);
}
