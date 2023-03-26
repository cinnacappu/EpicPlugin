// ChatGPT wrote this lololololololololololol
package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPing implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("§2§l[EP]§r§a " + player.getName() + "'s ping is §2§l" + player.getPing() + "§r§ams");
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    player.sendMessage("§2§l[EP]§r§a " + target.getName() + "'s ping is §2§l" + target.getPing() + "§r§ams");
                } else {
                    player.sendMessage("§2§l[EP]§r§c Player not found.");
                }
            } else {
                player.sendMessage("§cUsage: /ping (Player)");
            }
            return true;
        }
        return false;
    }
}