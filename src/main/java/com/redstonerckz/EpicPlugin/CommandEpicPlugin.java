package com.redstonerckz.EpicPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEpicPlugin implements CommandExecutor {
    public CommandEpicPlugin() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            player.sendMessage("§2§l[EP]§r§b EpicPlugin v3.6 currently running!");
        }

        return true;
    }
}