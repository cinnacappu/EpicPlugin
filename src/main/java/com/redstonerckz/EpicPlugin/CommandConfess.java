package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandConfess implements CommandExecutor {
    public CommandConfess() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage("§cUsage: /confess (Online Player) (Message)");
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            String msg = "";
            for (int i = 1; i != args.length; ++i) {
                msg = msg + args[i] + " ";
            }
            if (sender instanceof Player) {
                Server everyoneThatWillMakeFunOfThePlayerThatWillBeInTheCommand = Bukkit.getServer();
                String unfortunatePlayer = target.getDisplayName();
                everyoneThatWillMakeFunOfThePlayerThatWillBeInTheCommand.broadcastMessage("<" + unfortunatePlayer + "§r> " + msg);
            }
        }
        return true;
    }
}