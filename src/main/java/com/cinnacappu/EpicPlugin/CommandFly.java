package com.cinnacappu.EpicPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CommandFly implements CommandExecutor, Listener {
    private final Map<UUID, Boolean> flyingPlayers = new HashMap<>();

    public CommandFly() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("epicplugin.fly")) {
                Boolean previousFlight = flyingPlayers.remove(player.getUniqueId());
                if (previousFlight != null) {
                    player.sendMessage("§2§l[EP]§r§a Fly is now turned §2§loff!");
                    resetFlight(player, previousFlight);
                } else {
                    flyingPlayers.put(player.getUniqueId(), player.getAllowFlight());
                    player.sendMessage("§2§l[EP]§r§a Fly is now turned §2§lon!");
                    player.setAllowFlight(true);
                }
            } else {
                player.sendMessage("§2§l[EP]§r§c You cannot do that!");
            }
        }
        return true;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Boolean previousFlight = flyingPlayers.remove(player.getUniqueId());
        if (previousFlight != null) resetFlight(player, previousFlight);
    }

    public void shutdown() {
        for (Map.Entry<UUID, Boolean> entry : flyingPlayers.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) resetFlight(player, entry.getValue());
        }
        flyingPlayers.clear();
    }

    private void resetFlight(Player player, boolean previousFlight) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (!previousFlight) player.setFlying(false);
        player.setAllowFlight(previousFlight);
    }
}
