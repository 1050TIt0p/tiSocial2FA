package ru.matveylegenda.tisocial2fa.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;
import ru.matveylegenda.tisocial2fa.utils.BlockedList;
import ru.matveylegenda.tisocial2fa.utils.Config;

import java.util.List;

public class BlockListener implements Listener {
    private TiSocial2FA plugin = TiSocial2FA.getInstance();
    private BlockedList blockedUtil = new BlockedList();
    private Config config = plugin.config;

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            if (blockedUtil.isBlocked(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (blockedUtil.isBlocked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (blockedUtil.isEmpty()) {
            return;
        }

        Player player = event.getPlayer();

        if (blockedUtil.isBlocked(player)) {
            String[] commandArgs = event.getMessage().split(" ");
            String command = commandArgs[0].toLowerCase();
            List<String> allowedCommands = config.settings.allowedCommands;
            if (!allowedCommands.contains(command)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (blockedUtil.isBlocked(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (blockedUtil.isBlocked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (blockedUtil.isBlocked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (blockedUtil.isBlocked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        if (blockedUtil.isBlocked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (blockedUtil.isBlocked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (blockedUtil.isBlocked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (blockedUtil.isEmpty()) {
            return;
        }

        Player player = event.getPlayer();

        if (blockedUtil.isBlocked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (blockedUtil.isBlocked(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (blockedUtil.isBlocked(player)) {
            event.setCancelled(true);
        }
    }
}
