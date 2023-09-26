package me.chrommob.polarautoban;

import me.chrommob.polarautoban.events.Detection;
import org.bukkit.plugin.java.JavaPlugin;
import top.polar.api.PolarApi;
import top.polar.api.PolarApiAccessor;
import top.polar.api.exception.PolarNotLoadedException;
import top.polar.api.loader.LoaderApi;
import top.polar.api.user.event.DetectionAlertEvent;

public final class PolarAutoBan extends JavaPlugin {
    @Override
    public void onLoad() {
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
            api.events().repository().registerListener(DetectionAlertEvent.class, new Detection());
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
}
