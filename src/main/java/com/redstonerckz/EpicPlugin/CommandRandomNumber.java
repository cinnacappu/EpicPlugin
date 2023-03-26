package com.redstonerckz.EpicPlugin;

import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRandomNumber implements CommandExecutor {
    public CommandRandomNumber() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String minimum = args[0];
        String maximum = args[1];
        int min = Integer.parseInt(minimum);
        int max = Integer.parseInt(maximum);
        int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);
        if (sender instanceof Player) {
            player.sendMessage("§aNumber Generated: §2§l" + randomNumber);
        } else if (minimum.contains("0")) {
            int randomNumberMinZero = randomNumber + 1;
            player.sendMessage("§aNumber Generated: §2§l" + randomNumberMinZero);
        } else if (args.length < 1) {
            player.sendMessage("§cUsage: /randomnumber (minimum) (maximum)");
        }
        return true;
    }
}