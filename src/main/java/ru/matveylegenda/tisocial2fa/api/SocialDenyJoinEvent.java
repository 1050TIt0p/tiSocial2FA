package ru.matveylegenda.tisocial2fa.api;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class SocialDenyJoinEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    public SocialDenyJoinEvent(Player player) {
        super(player);
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
