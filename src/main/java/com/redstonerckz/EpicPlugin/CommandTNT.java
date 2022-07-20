package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CommandTNT implements CommandExecutor {
    public CommandTNT() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayerExact(args[0]);
                target.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
            }
        }
        return true;
    }
}