# Telegram Abstractions Tools

Telegram Abstractions Tools (TAT) is a Java library designed to simplify the creation of Telegram bots by providing an abstraction layer. Instead of dealing directly with requests and responses, developers work with handlers that manage the bot's context. These handlers include chat handlers, user handlers, and global handlers which make bot development more intuitive and organized.

Powered by [Java Telegram Bot API](https://github.com/pengrad/java-telegram-bot-api)

## In The Context Of The FinWave Project:

This library is used in the FinWave Telegram bot, as well as in some other projects. While not all Telegram API features are implemented, only those that are necessary are included.

## Features

- **AbstractChatHandler**: For managing chat interactions.
- **BaseMenu**: Simplifies button handling in messages by allowing lambda functions.
- **CommandAbstractChatHandler**: Handles text commands from chat messages.
- **ScenedAbstractChatHandler**: Supports multiple scenes within a chat.
- **Event Handling**: A robust event handling system to manage various types of events such as messages, callback queries, polls, and more.

## Install
### Gradle

```gradle
repositories {
    mavenCentral()
    maven {
        url "https://nexus.finwave.app/repository/maven-public"
    }
}

dependencies {
    implementation 'su.knst.tat:telegram-abstractions-tools:2.0.0'
}
```

## Some Examples

### Setting up the BotCore

```java
import su.knst.tat.BotCore;
import su.knst.tat.handlers.AbstractChatHandler;
import su.knst.tat.handlers.AbstractGlobalHandler;
import su.knst.tat.handlers.AbstractUserHandler;

public class MyBot {
    public static void main(String[] args) {
        String token = "YOUR_BOT_TOKEN";
        
        botCore = new BotCore(token);
        botCore.setHandlers(new GlobalHandler(botCore), // One for the whole bot
                chatId -> new MyChatHandler(botCore, chatId), // For each user
                userId -> new UserHandler(botCore, userId)); // For each chat
    }
}
```

### Creating a Chat Handler

```java
import su.knst.tat.handlers.AbstractChatHandler;
import su.knst.tat.BotCore;
import su.knst.tat.event.chat.NewMessageEvent;

public class MyChatHandler extends AbstractChatHandler {
    public MyChatHandler(BotCore core, long chatId) {
        super(core, chatId);
    }

    @Override
    public void start() {
        eventHandler.registerListener(NewMessageEvent.class, this::onNewMessage);
    }

    private void onNewMessage(NewMessageEvent event) {
        System.out.println("New message received: " + event.data.text());
    }
}
```

### Using Command Handler

```java
import su.knst.tat.handlers.command.CommandAbstractChatHandler;
import su.knst.tat.BotCore;
import su.knst.tat.event.chat.NewMessageEvent;
import su.knst.tat.handlers.command.AbstractCommand;

public class MyCommandHandler extends CommandAbstractChatHandler {
    public MyCommandHandler(BotCore core, long chatId) {
        super(core, chatId);
        registerCommand(AbstractCommand.command("start", this::startCommand));
    }

    private void startCommand(String[] args, NewMessageEvent event) {
        sendMessage(MessageBuilder.text("Welcome!")).thenAccept(response -> {
            System.out.println("Sent welcome message");
        });
    }
}
```

### Creating a Menu

```java
import su.knst.tat.menu.BaseMenu;
import su.knst.tat.scene.BaseScene;
import su.knst.tat.BotCore;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import su.knst.tat.utils.MessageBuilder;

public class MyMenu extends BaseMenu {
    public MyMenu(BaseScene<?> scene) {
        super(scene);
        setMessage(MessageBuilder.text("Choose an option:"));
        addButton(new InlineKeyboardButton("Option 1"), this::onOption1);
        addButton(new InlineKeyboardButton("Option 2"), this::onOption2);
    }

    private void onOption1(CallbackQueryEvent event) {
        // Handle option 1 action
    }

    private void onOption2(CallbackQueryEvent event) {
        // Handle option 2 action
    }
}
```

### Defining a Scene

```java
import su.knst.tat.scene.BaseScene;
import su.knst.tat.handlers.AbstractChatHandler;

public class MyScene extends BaseScene<String> {
    private MyMenu menu;

    public MyScene(AbstractChatHandler abstractChatHandler) {
        super(abstractChatHandler);
        menu = new MyMenu(this);
    }

    @Override
    public void start() {
        super.start();
        menu.apply();
    }

    @Override
    public void start(String arg) {
        super.start(arg);
        menu.apply();
    }
}
```

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.