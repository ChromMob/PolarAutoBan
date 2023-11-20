package me.chrommob.polarautoban.action;

import me.chrommob.polarautoban.PolarAutoBan;
import me.chrommob.polarautoban.config.CheckData;
import me.chrommob.polarautoban.config.PluginConfig;
import me.chrommob.polarautoban.webhook.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import top.polar.api.PolarApi;
import top.polar.api.user.event.type.CheckType;

import java.util.HashMap;
import java.util.Map;

public class ActionTaker {
    private Sender sender;
    private final PolarAutoBan polarAutoBan;
    private final PluginConfig config;
    public ActionTaker(PolarAutoBan polarAutoBan, PluginConfig config) {
        this.polarAutoBan = polarAutoBan;
        sender = polarAutoBan.getSender();
        this.config = config;
    }

    private final Map<String, Map<String, Integer>> vl = new HashMap<>();
    private final Map<String, Map<String, Long>> lastAlert = new HashMap<>();

    public void takeAction(String type, String playerName) {
        CheckData checkData = config.getCheckData(type);
        if (checkData == null) {
            return;
        }
        int vl = getVL(type, playerName);
        if (config.isDebug()) {
            Bukkit.getLogger().info("Player " + playerName + " has " + vl + " VL in " + type);
        }
        if (!checkData.enabled || vl < checkData.punishVL) {
            return;
        }
        setVL(type, playerName, 0);
        String command = checkData.punishCommand
                .replace("%player%", playerName)
                .replace("%check%", type)
                .replace("%vl%", String.valueOf(vl));
        Bukkit.getScheduler().runTask(PolarAutoBan.getPlugin(PolarAutoBan.class), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
        if (config.isWebhookEnabled()) {
            if (sender == null) {
                sender = polarAutoBan.getSender();
            }
            String webhookMessage = checkData.webhookMessage
                    .replace("%player%", playerName)
                    .replace("%check%", type)
                    .replace("%vl%", String.valueOf(vl));
            sender.add(webhookMessage, playerName, false);
        }
    }

    private void setVL(String type, String playerName, int i) {
        Map<String, Integer> playerVL = vl.computeIfAbsent(playerName, k -> new HashMap<>());
        playerVL.put(type, i);
    }

    private int getVL(String type, String playerName) {
        Map<String, Integer> playerVL = vl.computeIfAbsent(playerName, k -> new HashMap<>());
        int vl = playerVL.getOrDefault(type, 0);
        vl += 1;
        Map<String, Long> playerLastAlert = lastAlert.computeIfAbsent(playerName, k -> new HashMap<>());
        long lastAlert = playerLastAlert.getOrDefault(type, System.currentTimeMillis());
        CheckData checkData = config.getCheckData(type);
        long decay = checkData.decaySeconds * 1000L;
        if (lastAlert + decay < System.currentTimeMillis()) {
            vl = 0;
            String alertMessage = checkData.decayMessage
                    .replace("%player%", playerName)
                    .replace("%check%", type);
            Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserializeOr(alertMessage, Component.text(alertMessage)), "polarautoban.alert");
        }
        playerLastAlert.put(type, System.currentTimeMillis());
        playerVL.put(type, vl);
        return vl;
    }
}
