package com.cinnacappu.EpicPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWeatherClear implements CommandExecutor {
    public CommandWeatherClear() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            player.getWorld().setClearWeatherDuration(2147483647);
        }

        return true;
    }
}