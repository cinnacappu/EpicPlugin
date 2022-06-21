package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFeed implements CommandExecutor {
    public CommandFeed() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.setFoodLevel(20);
                player.setSaturation(20.0F);
                player.sendMessage("§2§l[EP]§r§b Healed Hunger");
            } else {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (sender instanceof Player) {
                    target.sendMessage("§2§l[EP]§r§b Healed Hunger");
                    target.setFoodLevel(20);
                    target.setSaturation(20.0F);
                } else {
                    player.sendMessage("§cUsage: /feed (Online Player)");
                }
            }
        }

        return true;
    }
}