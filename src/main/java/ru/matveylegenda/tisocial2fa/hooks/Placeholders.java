package ru.matveylegenda.tisocial2fa.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;
import ru.matveylegenda.tisocial2fa.utils.BlockedList;

public class Placeholders extends PlaceholderExpansion {
    private BlockedList blockedList = new BlockedList();

    private TiSocial2FA plugin; //

    @Override
    @NotNull
    public String getAuthor() {
        return "1050TI_top";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "tisocial2fa";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("blocked")) {
            return String.valueOf(blockedList.isBlocked(player));
        }

        return null;
    }
}
