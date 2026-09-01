package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandJoinSpoof implements CommandExecutor {
    public CommandJoinSpoof() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            String target = args[0];
            Bukkit.getServer().broadcastMessage("§e" + target + " has joined the game");
        }
        return true;
    }
}