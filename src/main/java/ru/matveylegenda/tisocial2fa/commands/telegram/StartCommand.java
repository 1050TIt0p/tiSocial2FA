package ru.matveylegenda.tisocial2fa.commands.telegram;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;

public class StartCommand {
    private TiSocial2FA plugin = TiSocial2FA.getInstance();

    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            long chatId = update.getMessage().getChatId();

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text("ID чата: " + chatId)
                    .build();

            TelegramClient telegramClient = new OkHttpTelegramClient(plugin.config.telegram.token);

            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
