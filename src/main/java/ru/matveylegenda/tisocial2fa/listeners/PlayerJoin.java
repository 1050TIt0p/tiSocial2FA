package ru.matveylegenda.tisocial2fa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.matveylegenda.tisocial2fa.utils.socials.Discord;
import ru.matveylegenda.tisocial2fa.utils.socials.Telegram;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new Discord().checkPlayer(player);
        new Telegram().checkPlayer(player);
    }
}
