package ru.matveylegenda.tisocial2fa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.matveylegenda.tisocial2fa.utils.BlockedList;

public class PlayerQuit implements Listener {
    private BlockedList blockedList = new BlockedList();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (blockedList.isEmpty()) {
            return;
        }

        if (blockedList.isBlocked(player)) {
            blockedList.remove(player);
        }
    }
}
