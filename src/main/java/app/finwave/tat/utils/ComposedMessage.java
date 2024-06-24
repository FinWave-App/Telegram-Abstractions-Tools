package app.finwave.tat.utils;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;

public record ComposedMessage(Keyboard keyboard, ParseMode parseMode, boolean webPagePreview, boolean notification, int replyTo, String text) {
}
