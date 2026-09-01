package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public final class CommandTNT implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§2§l[EP]§r§c This command can only be used by a player.");
            return true;
        }

        if (args.length > 1) {
            player.sendMessage("§2§l[EP]§r§c Usage: /tnt [player]");
            return true;
        }

        Player target = args.length == 0 ? player : Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage("§2§l[EP]§r§c Player '" + args[0] + "' not found");
            return true;
        }

        target.getWorld().spawnEntity(target.getLocation(), EntityType.TNT);
        return true;
    }
}
