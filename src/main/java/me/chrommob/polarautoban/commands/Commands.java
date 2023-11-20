package me.chrommob.polarautoban.commands;

import me.chrommob.polarautoban.PolarAutoBan;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {
    private final PolarAutoBan polarAutoBan;
    public Commands(PolarAutoBan polarAutoBan) {
        this.polarAutoBan = polarAutoBan;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("polarautoban")) {
            return false;
        }
        if (args.length != 1 && args.length != 2) {
            sender.sendMessage("§cUsage: /polarautoban reload");
            sender.sendMessage("§cUsage: /polarautoban test <checkType>");
            return false;
        }
        if (!args[0].equalsIgnoreCase("reload") && !args[0].equalsIgnoreCase("test")) {
            sender.sendMessage("§cUsage: /polarautoban reload");
            sender.sendMessage("§cUsage: /polarautoban test <checkType>");
            return false;
        }
        if (!sender.hasPermission("polarautoban.reload") && !sender.hasPermission("polarautoban.test")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return false;
        }

        if (args.length == 1) {
            sender.sendMessage("§aReloading config...");
            polarAutoBan.getConfigManager().reloadConfig("config");
            polarAutoBan.getSender().load();

            sender.sendMessage("§aConfig reloaded!");
            return true;
        }
        polarAutoBan.getAc().takeAction(args[1], sender.getName());
        return true;
    }
}
