package com.redstonerckz.EpicPlugin;

import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMagic8Ball implements CommandExecutor {
    public CommandMagic8Ball() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player) {
            int randomNumber = ThreadLocalRandom.current().nextInt(0, 19 + 1);
            if (randomNumber == 1) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§a It is certain.");
            } else if (randomNumber == 2) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§a It is decidedly so.");
            } else if (randomNumber == 3) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§a Without a doubt.");
            } else if (randomNumber == 4) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§a Yes definitely.");
            } else if (randomNumber == 5) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§a You may rely on it.");
            } else if (randomNumber == 6) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§a As I see it, yes.");
            } else if (randomNumber == 7) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§a Most likely.");
            } else if (randomNumber == 8) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§a Outlook good.");
            } else if (randomNumber == 9) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§a Yes.");
            } else if (randomNumber == 10) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§a Signs point to yes.");
            } else if (randomNumber == 11) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§e Reply hazy, try again.");
            } else if (randomNumber == 12) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§e Ask again later.");
            } else if (randomNumber == 13) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§e Better not tell you now.");
            } else if (randomNumber == 14) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§e Cannot predict now.");
            } else if (randomNumber == 15) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§e Concentrate and ask again.");
            } else if (randomNumber == 16) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§c Don't count on it.");
            } else if (randomNumber == 17) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§c My reply is no.");
            } else if (randomNumber == 18) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§c My sources say no.");
            } else if (randomNumber == 19) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§c Outlook not so good.");
            } else if (randomNumber == 20) {
                player.sendMessage("§2§l[Magic 8 Ball]:§r§c Very doubtful.");
            }
        }
        return true;
    }
}