package app.finwave.tat.utils;

import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyParameters;
import com.pengrad.telegrambot.model.suggestedposts.SuggestedPostParameters;
import com.pengrad.telegrambot.request.SendMessage;

public record ComposedMessage(Keyboard keyboard,
                              ParseMode parseMode,
                              LinkPreviewOptions linkPreviewOptions,
                              ReplyParameters replyParameters,
                              boolean notification,
                              boolean protectContent,
                              String text) {

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .keyboard(this.keyboard)
                .parseMode(this.parseMode)
                .linkPreviewOptions(this.linkPreviewOptions)
                .replyParameters(this.replyParameters)
                .notification(this.notification)
                .protectContent(this.protectContent)
                .text(this.text);
    }

    public static final class Builder {
        private Keyboard keyboard;
        private ParseMode parseMode;
        private LinkPreviewOptions linkPreviewOptions;
        private ReplyParameters replyParameters;
        private boolean notification = true;
        private boolean protectContent = false;
        private StringBuilder text = new StringBuilder();

        private Builder() {}

        public Builder keyboard(Keyboard keyboard) {
            this.keyboard = keyboard;
            return this;
        }

        public Builder parseMode(ParseMode parseMode) {
            this.parseMode = parseMode;
            return this;
        }

        public Builder linkPreviewOptions(LinkPreviewOptions linkPreviewOptions) {
            this.linkPreviewOptions = linkPreviewOptions;
            return this;
        }

        public Builder replyParameters(ReplyParameters replyParameters) {
            this.replyParameters = replyParameters;
            return this;
        }

        public Builder notification(boolean notification) {
            this.notification = notification;
            return this;
        }

        public Builder protectContent(boolean protectContent) {
            this.protectContent = protectContent;
            return this;
        }

        public ComposedMessage.Builder line(String content) {
            text.append(content).append("\n");

            return this;
        }

        public ComposedMessage.Builder append(String content) {
            text.append(content);

            return this;
        }

        public ComposedMessage.Builder gap() {
            text.append("\n");

            return this;
        }

        public ComposedMessage.Builder bold() {
            text.append("*");

            return this;
        }

        public ComposedMessage.Builder italic() {
            text.append("_");

            return this;
        }

        public ComposedMessage.Builder underline() {
            text.append("__");

            return this;
        }

        public ComposedMessage.Builder strikethrough() {
            text.append("~");

            return this;
        }

        public ComposedMessage.Builder spoiler() {
            text.append("||");

            return this;
        }

        public ComposedMessage.Builder url(String url, String content) {
            text
                    .append("[")
                    .append(content)
                    .append("](")
                    .append(url)
                    .append(")");

            return this;
        }

        public ComposedMessage.Builder fixedWidth() {
            text.append("`");

            return this;
        }

        public ComposedMessage.Builder code() {
            text.append("```");

            return this;
        }

        public ComposedMessage.Builder text(String text) {
            this.text = new StringBuilder(text);

            return this;
        }

        public ComposedMessage build() {
            if (text == null || text.isEmpty()) {
                throw new IllegalStateException("text must not be null or blank");
            }

            return new ComposedMessage(
                    keyboard,
                    parseMode,
                    linkPreviewOptions,
                    replyParameters,
                    notification,
                    protectContent,
                    text.toString()
            );
        }
    }
}