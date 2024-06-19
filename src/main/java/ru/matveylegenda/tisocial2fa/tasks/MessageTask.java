package ru.matveylegenda.tisocial2fa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;
import ru.matveylegenda.tisocial2fa.utils.BlockedList;
import ru.matveylegenda.tisocial2fa.utils.ColorParser;
import ru.matveylegenda.tisocial2fa.utils.Config;

public class MessageTask {
    private TiSocial2FA plugin = TiSocial2FA.getInstance();
    private BlockedList blockedList = new BlockedList();
    private Config config = plugin.config;

    public MessageTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (blockedList.isEmpty()) {
                        return;
                    }

                    if (blockedList.isBlocked(player)) {
                        for (String message : config.messages.minecraft.join) {
                            player.sendMessage(ColorParser.hex(message));
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20L);
    }
}
