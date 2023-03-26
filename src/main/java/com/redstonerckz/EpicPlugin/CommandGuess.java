package com.redstonerckz.EpicPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CommandGuess implements CommandExecutor {
    private boolean isWaitingForGuess = false;
    private int randomNumber;

    public CommandGuess() {}

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (!isWaitingForGuess) {
                randomNumber = (int) (Math.random() * 10 + 1);
                sender.sendMessage("Guess the number from 1 - 10 (With 'guess [number]')");
                isWaitingForGuess = true;
            } else {
                try {
                    int guess = Integer.parseInt(args[0]);
                    if (guess == randomNumber) {
                        sender.sendMessage("You did it! It is indeed " + randomNumber + " :D");
                    } else {
                        sender.sendMessage("You got it wrong, it was " + randomNumber + " :(");
                    }
                    isWaitingForGuess = false;
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    sender.sendMessage("Invalid number! Please enter a number between 1 and 10!");
                }
            }
        } else {
            sender.sendMessage("§2§l[EP]§r§c This command is only for the console!");
        }
        return true;
    }
}