package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSaturate implements CommandExecutor {
    public CommandSaturate() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 1) {
                int saturationAmount = Integer.parseInt(args[0]);
                player.setSaturation(saturationAmount);
                player.sendMessage("§2§l[EP]§r§a Set hunger to §2§l" + saturationAmount);
            }
            if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[1]);
                int saturationAmount = Integer.parseInt(args[0]);
                if (target == null) {
                    player.sendMessage("§2§l[EP]§r§c Player '" + args[1] + "' not found");
                    return false;
                }
                if (sender instanceof Player) {
                    target.setSaturation(saturationAmount);
                    target.sendMessage("§2§l[EP]§r§a Set hunger to §2§l" + saturationAmount);
                    player.sendMessage("§2§l[EP]§r§a Set the hunger of §2§l" + target.getDisplayName() + "§r§a to §2§l" + saturationAmount);
                } else {
                    player.sendMessage("§cUsage: /hunger (amount) [player]");
                }
            }
        }
        return true;
    }
}