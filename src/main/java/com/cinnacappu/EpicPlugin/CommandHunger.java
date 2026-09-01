package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHunger implements CommandExecutor {
    public CommandHunger() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 1) {
                int hungerAmount = Integer.parseInt(args[0]);
                player.setFoodLevel(hungerAmount);
                player.sendMessage("§2§l[EP]§r§a Set hunger to §2§l" + hungerAmount);
            }
            if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[1]);
                int hungerAmount = Integer.parseInt(args[0]);
                if (target == null) {
                    player.sendMessage("§2§l[EP]§r§c Player '" + args[1] + "' not found");
                    return false;
                }
                if (sender instanceof Player) {
                    target.setFoodLevel(hungerAmount);
                    target.sendMessage("§2§l[EP]§r§a Set hunger to §2§l" + hungerAmount);
                    player.sendMessage("§2§l[EP]§r§a Set the hunger of §2§l" + target.getDisplayName() + "§r§a to §2§l" + hungerAmount);
                } else {
                    player.sendMessage("§cUsage: /hunger (amount) [player]");
                }
            }
        }
        return true;
    }
}