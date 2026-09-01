package com.cinnacappu.EpicPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CommandExplodeRandom implements CommandExecutor {

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
        target.getWorld().createExplosion(target.getLocation(), 10.0F, true);
        if (target.getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR) {
            target.setHealth(0.0D);
        }

        player.sendMessage("§4It has been done to " + target.getName() + ".");
        return true;
    }
}
