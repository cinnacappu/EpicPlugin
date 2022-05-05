package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandA implements CommandExecutor {
    public CommandA() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length == 0) {
            target = (Player)sender;
            target.setGameMode(GameMode.ADVENTURE);
            target.sendMessage("Set own game mode to Adventure Mode");
        } else if (args.length == 1) {
            target = Bukkit.getPlayerExact(args[0]);
            Player player = (Player)sender;
            target.sendMessage("Your game mode was changed to Adventure Mode by " + player.getDisplayName());
            target.setGameMode(GameMode.ADVENTURE);
        }

        return true;
    }
}