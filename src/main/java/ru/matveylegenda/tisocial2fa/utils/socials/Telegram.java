package ru.matveylegenda.tisocial2fa.utils.socials;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;
import ru.matveylegenda.tisocial2fa.api.SocialCaptureEvent;
import ru.matveylegenda.tisocial2fa.commands.telegram.StartCommand;
import ru.matveylegenda.tisocial2fa.listeners.AllowJoin;
import ru.matveylegenda.tisocial2fa.utils.BlockedList;
import ru.matveylegenda.tisocial2fa.utils.ColorParser;
import ru.matveylegenda.tisocial2fa.utils.Config;

public class Telegram implements LongPollingSingleThreadUpdateConsumer {
    private TiSocial2FA plugin = TiSocial2FA.getInstance();
    private Config config = plugin.config;
    private BlockedList blockedList = new BlockedList();

    public void enableBot() {
        if (!config.telegram.enabled) {
            return;
        }

        String token = config.telegram.token;

        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();

            botsApplication.registerBot(token, this);
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка: " + e);
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void checkPlayer(Player player) {
        if (!config.telegram.enabled) {
            return;
        }

        String chatID = config.telegram.users.get(player.getName());

        if (chatID != null) {
            blockedList.add(player);

            for (String message : config.messages.minecraft.join) {
                player.sendMessage(ColorParser.hex(message));
            }

            String allowButtonEmoji = config.messages.social.buttons.allow.emoji;
            String allowButtonText = config.messages.social.buttons.allow.text;
            InlineKeyboardButton allowButton = InlineKeyboardButton
                    .builder()
                    .text(allowButtonEmoji + " " +  allowButtonText)
                    .callbackData("2fa-allow-join")
                    .build();

            String denyButtonEmoji = config.messages.social.buttons.deny.emoji;
            String denyButtonText = config.messages.social.buttons.deny.text;
            InlineKeyboardButton denyButton = InlineKeyboardButton
                    .builder()
                    .text(denyButtonEmoji + " " +  denyButtonText)
                    .callbackData("2fa-deny-join")
                    .build();

            String joinMessage = config.messages.social.join;

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatID)
                    .text(joinMessage)
                    .replyMarkup(InlineKeyboardMarkup
                            .builder()
                            .keyboardRow(
                                    new InlineKeyboardRow(
                                            allowButton,
                                            denyButton
                                    )
                            )
                            .build())
                    .build();

            TelegramClient telegramClient = new OkHttpTelegramClient(plugin.config.telegram.token);

            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            int time = config.settings.time;

            if (config.settings.bossbar.enabled) {
                String barTitle = ColorParser.hex(
                        config.settings.bossbar.title
                                .replace("{time}", String.valueOf(time))
                );
                BarColor barColor = BarColor.valueOf(config.settings.bossbar.color);
                BarStyle barStyle = BarStyle.valueOf(config.settings.bossbar.style);

                BossBar bossBar = Bukkit.createBossBar(barTitle, barColor, barStyle);
                bossBar.addPlayer(player);

                BukkitRunnable bossBarTask = new BukkitRunnable() {
                    int remainingTime = time;

                    @Override
                    public void run() {
                        if(remainingTime <= 0 || !player.isOnline() || !blockedList.isBlocked(player)) {
                            bossBar.removePlayer(player);
                            cancel();
                            return;
                        }

                        remainingTime--;

                        double progress = (double) remainingTime / time;
                        bossBar.setProgress(progress);

                        String barTitle = ColorParser.hex(
                                config.settings.bossbar.title
                                        .replace("{time}", String.valueOf(remainingTime))
                        );
                        bossBar.setTitle(barTitle);
                    }
                };
                bossBarTask.runTaskTimer(plugin, 0L, 20L);
            }

            BukkitRunnable kickTask = new BukkitRunnable() {

                @Override
                public void run() {
                    if (!player.isOnline()) {
                        return;
                    }

                    if (blockedList.isBlocked(player)) {
                        String command = config.settings.timeoutCommand
                                .replace("{player}", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                }
            };
            kickTask.runTaskLater(plugin, time * 20L);

            SocialCaptureEvent socialCaptureEvent = new SocialCaptureEvent(player);
            Bukkit.getServer().getPluginManager().callEvent(socialCaptureEvent);
        }
    }

    @Override
    public void consume(Update update) {
        new StartCommand().consume(update);
        new AllowJoin().consume(update);
    }
}
