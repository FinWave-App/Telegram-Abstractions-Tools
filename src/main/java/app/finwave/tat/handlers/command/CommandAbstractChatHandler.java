package app.finwave.tat.handlers.command;

import app.finwave.tat.BotCore;
import app.finwave.tat.handlers.AbstractChatHandler;
import app.finwave.tat.event.chat.NewMessageEvent;
import app.finwave.tat.event.handler.HandlerRemover;
import app.finwave.tat.utils.ComposedMessage;
import app.finwave.tat.utils.MessageBuilder;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CommandAbstractChatHandler extends AbstractChatHandler {
    protected HashMap<String, AbstractCommand> commands = new HashMap<>();
    protected HandlerRemover commandsListenerRemover;

    public CommandAbstractChatHandler(BotCore core, long chatId) {
        super(core, chatId);

        this.commandsListenerRemover = eventHandler.setValidator(NewMessageEvent.class, (e) -> !commandListener(e));
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

    public boolean runCommand(String command, NewMessageEvent event, String... args) {
        if (command.contains("@")) {
            String[] commandAndMe = command.split("@");
            Optional<User> me = core.getMe();

            if (me.isEmpty() || !commandAndMe[1].equals(me.get().username()))
                return true;

            command = commandAndMe[0];
        }else if (event.data.chat().type() != Chat.Type.Private)
            return true;

        if (!commands.containsKey(command)) {
            commandNotFound(command, args, event);

            return true;
        }

        commands.get(command).run(args, event);

        return true;
    }

    protected boolean commandListener(NewMessageEvent event) {
        String text = event.data.text();

        if (text == null || !text.startsWith("/"))
            return false;

        String[] commandAndArgs = text.substring(1).split(" ");
        String[] args = Arrays.copyOfRange(commandAndArgs, 1, commandAndArgs.length);

        return runCommand(commandAndArgs[0], event, args);
    }
}
