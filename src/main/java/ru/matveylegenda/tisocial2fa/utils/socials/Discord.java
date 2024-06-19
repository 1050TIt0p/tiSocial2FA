package ru.matveylegenda.tisocial2fa.utils.socials;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;
import ru.matveylegenda.tisocial2fa.listeners.AllowJoin;
import ru.matveylegenda.tisocial2fa.utils.BlockedList;
import ru.matveylegenda.tisocial2fa.utils.ColorParser;
import ru.matveylegenda.tisocial2fa.utils.Config;

public class Discord {
    private TiSocial2FA plugin = TiSocial2FA.getInstance();
    private static JDA jda;
    private Config config = plugin.config;
    private BlockedList blockedList = new BlockedList();

    public void enableBot() {
        if (!config.discord.enabled) {
            return;
        }

        String token = config.discord.token;

        try {
            jda = JDABuilder.createDefault(token)
                    .enableIntents(
                            GatewayIntent.DIRECT_MESSAGES,
                            GatewayIntent.MESSAGE_CONTENT
                    )

                    .addEventListeners(
                            new AllowJoin()
                    )

                    .build();
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка: " + e);
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void checkPlayer(Player player) {
        if (!config.discord.enabled) {
            return;
        }

        String discordID = config.discord.users.get(player.getName());

        if (discordID != null) {
            blockedList.add(player);

            for (String message : config.messages.minecraft.join) {
                player.sendMessage(ColorParser.hex(message));
            }

            String buttonEmoji = config.messages.social.buttonEmoji;
            String buttonText = config.messages.social.buttonText;
            Button button = Button.danger("2fa-allow-join", buttonText)
                    .withStyle(ButtonStyle.SECONDARY)
                    .withEmoji(Emoji.fromUnicode(buttonEmoji));

            String joinDiscordMessage = config.messages.social.join;
            jda.openPrivateChannelById(discordID).queue(channel ->
                    channel.sendMessage(joinDiscordMessage)
                            .setComponents(
                                    ActionRow.of(button)
                            )
                            .queue()
            );

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
                        String kickMessage = ColorParser.hex(config.messages.minecraft.kick);
                        player.kickPlayer(kickMessage);
                    }
                }
            };
            kickTask.runTaskLater(plugin, time * 20L);
        }
    }

    public static JDA getJDA() {
        return jda;
    }
}