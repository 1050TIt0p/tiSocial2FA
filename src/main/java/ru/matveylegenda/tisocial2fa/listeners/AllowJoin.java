package ru.matveylegenda.tisocial2fa.listeners;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;
import ru.matveylegenda.tisocial2fa.api.SocialAllowJoinEvent;
import ru.matveylegenda.tisocial2fa.api.SocialDenyJoinEvent;
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
                        event.editMessage(playerNotFoundMessage)
                                .setComponents()
                                .queue();

                        return;
                    }

                    if (blockedList.isEmpty()) {
                        String verifyNotRequiredMessage = config.messages.social.verifyNotRequired;
                        event.editMessage(verifyNotRequiredMessage)
                                .setComponents()
                                .queue();

                        return;
                    }

                    if(!blockedList.isBlocked(player)) {
                        String verifyNotRequiredMessage = config.messages.social.verifyNotRequired;
                        event.editMessage(verifyNotRequiredMessage)
                                .setComponents()
                                .queue();

                        return;
                    }

                    blockedList.remove(player);

                    for (String allowJoin : config.messages.minecraft.allowJoin) {
                        player.sendMessage(ColorParser.hex(allowJoin));
                    }

                    String allowJoinSocialMessage = config.messages.social.allowJoin;
                    event.editMessage(allowJoinSocialMessage)
                            .setComponents()
                            .queue();

                    SocialAllowJoinEvent socialAllowJoinEvent = new SocialAllowJoinEvent(player);
                    Bukkit.getServer().getPluginManager().callEvent(socialAllowJoinEvent);
                }
            }
        }

        if (event.getComponentId().equals("2fa-deny-join")) {
            for (String nick : config.discord.users.keySet()) {
                if (config.discord.users.get(nick).equals(discordID)) {

                    if (event.isAcknowledged()) {
                        return;
                    }

                    Player player = Bukkit.getPlayerExact(nick);

                    if (player == null) {
                        String playerNotFoundMessage = config.messages.social.playerNotFound;
                        event.editMessage(playerNotFoundMessage)
                                .setComponents()
                                .queue();

                        return;
                    }

                    String command = config.settings.punishCommand
                            .replace("{player}", player.getName());

                    Bukkit.getScheduler().runTask(plugin, () ->
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command)
                    );

                    String denyJoinSocialMessage = config.messages.social.denyJoin;
                    event.editMessage(denyJoinSocialMessage)
                            .setComponents()
                            .queue();

                    SocialDenyJoinEvent socialDenyJoinEvent = new SocialDenyJoinEvent(player);
                    Bukkit.getServer().getPluginManager().callEvent(socialDenyJoinEvent);
                }
            }
        }
    }

    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();

            if (callData.equals("2fa-allow-join")) {
                for (String nick : config.telegram.users.keySet()) {
                    if (config.telegram.users.get(nick).equals(String.valueOf(chatId))) {

                        Player player = Bukkit.getPlayerExact(nick);

                        if (player == null) {
                            String playerNotFoundMessage = config.messages.social.playerNotFound;
                            telegramEditMessage(playerNotFoundMessage, chatId, messageId);

                            return;
                        }

                        if (blockedList.isEmpty()) {
                            String verifyNotRequiredMessage = config.messages.social.verifyNotRequired;
                            telegramEditMessage(verifyNotRequiredMessage, chatId, messageId);

                            return;
                        }

                        if(!blockedList.isBlocked(player)) {
                            String verifyNotRequiredMessage = config.messages.social.verifyNotRequired;
                            telegramEditMessage(verifyNotRequiredMessage, chatId, messageId);

                            return;
                        }

                        blockedList.remove(player);

                        for (String message : config.messages.minecraft.allowJoin) {
                            player.sendMessage(ColorParser.hex(message));
                        }

                        String allowJoinSocialMessage = config.messages.social.allowJoin;
                        telegramEditMessage(allowJoinSocialMessage, chatId, messageId);

                        SocialAllowJoinEvent socialAllowJoinEvent = new SocialAllowJoinEvent(player);
                        Bukkit.getServer().getPluginManager().callEvent(socialAllowJoinEvent);
                    }
                }
            }

            if (callData.equals("2fa-deny-join")) {
                for (String nick : config.telegram.users.keySet()) {
                    if (config.telegram.users.get(nick).equals(String.valueOf(chatId))) {

                        Player player = Bukkit.getPlayerExact(nick);

                        if (player == null) {
                            String playerNotFoundMessage = config.messages.social.playerNotFound;
                            telegramEditMessage(playerNotFoundMessage, chatId, messageId);

                            return;
                        }

                        String command = config.settings.punishCommand
                                .replace("{player}", player.getName());

                        Bukkit.getScheduler().runTask(plugin, () ->
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command)
                        );

                        String denyJoinSocialMessage = config.messages.social.denyJoin;
                        telegramEditMessage(denyJoinSocialMessage, chatId, messageId);

                        SocialDenyJoinEvent socialDenyJoinEvent = new SocialDenyJoinEvent(player);
                        Bukkit.getServer().getPluginManager().callEvent(socialDenyJoinEvent);
                    }
                }
            }
        }
    }

    private void telegramEditMessage(String content, long chatId, long messageId) {
        EditMessageText message = EditMessageText
                .builder()
                .chatId(chatId)
                .messageId(Math.toIntExact(messageId))
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
