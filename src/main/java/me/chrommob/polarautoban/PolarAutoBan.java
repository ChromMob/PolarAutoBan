package me.chrommob.polarautoban;

import me.chrommob.polarautoban.action.ActionTaker;
import me.chrommob.polarautoban.config.*;
import me.chrommob.polarautoban.events.CloudEvent;
import me.chrommob.polarautoban.events.DetectionEvent;
import me.chrommob.polarautoban.webhook.Sender;
import org.bukkit.plugin.java.JavaPlugin;
import top.polar.api.PolarApi;
import top.polar.api.PolarApiAccessor;
import top.polar.api.exception.PolarNotLoadedException;
import top.polar.api.loader.LoaderApi;
import top.polar.api.user.event.CloudDetectionEvent;
import top.polar.api.user.event.DetectionAlertEvent;
import top.polar.api.user.event.MitigationEvent;
import top.polar.api.user.event.type.CheckType;
import top.polar.api.user.event.type.CloudCheckType;

import java.util.ArrayList;
import java.util.List;

public final class PolarAutoBan extends JavaPlugin {
    private Sender sender;
    @Override
    public void onLoad() {
        ConfigManager configManager = new ConfigManager(getDataFolder());

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

        sender = new Sender(configManager);
        sender.load();

        sender.add("PolarAutoBan has been enabled!", "Server", true);

        ActionTaker at = new ActionTaker(this, config);

        try {
            Class.forName("top.polar.api.loader.LoaderApi");
        } catch (ClassNotFoundException e) {
            sender.add("Polar is not loaded!", "Server", true);
            return;
        }

        LoaderApi.registerEnableCallback(() -> {
            PolarApi api;
            try {
                api = PolarApiAccessor.access().get();
            } catch (PolarNotLoadedException e) {
                api = null;
            }
            if (api == null) {
                return;
            }
            api.events().repository().registerListener(DetectionAlertEvent.class, new DetectionEvent(at));
            api.events().repository().registerListener(MitigationEvent.class,
                    new me.chrommob.polarautoban.events.MitigationEvent(at));
            api.events().repository().registerListener(CloudDetectionEvent.class, new CloudEvent(at));
        });
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Sender getSender() {
        return sender;
    }
}
