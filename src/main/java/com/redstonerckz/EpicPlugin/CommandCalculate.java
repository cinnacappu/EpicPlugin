package com.redstonerckz.EpicPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCalculate implements CommandExecutor {
    public CommandCalculate() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        String firstInt = args[0];
        String operator = args[1];
        String secondInt = args[2];
        if (sender instanceof Player) {
            double a = (double)Integer.parseInt(firstInt);
            double b = (double)Integer.parseInt(secondInt);
            double sum;
            if (operator.contains("+")) {
                sum = a + b;
                player.sendMessage("§aAnswer = §2§l" + sum);
            } else if (operator.contains("-")) {
                sum = a - b;
                player.sendMessage("§aAnswer = §2§l" + sum);
            } else if (operator.contains("x")) {
                sum = a * b;
                player.sendMessage("§aAnswer = §2§l" + sum);
            } else if (operator.contains("*")) {
                sum = a * b;
                player.sendMessage("§aAnswer = §2§l" + sum);
            } else if (operator.contains("/")) {
                sum = a / b;
                player.sendMessage("§aAnswer = §2§l" + sum);
            }
        } else if (args.length < 0) {
            player.sendMessage("§cUsage: /calculate (First Number) (Operator) (Second Number)");
        }

        return true;
    }
}