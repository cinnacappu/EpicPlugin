package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CommandBed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§2§l[EP]§r§c This command can only be used by a player.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§2§l[EP]§r§c Usage: /bed <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage("§2§l[EP]§r§c Player '" + args[0] + "' not found");
            return true;
        }

        Location bedLocation = target.getRespawnLocation();
        if (bedLocation == null) {
            player.sendMessage("§2§l[EP]§r§c " + target.getName() + " does not have a bed spawn set.");
            return true;
        }

        player.sendMessage("§aThe location of " + target.getDisplayName() + "'s bed is at: §2§l"
                + bedLocation.getBlockX() + " " + bedLocation.getBlockY() + " " + bedLocation.getBlockZ());
        return true;
    }
}
