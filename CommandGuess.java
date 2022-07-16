// ((([[[{{{<<<CONSOLE ONLY, NOT WORKING!!!>>>}}}]]])))
package com.redstonerckz.EpicPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.server.ServerCommandEvent;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class CommandGuess implements CommandExecutor {
    public CommandGuess() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            int randomNumber = (int) (Math.random() * 10 + 1);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Guess the number from 1 - 10");
            String ServerCommandEvent;
            int guess = 1; // parseInt (input from console);
            if (guess == randomNumber) {
                System.out.println("You did it! It is indeed " + randomNumber + " :D");
            } else {
                System.out.println("You got it wrong, it was " + randomNumber + " :(");
            }
        }
        return true;
    }
}