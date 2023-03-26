// ChatGPT also wrote this lolololololololol
package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSaveMe implements CommandExecutor {

    public CommandSaveMe() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player targetPlayer;

            if (args.length > 0) {
                targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer == null) {
                    player.sendMessage("§2§l[EP]§r§c Player '" + args[0] + "' not found");
                    return false;
                }
            } else {
                targetPlayer = player;
            }

            Location location = targetPlayer.getLocation();

            if (targetPlayer.getGameMode() == GameMode.SURVIVAL || targetPlayer.getGameMode() == GameMode.ADVENTURE) {

                int searchY = (int) Math.floor(location.getY());

                while (searchY < 384) {
                    Block blockAbove = targetPlayer.getWorld().getBlockAt(location.getBlockX(), searchY + 1, location.getBlockZ());
                    if (blockAbove.getType().isSolid()) {
                        Location teleportLocation = new Location(location.getWorld(), location.getX(), searchY + 1, location.getZ(), location.getYaw(), location.getPitch());
                        teleportLocation.add(0, targetPlayer.getHeight(), 0);
                        targetPlayer.teleport(teleportLocation);
                        player.sendMessage("§2§l[EP]§r§a Saved §2§l" + targetPlayer.getName());
                        return true;
                    } else {
                        searchY++;
                    }
                }
                player.sendMessage("§2§l[EP]§r§c No solid block found above " + targetPlayer.getName() + ", they can't be saved");
            } else {
                player.sendMessage("§2§l[EP]§r§c Player " + targetPlayer.getName() + " is in creative/spectator mode, they can save themself");
            }
            return false;
        }

        return false;
    }
}