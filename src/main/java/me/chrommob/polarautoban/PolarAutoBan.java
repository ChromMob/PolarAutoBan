package me.chrommob.polarautoban;

import me.chrommob.polarautoban.commands.ReloadCommand;
import me.chrommob.polarautoban.config.*;
import me.chrommob.polarautoban.webhook.Sender;
import org.bukkit.plugin.java.JavaPlugin;
import top.polar.api.loader.LoaderApi;
import top.polar.api.user.event.type.CheckType;
import top.polar.api.user.event.type.CloudCheckType;

import java.util.ArrayList;
import java.util.List;

public final class PolarAutoBan extends JavaPlugin {
    private Sender sender;
    private ConfigManager configManager;
    @Override
    public void onLoad() {
        configManager = new ConfigManager(getDataFolder());

        List<ConfigKey> keys = new ArrayList<>();
        List<ConfigKey> webhookKeys = new ArrayList<>();
        webhookKeys.add(new ConfigKey("enabled", true));
        webhookKeys.add(new ConfigKey("url", "https://discord.com/api/webhooks/1234567890/abcdefghijklmnopqrstuvwxyz"));
        keys.add(new ConfigKey("webhook", webhookKeys));

        List<ConfigKey> checkKeys = new ArrayList<>();
        for (CheckType type : CheckType.values()) {
            checkKeys.add(new ConfigKey(type.name().toLowerCase(), CheckData.getKeys()));
        }

        for (CloudCheckType type : CloudCheckType.values()) {
            checkKeys.add(new ConfigKey(type.name().toLowerCase(), CheckData.getKeys()));
        }

        keys.add(new ConfigKey("checks", checkKeys));

        PluginConfig config = new PluginConfig("config", keys);
        configManager.addConfig(config);

        try {
            Class.forName("top.polar.api.loader.LoaderApi");
        } catch (ClassNotFoundException e) {
            sender.add("Polar is not loaded!", "Server", true);
            return;
        }
        PolarAutoBanHook hook = new PolarAutoBanHook(this);
        LoaderApi.registerEnableCallback(hook::init);
    }

    @Override
    public void onEnable() {
        this.getCommand("polarautoban").setExecutor(new ReloadCommand(this));

        // Plugin startup logic
        sender = new Sender(configManager);
        sender.load();

        sender.add("PolarAutoBan has been enabled!", "Server", true);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Sender getSender() {
        return sender;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
