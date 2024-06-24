package su.knst.tat.handlers.command;

import su.knst.tat.event.chat.NewMessageEvent;

public abstract class AbstractCommand implements CommandRunner {
    protected CommandAbstractChatHandler chatHandler;

    public static AbstractCommand command(String name, String description, String commandArgsTips, boolean hidden, CommandRunner runner) {
        return new AbstractCommand() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public String commandArgsTips() {
                return commandArgsTips;
            }

            @Override
            public boolean hidden() {
                return hidden;
            }

            @Override
            public void run(String[] args, NewMessageEvent event) {
                runner.run(args, event);
            }
        };
    }

    public static AbstractCommand command(String name, String description, String commandArgsTips, CommandRunner runner) {
        return command(name, description, commandArgsTips, false, runner);
    }

    public static AbstractCommand command(String name, String description, CommandRunner runner) {
        return command(name, description, null, runner);
    }

    public static AbstractCommand command(String name, CommandRunner runner) {
        return command(name, null, runner);
    }

    public void init(CommandAbstractChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    public abstract String name();
    public abstract String description();

    public abstract String commandArgsTips();

    public boolean hidden() {
        return false;
    }
}
