package ru.matveylegenda.tisocial2fa;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.matveylegenda.tisocial2fa.commands.Social2FA;
import ru.matveylegenda.tisocial2fa.listeners.BlockListener;
import ru.matveylegenda.tisocial2fa.listeners.PlayerJoin;
import ru.matveylegenda.tisocial2fa.listeners.PlayerQuit;
import ru.matveylegenda.tisocial2fa.tasks.MessageTask;
import ru.matveylegenda.tisocial2fa.utils.Config;
import ru.matveylegenda.tisocial2fa.utils.Metrics;
import ru.matveylegenda.tisocial2fa.utils.socials.Discord;
import ru.matveylegenda.tisocial2fa.utils.socials.Telegram;

import java.io.File;

public final class TiSocial2FA extends JavaPlugin {
    private static TiSocial2FA instance;
    public File configFile = new File(getDataFolder() + "/config.yml");
    public Config config = new Config();

    private final ConsoleCommandSender consoleSender = getServer().getConsoleSender();

    @Override
    public void onEnable() {
        instance = this;

        consoleSender.sendMessage("");
        consoleSender.sendMessage("§b  _   _ ____             _       _ ____  _____ _    ");
        consoleSender.sendMessage("§b | |_(_) ___|  ___   ___(_) __ _| |___ \\|  ___/ \\   ");
        consoleSender.sendMessage("§b | __| \\___ \\ / _ \\ / __| |/ _` | | __) | |_ / _ \\  ");
        consoleSender.sendMessage("§b | |_| |___) | (_) | (__| | (_| | |/ __/|  _/ ___ \\ ");
        consoleSender.sendMessage("§b  \\__|_|____/ \\___/ \\___|_|\\__,_|_|_____|_|/_/   \\_\\");
        consoleSender.sendMessage(" §fВерсия: §9" + getDescription().getVersion() + " §8| §fАвтор: §91050TI_top");
        consoleSender.sendMessage("");

        config.reloadConfig();

        getCommand("tisocial2fa").setExecutor(new Social2FA());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BlockListener(), this);
        pluginManager.registerEvents(new PlayerJoin(), this);
        pluginManager.registerEvents(new PlayerQuit(), this);

        new Discord().enableBot();
        new Telegram().enableBot();

        new Metrics(this, 22325);

        new MessageTask();
    }

    public static TiSocial2FA getInstance() {
        return instance;
    }
}