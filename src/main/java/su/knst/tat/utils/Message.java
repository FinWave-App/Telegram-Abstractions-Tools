package su.knst.tat.utils;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;

public class Message {
    public final Keyboard keyboard;
    public final ParseMode parseMode;
    public final boolean webPagePreview;
    public final boolean notification;
    public final int replyTo;
    public final String text;

    public Message(Keyboard keyboard, ParseMode parseMode, boolean webPagePreview, boolean notification, int replyTo, String text) {
        this.keyboard = keyboard;
        this.parseMode = parseMode;
        this.webPagePreview = webPagePreview;
        this.notification = notification;
        this.replyTo = replyTo;
        this.text = text;
    }
}
