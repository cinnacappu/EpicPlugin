package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMisclick implements CommandExecutor {
    public CommandMisclick() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.swingMainHand();
        } else if (args.length == 1) {
            Player player = (Player) sender;
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.sendMessage("§2§l[EP]§r§c Player '" + args[0] + "' not found");
                return false;
            }
            target.swingMainHand();
        }
        return true;
    }
}