package su.knst.tat.handlers.command;

import su.knst.tat.BotCore;
import su.knst.tat.event.chat.NewMessageEvent;
import su.knst.tat.event.handler.HandlerRemover;
import su.knst.tat.handlers.AbstractChatHandler;
import su.knst.tat.utils.ComposedMessage;
import su.knst.tat.utils.MessageBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandAbstractChatHandler extends AbstractChatHandler {
    protected HashMap<String, AbstractCommand> commands = new HashMap<>();
    protected HandlerRemover commandsListenerRemover;

    public CommandAbstractChatHandler(BotCore core, long chatId) {
        super(core, chatId);

        this.commandsListenerRemover = eventHandler.setValidator(NewMessageEvent.class, this::commandListener);
    }

    public void registerCommand(AbstractCommand command) {
        command.init(this);

        this.commands.put(command.name(), command);
    }

    public ComposedMessage commandsDescription() {
        MessageBuilder builder = MessageBuilder.create();

        for (Map.Entry<String, AbstractCommand> commandEntry : commands.entrySet()) {
            if (commandEntry.getValue().hidden())
                continue;

            builder.append("/").append(commandEntry.getKey());

            String tips = commandEntry.getValue().commandArgsTips();
            if (tips != null && !tips.isBlank())
                builder.append(" ").append(tips);

            String description = commandEntry.getValue().description();
            if (description != null && !description.isBlank())
                builder.append(" - ").append(description);

            builder.gap();
        }

        return builder.build();
    }

    protected void commandNotFound(String command, String[] args, NewMessageEvent event) {

    }

    public void runCommand(String command, NewMessageEvent event, String... args) {
        if (!commands.containsKey(command)) {
            commandNotFound(command, args, event);

            return;
        }

        commands.get(command).run(args, event);
    }

    protected boolean commandListener(NewMessageEvent event) {
        String text = event.data.text();

        if (!text.startsWith("/"))
            return true;

        String[] commandAndArgs = text.substring(1).split(" ");
        String[] args = Arrays.copyOfRange(commandAndArgs, 1, commandAndArgs.length);

        runCommand(commandAndArgs[0], event, args);

        return false;
    }
}
