package app.finwave.tat.utils;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;

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

    public static MessageBuilder create(ComposedMessage composedMessage) {
        return new MessageBuilder(composedMessage);
    }

    public static MessageBuilder create(String text) {
        return new MessageBuilder(text);
    }

    public static ComposedMessage text(String text) {
        return new MessageBuilder(text).build();
    }

    protected MessageBuilder() {

    }

    protected MessageBuilder(String text) {
        this.stringBuilder.append(text);
    }

    protected MessageBuilder(ComposedMessage composedMessage) {
        this.stringBuilder.append(composedMessage.text());
        this.keyboard = composedMessage.keyboard();
        this.parseMode = composedMessage.parseMode();
        this.webPagePreview = composedMessage.webPagePreview();
        this.notification = composedMessage.notification();
        this.replyTo = composedMessage.replyTo();
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

    public ComposedMessage build() {
        return new ComposedMessage(keyboard, parseMode, webPagePreview, notification, replyTo, stringBuilder.toString());
    }
}
