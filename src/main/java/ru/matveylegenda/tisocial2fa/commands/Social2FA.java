package ru.matveylegenda.tisocial2fa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.matveylegenda.tisocial2fa.TiSocial2FA;
import ru.matveylegenda.tisocial2fa.utils.ColorParser;
import ru.matveylegenda.tisocial2fa.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class Social2FA implements CommandExecutor, TabCompleter {
    private TiSocial2FA plugin = TiSocial2FA.getInstance();
    private Config config = plugin.config;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {

            if (!sender.hasPermission("tisocial2fa.use")) {
                String noPermissionMessage = ColorParser.hex(config.messages.minecraft.noPermission);
                sender.sendMessage(noPermissionMessage);

                return true;
            }

            for (String message : config.messages.minecraft.usage) {
                sender.sendMessage(ColorParser.hex(message));
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("tisocial2fa.reload")) {
                String noPermissionMessage = ColorParser.hex(config.messages.minecraft.noPermission);
                sender.sendMessage(noPermissionMessage);

                return true;
            }

            config.reloadConfig();

            String reloadMessage = ColorParser.hex(config.messages.minecraft.reload);
            sender.sendMessage(reloadMessage);

            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {

            if (!sender.hasPermission("tisocial2fa.add")) {
                String noPermissionMessage = ColorParser.hex(config.messages.minecraft.noPermission);
                sender.sendMessage(noPermissionMessage);

                return true;
            }

            if (args.length < 4) {

                if (!sender.hasPermission("tisocial2fa.use")) {
                    String noPermissionMessage = ColorParser.hex(config.messages.minecraft.noPermission);
                    sender.sendMessage(noPermissionMessage);

                    return true;
                }

                for (String message : config.messages.minecraft.usage) {
                    sender.sendMessage(ColorParser.hex(message));
                }

                return true;
            }

            if (args[1].equalsIgnoreCase("discord")) {
                config.discord.users.put(args[2], args[3]);
                config.saveConfig();

                String addPlayer = ColorParser.hex(
                        config.messages.minecraft.addPlayer
                                .replace("{player}", args[2])
                );
                sender.sendMessage(addPlayer);

                return true;
            }

            if (args[1].equalsIgnoreCase("telegram")) {
                config.telegram.users.put(args[2], args[3]);
                config.saveConfig();

                String addPlayer = ColorParser.hex(
                        config.messages.minecraft.addPlayer
                                .replace("{player}", args[2])
                );
                sender.sendMessage(addPlayer);

                return true;
            }

            if (!sender.hasPermission("tisocial2fa.use")) {
                String noPermissionMessage = ColorParser.hex(config.messages.minecraft.noPermission);
                sender.sendMessage(noPermissionMessage);

                return true;
            }

            for (String message : config.messages.minecraft.usage) {
                sender.sendMessage(ColorParser.hex(message));
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {

            if (!sender.hasPermission("tisocial2fa.remove")) {
                String noPermissionMessage = ColorParser.hex(config.messages.minecraft.noPermission);
                sender.sendMessage(noPermissionMessage);

                return true;
            }

            if (args.length < 3) {

                if (!sender.hasPermission("tisocial2fa.use")) {
                    String noPermissionMessage = ColorParser.hex(config.messages.minecraft.noPermission);
                    sender.sendMessage(noPermissionMessage);

                    return true;
                }

                for (String message : config.messages.minecraft.usage) {
                    sender.sendMessage(ColorParser.hex(message));
                }

                return true;
            }

            if (args[1].equalsIgnoreCase("discord")) {

                if (!config.discord.users.keySet().contains(args[2])) {

                    String removePlayerNotFound = ColorParser.hex(
                            config.messages.minecraft.removePlayerNotFound
                                    .replace("{player}", args[2])
                    );
                    sender.sendMessage(removePlayerNotFound);

                    return true;
                }

                config.discord.users.remove(args[2]);
                config.saveConfig();

                String removePlayer = ColorParser.hex(
                        config.messages.minecraft.removePlayer
                                .replace("{player}", args[2])
                );
                sender.sendMessage(removePlayer);

                return true;
            }

            if (args[1].equalsIgnoreCase("telegram")) {

                if (!config.telegram.users.keySet().contains(args[2])) {

                    String removePlayerNotFound = ColorParser.hex(
                            config.messages.minecraft.removePlayerNotFound
                                    .replace("{player}", args[2])
                    );
                    sender.sendMessage(removePlayerNotFound);

                    return true;
                }

                config.telegram.users.remove(args[2]);
                config.saveConfig();

                String removePlayer = ColorParser.hex(
                        config.messages.minecraft.removePlayer
                                .replace("{player}", args[2])
                );
                sender.sendMessage(removePlayer);

                return true;
            }

            if (!sender.hasPermission("tisocial2fa.use")) {
                String noPermissionMessage = ColorParser.hex(config.messages.minecraft.noPermission);
                sender.sendMessage(noPermissionMessage);

                return true;
            }

            for (String message : config.messages.minecraft.usage) {
                sender.sendMessage(ColorParser.hex(message));
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission("tisocial2fa.use")) {

            return null;
        }

        if (args.length == 1) {

            return List.of(
                    "reload",
                    "add",
                    "remove"
            );
        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {

                return List.of(
                        "discord",
                        "telegram"
                );
            }

            return null;
        }

        if (args.length == 3) {

            if (args[0].equalsIgnoreCase("remove")) {

                if (args[1].equalsIgnoreCase("discord")) {

                    return new ArrayList<>(config.discord.users.keySet());
                }

                if (args[1].equalsIgnoreCase("telegram")) {

                    return new ArrayList<>(config.telegram.users.keySet());
                }

                return null;
            }

            return null;
        }

        return null;
    }
}