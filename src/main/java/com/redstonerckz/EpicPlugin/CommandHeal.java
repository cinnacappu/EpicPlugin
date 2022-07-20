package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHeal implements CommandExecutor {
    public CommandHeal() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.setHealth(20.0D);
                player.sendMessage("§2§l[EP]§r§b Healed HP and Hunger");
                player.setFireTicks(0);
                player.setFoodLevel(20);
                player.setSaturation(20.0F);
            } else {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (sender instanceof Player) {
                    target.sendMessage("§2§l[EP]§r§b Healed HP and Hunger");
                    target.setHealth(20.0D);
                    target.setFireTicks(0);
                    target.setFoodLevel(20);
                    target.setSaturation(20.0F);
                } else {
                    player.sendMessage("§cUsage: /heal (Online Player)");
                }
            }
        }

        return true;
    }
}