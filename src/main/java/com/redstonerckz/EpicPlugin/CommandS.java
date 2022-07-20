package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandS implements CommandExecutor {
    public CommandS() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length == 0) {
            target = (Player)sender;
            target.setGameMode(GameMode.SURVIVAL);
            target.sendMessage("Set own game mode to Survival Mode");
        } else if (args.length == 1) {
            target = Bukkit.getPlayerExact(args[0]);
            Player player = (Player)sender;
            target.sendMessage("Your game mode was changed to Survival Mode by " + player.getDisplayName());
            target.setGameMode(GameMode.SURVIVAL);
        }

        return true;
    }
}