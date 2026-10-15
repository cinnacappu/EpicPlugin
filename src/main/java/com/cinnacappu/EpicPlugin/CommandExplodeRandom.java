package com.cinnacappu.EpicPlugin;

import java.util.Collection;
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

        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        if (onlinePlayers.isEmpty()) {
            player.sendMessage("§2§l[EP]§r§c No online players are available.");
            return true;
        }

        int selected = ThreadLocalRandom.current().nextInt(onlinePlayers.size());
        Player target = null;
        for (Player candidate : onlinePlayers) {
            if (selected-- == 0) {
                target = candidate;
                break;
            }
        }
        if (target == null) return true;
        float power = ExplosionSettings.power(sender, 10.0F);
        if (Float.isNaN(power)) return true;
        target.getWorld().createExplosion(target.getLocation(), power, true);
        if (target.getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR) {
            target.setHealth(0.0D);
        }

        player.sendMessage("§4It has been done to " + target.getName() + ".");
        return true;
    }
}
