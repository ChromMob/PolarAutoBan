package me.chrommob.polarautoban.config;

import java.util.List;

public class CheckData {
    public final boolean enabled;
    public final int punishVL;
    public final int decaySeconds;
    public final String punishCommand;
    public final String decayMessage;
    public final String webhookMessage;

    public CheckData(ConfigKey checkData) {
        ConfigKey enabled = checkData.getKey("enabled");
        ConfigKey punishVL = checkData.getKey("punish-vl");
        ConfigKey decaySeconds = checkData.getKey("decay-seconds");
        ConfigKey punishCommand = checkData.getKey("punish-command");
        ConfigKey decayMessage = checkData.getKey("decay-message");
        ConfigKey webhookMessage = checkData.getKey("webhook-message");
        this.enabled = enabled.getAsType(Boolean.class);
        this.punishVL = punishVL.getAsType(Integer.class);
        this.decaySeconds = decaySeconds.getAsInt();
        this.punishCommand = punishCommand.getAsString();
        this.decayMessage = decayMessage.getAsString();
        this.webhookMessage = webhookMessage.getAsString();
    }

    public static List<ConfigKey> getKeys() {
        return List.of(
                new ConfigKey("enabled", true),
                new ConfigKey("punish-vl", 5),
                new ConfigKey("decay-seconds", 5),
                new ConfigKey("punish-command", "ban %player% %check%"),
                new ConfigKey("decay-message", "VL for %player% on %check% has been reset to 0"),
                new ConfigKey("webhook-message", "%player% has been banned for %check% with %vl% VL")
        );
    }
}
