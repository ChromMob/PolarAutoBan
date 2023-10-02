package me.chrommob.polarautoban.action;

import me.chrommob.polarautoban.PolarAutoBan;
import me.chrommob.polarautoban.config.CheckData;
import me.chrommob.polarautoban.config.PluginConfig;
import me.chrommob.polarautoban.webhook.Sender;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import top.polar.api.user.event.type.CheckType;

import java.util.HashMap;
import java.util.Map;

public class ActionTaker {
    private final Sender sender;
    private final PluginConfig config;
    public ActionTaker(PolarAutoBan polarAutoBan, PluginConfig config) {
        sender = polarAutoBan.getSender();
        this.config = config;
    }

    private final Map<String, Map<String, Integer>> vl = new HashMap<>();
    private final Map<String, Map<String, Long>> lastAlert = new HashMap<>();

    public void takeAction(String type, String playerName) {
        int vl = getVL(type, playerName);
        CheckData checkData = config.getCheckData(type);
        System.out.println("vl: " + vl + ", punishVL: " + (checkData == null ? "null" : checkData.punishVL) + ", enabled: " + (checkData == null ? "null" : checkData.enabled));
        if (checkData == null) {
            return;
        }
        if (!checkData.enabled || vl < checkData.punishVL) {
            return;
        }
        String command = checkData.punishCommand
                .replace("%player%", playerName)
                .replace("%check%", type)
                .replace("%vl%", String.valueOf(vl));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        String alertMessage = checkData.alertMessage
                .replace("%player%", playerName)
                .replace("%check%", type)
                .replace("%vl%", String.valueOf(vl));
        Bukkit.getServer().broadcast(Component.text(alertMessage), "polarautoban.alert");
        if (config.isWebhookEnabled()) {
            String webhookMessage = checkData.webhookMessage
                    .replace("%player%", playerName)
                    .replace("%check%", type)
                    .replace("%vl%", String.valueOf(vl));
            sender.add(webhookMessage, playerName, false);
        }
    }

    private int getVL(String type, String playerName) {
        Map<String, Integer> playerVL = vl.computeIfAbsent(playerName, k -> new HashMap<>());
        int vl = playerVL.getOrDefault(type, 0);
        vl += 1;
        Map<String, Long> playerLastAlert = lastAlert.computeIfAbsent(playerName, k -> new HashMap<>());
        long lastAlert = playerLastAlert.getOrDefault(type, 0L);
        CheckData checkData = config.getCheckData(type);
        long decay = checkData.decaySeconds * 1000L;
        if (lastAlert + decay > System.currentTimeMillis()) {
            vl = 0;
        }
        playerLastAlert.put(type, System.currentTimeMillis());
        playerVL.put(type, vl);
        return vl;
    }
}
