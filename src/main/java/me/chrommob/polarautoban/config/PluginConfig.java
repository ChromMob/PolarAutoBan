package me.chrommob.polarautoban.config;

import top.polar.api.user.event.type.CheckType;

import java.util.List;

public class PluginConfig extends ConfigWrapper {

    public PluginConfig(String name, List<ConfigKey> keys) {
        super(name, keys);
    }

    public boolean isWebhookEnabled() {
        ConfigKey webhook = getKey("webhook");
        ConfigKey enabled = webhook.getKey("enabled");
        return enabled.getAsType(Boolean.class);
    }

    public String getWebhookUrl() {
        ConfigKey webhook = getKey("webhook");
        ConfigKey url = webhook.getKey("url");
        return url.getAsString();
    }

    public CheckData getCheckData(String check) {
        ConfigKey checkData = getKey(check);
        if (checkData == null) {
            return null;
        }
        return new CheckData(checkData);
    }
}
