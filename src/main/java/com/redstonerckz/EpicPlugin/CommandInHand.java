package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInHand implements CommandExecutor {
    public CommandInHand() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player target = (Player) Bukkit.getPlayerExact(args[0]);
            String item = target.getItemInHand().getData().getItemType().name();
            player.sendMessage("§2§l" + target.getDisplayName() + "§r§a has§2§l " + item + "§r§a in their hand");
        }
        return true;
    }
}