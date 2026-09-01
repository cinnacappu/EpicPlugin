package com.cinnacappu.EpicPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public final class CommandTNTRandom implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§2§l[EP]§r§c This command can only be used by a player.");
            return true;
        }

        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (onlinePlayers.isEmpty()) {
            player.sendMessage("§2§l[EP]§r§c No online players are available.");
            return true;
        }

        Player target = onlinePlayers.get(ThreadLocalRandom.current().nextInt(onlinePlayers.size()));
        target.getWorld().spawnEntity(target.getLocation(), EntityType.TNT);
        player.sendMessage("§4TNT spawned at " + target.getName() + ".");
        return true;
    }
}
