package com.cinnacappu.EpicPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class CommandGod implements CommandExecutor, Listener {

    private static final String PREFIX = "§2§l[EP]§r";
    private final Map<UUID, GodState> godPlayers = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PREFIX + "§c This command can only be used by a player.");
            return true;
        }

        UUID playerId = player.getUniqueId();
        GodState previousState = godPlayers.remove(playerId);
        if (previousState != null) {
            resetResistance(player, previousState);
            player.sendMessage(PREFIX + "§a God mode is now turned §2§loff!");
            return true;
        }

        PotionEffect previousResistance = player.getPotionEffect(PotionEffectType.RESISTANCE);
        int enabledAt = player.getTicksLived();
        boolean applied = player.addPotionEffect(
                new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 5, true, false));
        godPlayers.put(playerId, new GodState(previousResistance, enabledAt, applied));
        player.setArrowsInBody(0);
        player.setFallDistance(0.0F);
        player.setFireTicks(0);
        player.setHealth(maxHealth(player));
        player.setFoodLevel(20);
        player.setSaturation(20.0F);
        player.sendMessage(PREFIX + "§a God mode is now turned §2§lon!");
        return true;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GodState state = godPlayers.remove(player.getUniqueId());
        if (state != null) resetResistance(player, state);
    }

    public void shutdown() {
        for (Map.Entry<UUID, GodState> entry : godPlayers.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) resetResistance(player, entry.getValue());
        }
        godPlayers.clear();
    }

    private void resetResistance(Player player, GodState state) {
        if (!state.applied) return;
        PotionEffect current = player.getPotionEffect(PotionEffectType.RESISTANCE);
        long elapsed = (long) player.getTicksLived() - state.enabledAt;
        if (current == null || !isGodEffect(current.getAmplifier(), current.isAmbient(), current.hasParticles(),
                current.hasIcon(), current.getDuration(), elapsed)) return;

        player.removePotionEffect(PotionEffectType.RESISTANCE);
        PotionEffect previous = state.previousResistance;
        if (previous == null) return;
        int duration = remainingDuration(previous.getDuration(), elapsed);
        if (duration == PotionEffect.INFINITE_DURATION || duration > 0) {
            player.addPotionEffect(new PotionEffect(previous.getType(), duration, previous.getAmplifier(),
                    previous.isAmbient(), previous.hasParticles(), previous.hasIcon()));
        }
    }

    static boolean isGodEffect(int amplifier, boolean ambient, boolean particles, boolean icon,
                               int duration, long elapsed) {
        return elapsed >= 0 && elapsed < Integer.MAX_VALUE && amplifier == 5 && ambient && !particles && !icon
                && duration > 0 && Math.abs((long) duration - (Integer.MAX_VALUE - elapsed)) <= 2;
    }

    static int remainingDuration(int duration, long elapsed) {
        if (elapsed < 0) return 0;
        if (duration == PotionEffect.INFINITE_DURATION) return duration;
        return (int) Math.max(0, (long) duration - elapsed);
    }

    private record GodState(PotionEffect previousResistance, int enabledAt, boolean applied) {
    }

    private static double maxHealth(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        return attribute == null ? 20.0 : attribute.getValue();
    }
}
