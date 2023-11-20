package me.chrommob.polarautoban;

import me.chrommob.polarautoban.action.ActionTaker;
import me.chrommob.polarautoban.config.PluginConfig;
import me.chrommob.polarautoban.events.CloudEvent;
import me.chrommob.polarautoban.events.DetectionEvent;
import top.polar.api.PolarApi;
import top.polar.api.PolarApiAccessor;
import top.polar.api.exception.PolarNotLoadedException;
import top.polar.api.user.event.CloudDetectionEvent;
import top.polar.api.user.event.DetectionAlertEvent;
import top.polar.api.user.event.MitigationEvent;

public class PolarAutoBanHook {
    private final ActionTaker at;
    public PolarAutoBanHook(PolarAutoBan plugin) {
        PluginConfig config = plugin.getConfigManager().getConfigWrapper("config");
        at = new ActionTaker(plugin, config);
        plugin.setAc(at);
    }

    public void init() {
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
        api.events().repository().registerListener(CloudDetectionEvent.class, new CloudEvent(at));
    }
}
