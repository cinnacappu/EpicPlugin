package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandIAmTryingToBeNiceButItsHard implements CommandExecutor {
    public CommandIAmTryingToBeNiceButItsHard() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 0) {
                player.sendMessage("§cUsage: /iamtryingtobenicebutitshard (Message)");
            }
            Server everyone = Bukkit.getServer();
            String msg = "";
            for (int i = 0; i != args.length; ++i) {
                msg = msg + args[i] + " ";
            }
            char[] charArr = msg.toCharArray();
            StringBuilder sb = new StringBuilder();
            boolean switchCase = false;
            for (char ch : charArr) {
                if (Character.isLetter(ch)) {
                    if (switchCase) {
                        sb.append(Character.toUpperCase(ch));
                    } else {
                        sb.append(Character.toLowerCase(ch));
                    }
                    switchCase = !switchCase;
                } else {
                    sb.append(ch);
                }
            }
            everyone.broadcastMessage("<" + player.getDisplayName() + "> " + sb);
        }
        return true;
    }
}