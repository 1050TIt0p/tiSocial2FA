package ru.matveylegenda.tisocial2fa.api;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class SocialLogoutEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    public SocialLogoutEvent(Player player) {
        super(player);
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
