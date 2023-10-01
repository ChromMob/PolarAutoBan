package me.chrommob.polarautoban.commands;

import me.chrommob.polarautoban.PolarAutoBan;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    private final PolarAutoBan polarAutoBan;
    public ReloadCommand(PolarAutoBan polarAutoBan) {
        this.polarAutoBan = polarAutoBan;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("polarautoban")) {
            return false;
        }
        if (args.length != 1) {
            sender.sendMessage("§cUsage: /polarautoban reload");
            return false;
        }
        if (!args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage("§cUsage: /polarautoban reload");
            return false;
        }
        if (!sender.hasPermission("polarautoban.reload")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return false;
        }

        polarAutoBan.getSender().add("Reloading config...", "Server", true);
        sender.sendMessage("§aReloading config...");
        polarAutoBan.getConfigManager().reloadConfig("config");

        sender.sendMessage("§aConfig reloaded!");
        return true;
    }
}
