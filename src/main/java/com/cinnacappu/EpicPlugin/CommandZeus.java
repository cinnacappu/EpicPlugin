package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CommandZeus implements CommandExecutor {
    public CommandZeus() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length < 1) {
                player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.LIGHTNING_BOLT);
                player.setHealth(0.0D);
            } else if (args.length == 1) {
                Player veryUnfortunatePlayer = Bukkit.getPlayerExact(args[0]);
                if (veryUnfortunatePlayer == null) {
                    player.sendMessage("§2§l[EP]§r§c Player '" + args[0] + "' not found");
                    return false;
                }
                veryUnfortunatePlayer.getLocation().getWorld().spawnEntity(veryUnfortunatePlayer.getLocation(), EntityType.LIGHTNING_BOLT);
                veryUnfortunatePlayer.setHealth(0.0D);
            }

            player.sendMessage("§4It has been done.");
        }
        return true;
    }
}
