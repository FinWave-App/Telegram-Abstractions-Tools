package su.knst.tat.utils;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;

public class MessageBuilder {
    protected StringBuilder stringBuilder = new StringBuilder();
    protected Keyboard keyboard;
    protected ParseMode parseMode = ParseMode.Markdown;
    protected boolean webPagePreview = true;
    protected boolean notification = true;
    protected int replyTo = -1;

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public static MessageBuilder create(Message message) {
        return new MessageBuilder(message);
    }

    public static MessageBuilder create(String text) {
        return new MessageBuilder(text);
    }

    public static Message text(String text) {
        return new MessageBuilder(text).build();
    }

    protected MessageBuilder() {

    }

    protected MessageBuilder(String text) {
        this.stringBuilder.append(text);
    }

    protected MessageBuilder(Message message) {
        this.stringBuilder.append(message.text);
        this.keyboard = message.keyboard;
        this.parseMode = message.parseMode;
        this.webPagePreview = message.webPagePreview;
        this.notification = message.notification;
        this.replyTo = message.replyTo;
    }

    public MessageBuilder line(String content) {
        stringBuilder.append(content).append("\n");

        return this;
    }

    public MessageBuilder append(String content) {
        stringBuilder.append(content);

        return this;
    }

    public MessageBuilder gap() {
        stringBuilder.append("\n");

        return this;
    }

    public MessageBuilder bold() {
        stringBuilder.append("*");

        return this;
    }

    public MessageBuilder italic() {
        stringBuilder.append("_");

        return this;
    }

    public MessageBuilder underline() {
        stringBuilder.append("__");

        return this;
    }

    public MessageBuilder strikethrough() {
        stringBuilder.append("~");

        return this;
    }

    public MessageBuilder spoiler() {
        stringBuilder.append("||");

        return this;
    }

    public MessageBuilder url(String url, String content) {
        stringBuilder
                .append("[")
                .append(content)
                .append("](")
                .append(url)
                .append(")");

        return this;
    }

    public MessageBuilder fixedWidth() {
        stringBuilder.append("`");

        return this;
    }

    public MessageBuilder code() {
        stringBuilder.append("```");

        return this;
    }

    public MessageBuilder clearText() {
        this.stringBuilder = new StringBuilder();

        return this;
    }

    public MessageBuilder setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;

        return this;
    }

    public MessageBuilder setParseMode(ParseMode parseMode) {
        this.parseMode = parseMode;

        return this;
    }

    public MessageBuilder setWebPagePreview(boolean webPagePreview) {
        this.webPagePreview = webPagePreview;

        return this;
    }

    public MessageBuilder setNotification(boolean notification) {
        this.notification = notification;

        return this;
    }

    public MessageBuilder setReplyTo(int replyTo) {
        this.replyTo = replyTo;

        return this;
    }

    public Message build() {
        return new Message(keyboard, parseMode, webPagePreview, notification, replyTo, stringBuilder.toString());
    }
}
