package com.cinnacappu.EpicPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class CommandLastDeath implements CommandExecutor, Listener {

    private static final String PREFIX = "§2§l[EP]§r";
    private final Map<UUID, Location> lastDeathLocations = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        lastDeathLocations.put(player.getUniqueId(), player.getLocation());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PREFIX + "§c This command can only be used by a player.");
            return true;
        }

        Location location = lastDeathLocations.get(player.getUniqueId());
        if (location == null) {
            player.sendMessage(PREFIX + "§c You have not died since this server started.");
            return true;
        }

        player.sendMessage(PREFIX + "§a Your last death location is: §2§l" + formatLocation(location));
        return true;
    }

    private static String formatLocation(Location location) {
        String worldName = location.getWorld() == null ? "unknown world" : location.getWorld().getName();
        return worldName + " (X: " + location.getBlockX() + ", Y: " + location.getBlockY()
                + ", Z: " + location.getBlockZ() + ")";
    }
}
