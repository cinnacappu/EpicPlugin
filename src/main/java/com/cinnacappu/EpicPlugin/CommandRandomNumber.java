package com.cinnacappu.EpicPlugin;

import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRandomNumber implements CommandExecutor {
    public CommandRandomNumber() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cUsage: /randomnumber (minimum) (maximum)");
            return true;
        }
        try {
            int min = Integer.parseInt(args[0]);
            int max = Integer.parseInt(args[1]);
            if (min > max) {
                sender.sendMessage("§cMinimum cannot be greater than maximum.");
                return true;
            }
            int randomNumber = (int) ThreadLocalRandom.current().nextLong(min, (long) max + 1);
            sender.sendMessage("§aNumber Generated: §2§l" + randomNumber);
        } catch (NumberFormatException ex) {
            sender.sendMessage("§cPlease enter whole numbers between -2147483648 and 2147483647.");
        }
        return true;
    }
}
