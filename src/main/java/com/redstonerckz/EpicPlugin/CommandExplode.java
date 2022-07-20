package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExplode implements CommandExecutor {
    public CommandExplode() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player personWithTooMuchPower = (Player)sender;
            if (args.length < 1) {
                personWithTooMuchPower.getLocation().getWorld().createExplosion(personWithTooMuchPower.getLocation(), 10.0F, true);
                if (personWithTooMuchPower.getGameMode() == GameMode.CREATIVE) {
                    personWithTooMuchPower.setHealth(0.0D);
                }

                if (personWithTooMuchPower.getGameMode() == GameMode.SPECTATOR) {
                    personWithTooMuchPower.setHealth(0.0D);
                }
            } else if (args.length == 1) {
                Player veryUnfortunatePlayer = Bukkit.getPlayerExact(args[0]);
                veryUnfortunatePlayer.getLocation().getWorld().createExplosion(veryUnfortunatePlayer.getLocation(), 10.0F, true);
                if (veryUnfortunatePlayer.getGameMode() == GameMode.CREATIVE) {
                    veryUnfortunatePlayer.setHealth(0.0D);
                }

                if (veryUnfortunatePlayer.getGameMode() == GameMode.SPECTATOR) {
                    veryUnfortunatePlayer.setHealth(0.0D);
                }
            }

            personWithTooMuchPower.sendMessage("§4It has been done.");
        }

        return true;
    }
}