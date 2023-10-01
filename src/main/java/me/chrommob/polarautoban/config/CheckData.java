package me.chrommob.polarautoban.config;

import java.util.List;

public class CheckData {
    public final boolean enabled;
    public final int punishVL;
    public final int decaySeconds;
    public final String punishCommand;
    public final String alertMessage;
    public final String webhookMessage;

    public CheckData(ConfigKey checkData) {
        ConfigKey enabled = checkData.getKey("enabled");
        ConfigKey punishVL = checkData.getKey("punish-vl");
        ConfigKey decaySeconds = checkData.getKey("decay-seconds");
        ConfigKey punishCommand = checkData.getKey("punish-command");
        ConfigKey alertMessage = checkData.getKey("alert-message");
        ConfigKey webhookMessage = checkData.getKey("webhook-message");
        this.enabled = enabled.getAsType(Boolean.class);
        this.punishVL = punishVL.getAsType(Integer.class);
        this.decaySeconds = decaySeconds.getAsInt();
        this.punishCommand = punishCommand.getAsString();
        this.alertMessage = alertMessage.getAsString();
        this.webhookMessage = webhookMessage.getAsString();
    }

    public static List<ConfigKey> getKeys() {
        return List.of(
                new ConfigKey("enabled", true),
                new ConfigKey("punish-vl", 5),
                new ConfigKey("decay-seconds", 5),
                new ConfigKey("punish-command", "ban %player% %check%"),
                new ConfigKey("alert-message", "<red>%player% has been banned for %check% with %vl% VL"),
                new ConfigKey("webhook-message", "%player% has been banned for %check% with %vl% VL")
        );
    }
}
