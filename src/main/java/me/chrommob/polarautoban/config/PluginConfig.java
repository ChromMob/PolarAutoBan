package me.chrommob.polarautoban.config;

import java.util.List;

public class PluginConfig extends ConfigWrapper {

    public PluginConfig(String name, List<ConfigKey> keys) {
        super(name, keys);
    }

    public boolean isDebug() {
        return getKey("debug").getAsBoolean();
    }

    public boolean isWebhookEnabled() {
        ConfigKey webhook = getKey("webhook");
        ConfigKey enabled = webhook.getKey("enabled");
        return enabled.getAsBoolean();
    }

    public String getWebhookUrl() {
        ConfigKey webhook = getKey("webhook");
        ConfigKey url = webhook.getKey("url");
        return url.getAsString();
    }

    public CheckData getCheckData(String check) {
        ConfigKey allChecks = getKey("checks");
        ConfigKey checkData = allChecks.getKey(check);
        if (checkData == null) {
            return null;
        }
        return new CheckData(checkData);
    }
}
