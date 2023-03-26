package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTPHere implements CommandExecutor {
    public CommandTPHere() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player target = Bukkit.getPlayerExact(args[0]);
            Player player = (Player)sender;
            Location playerLocation = player.getLocation();
            target.teleport(playerLocation);
        }

        return true;
    }
}