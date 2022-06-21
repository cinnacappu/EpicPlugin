package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSuperuserDo implements CommandExecutor {
    public CommandSuperuserDo() {
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player target = Bukkit.getPlayerExact(args[0]);
            String cmd = "";
            for(int i = 1; i != args.length; ++i) {
                cmd = cmd + args[i] + " ";
            }
            target.performCommand(cmd);
            player.sendMessage("§2§l[EP]§r§b Executed command ' " + cmd + "' as " + target.getDisplayName());
        }
        return true;
    }
}