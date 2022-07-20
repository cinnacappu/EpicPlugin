package com.redstonerckz.EpicPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDayClear implements CommandExecutor {
    public CommandDayClear() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            player.getWorld().setClearWeatherDuration(2147483647);
            player.getWorld().setTime(0L);
        }

        return true;
    }
}