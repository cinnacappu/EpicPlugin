package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBroadcast implements CommandExecutor {
    public CommandBroadcast() {
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            String msg = "";
            for(int i = 0; i != args.length; ++i) {
                msg = msg + args[i] + " ";
            }
            if (sender instanceof Player) {
                Bukkit.getServer().broadcastMessage("§6§l[Broadcast]§r " + msg);
            } else {
                player.sendMessage("§cUsage: /<cmd> (msg)");
            }
        }
        return true;
    }
}