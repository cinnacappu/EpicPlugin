package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CommandInHand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§2§l[EP]§r§c This command can only be used by a player.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§2§l[EP]§r§c Usage: /inhand <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage("§2§l[EP]§r§c Player '" + args[0] + "' not found");
            return true;
        }

        String item = target.getInventory().getItemInMainHand().getType().name();
        player.sendMessage("§2§l" + target.getDisplayName() + "§r§a has §2§l" + item + "§r§a in their main hand");
        return true;
    }
}
