package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBed implements CommandExecutor {
    public CommandBed() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 0) {
                player.sendMessage("§2§l[EP]§r§c You need to specify someone");
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            String x = String.valueOf(target.getBedSpawnLocation().getBlockX());
            String y = String.valueOf(target.getBedSpawnLocation().getBlockY());
            String z = String.valueOf(target.getBedSpawnLocation().getBlockZ());
            player.sendMessage("§aThe location of " + target.getDisplayName() + "'s bed is at: §2§l" + x + " " + y + " " + z);
        }
        return true;
    }
}