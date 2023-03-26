package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDamage implements CommandExecutor {
    public CommandDamage() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            double damage = (double) Integer.parseInt(args[0]);
            if (args.length == 1) {
                player.damage(damage);
                player.sendMessage("§2§l[EP]§r§b Damaged HP");
                if (args.length == 2) {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (sender instanceof Player) {
                        target.sendMessage("§2§l[EP]§r§b Damaged HP");
                        target.damage(damage);
                    } else {
                        player.sendMessage("§cUsage: /damage (amount) (player)");
                    }
                }
            }
        }
        return true;
    }
}