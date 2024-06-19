package ru.matveylegenda.tisocial2fa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;
import ru.matveylegenda.tisocial2fa.utils.ColorParser;
import ru.matveylegenda.tisocial2fa.utils.Config;

public class Social2FA implements CommandExecutor {
    private TiSocial2FA plugin = TiSocial2FA.getInstance();
    private Config config = plugin.config;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Config config = plugin.config;

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("tisocial2fa.reload")) {
                config.reloadConfig();

                String reloadMessage = ColorParser.hex(config.messages.minecraft.reload);
                sender.sendMessage(reloadMessage);

                return true;
            } else {
                String noPermissionMessage = ColorParser.hex(config.messages.minecraft.noPermission);
                sender.sendMessage(noPermissionMessage);

                return true;
            }
        } else {
            if (sender.hasPermission("tidiscord2fa.use")) {
                for (String message : config.messages.minecraft.usage) {
                    sender.sendMessage(ColorParser.hex(message));
                }

                return true;
            } else {
                String noPermissionMessage = ColorParser.hex(config.messages.minecraft.noPermission);
                sender.sendMessage(noPermissionMessage);

                return true;
            }
        }
    }
}
