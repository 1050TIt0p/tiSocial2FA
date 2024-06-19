package ru.matveylegenda.tisocial2fa.listeners;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;
import ru.matveylegenda.tisocial2fa.utils.BlockedList;
import ru.matveylegenda.tisocial2fa.utils.ColorParser;
import ru.matveylegenda.tisocial2fa.utils.Config;

public class AllowJoin extends ListenerAdapter {
    private TiSocial2FA plugin = TiSocial2FA.getInstance();
    private BlockedList blockedList = new BlockedList();
    private Config config = plugin.config;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String discordID = event.getUser().getId();

        if (event.isFromGuild()) {
            return;
        }

        if (event.getComponentId().equals("2fa-allow-join")) {
            for (String nick : config.discord.users.keySet()) {
                if (config.discord.users.get(nick).equals(discordID)) {

                    if (event.isAcknowledged()) {
                        return;
                    }

                    Player player = Bukkit.getPlayerExact(nick);

                    if (player == null) {
                        String playerNotFoundMessage = config.messages.social.playerNotFound;
                        event.reply(playerNotFoundMessage)
                                .queue();

                        return;
                    }

                    if (blockedList.isEmpty()) {
                        String verifyNotRequiredMessage = config.messages.social.verifyNotRequired;
                        event.reply(verifyNotRequiredMessage)
                                .queue();

                        return;
                    }

                    if(!blockedList.isBlocked(player)) {
                        String verifyNotRequiredMessage = config.messages.social.verifyNotRequired;
                        event.reply(verifyNotRequiredMessage)
                                .queue();

                        return;
                    }

                    blockedList.remove(player);

                    for (String message : config.messages.minecraft.allowJoin) {
                        player.sendMessage(ColorParser.hex(message));
                    }

                    String allowJoinDiscordMessage = config.messages.social.allowJoin;
                    event.reply(allowJoinDiscordMessage)
                            .queue();
                }
            }
        }
    }

    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callData.equals("2fa-allow-join")) {
                for (String nick : config.telegram.users.keySet()) {
                    if (config.telegram.users.get(nick).equals(String.valueOf(chatId))) {

                        Player player = Bukkit.getPlayerExact(nick);

                        if (player == null) {
                            String playerNotFoundMessage = config.messages.social.playerNotFound;
                            telegramSendMessage(playerNotFoundMessage, chatId);

                            return;
                        }

                        if (blockedList.isEmpty()) {
                            String verifyNotRequiredMessage = config.messages.social.verifyNotRequired;
                            telegramSendMessage(verifyNotRequiredMessage, chatId);

                            return;
                        }

                        if(!blockedList.isBlocked(player)) {
                            String verifyNotRequiredMessage = config.messages.social.verifyNotRequired;
                            telegramSendMessage(verifyNotRequiredMessage, chatId);

                            return;
                        }

                        blockedList.remove(player);

                        for (String message : config.messages.minecraft.allowJoin) {
                            player.sendMessage(ColorParser.hex(message));
                        }

                        String allowJoinDiscordMessage = config.messages.social.allowJoin;
                        telegramSendMessage(allowJoinDiscordMessage, chatId);
                    }
                }
            }
        }
    }

    private void telegramSendMessage(String content, long chatId) {
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(content)
                .build();

        TelegramClient telegramClient = new OkHttpTelegramClient(plugin.config.telegram.token);

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
