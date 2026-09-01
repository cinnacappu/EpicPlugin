package com.cinnacappu.EpicPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSaturation implements CommandExecutor {
    public CommandSaturation() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int saturation = Math.round(player.getSaturation());
                player.sendMessage("§2§l[EP]§r§a Your saturation level is §r§2§l" + saturation);
        }
        return true;
    }
}