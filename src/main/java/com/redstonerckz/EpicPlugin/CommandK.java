package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandK implements CommandExecutor {
    public CommandK() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.setHealth(0.0D);
            } else {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (sender instanceof Player) {
                    target.setHealth(0.0D);
                } else {
                    player.sendMessage("§cUsage: /k (Online Player)");
                }
            }
        }
        return true;
    }
}