package com.cinnacappu.EpicPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class CommandGod implements CommandExecutor {

    private static final String PREFIX = "§2§l[EP]§r";
    private final Set<UUID> godPlayers = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PREFIX + "§c This command can only be used by a player.");
            return true;
        }

        UUID playerId = player.getUniqueId();
        if (godPlayers.remove(playerId)) {
            player.removePotionEffect(PotionEffectType.RESISTANCE);
            player.sendMessage(PREFIX + "§a God mode is now turned §2§loff!");
            return true;
        }

        godPlayers.add(playerId);
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 5, true, false));
        player.setArrowsInBody(0);
        player.setFallDistance(0.0F);
        player.setFireTicks(0);
        player.setHealth(maxHealth(player));
        player.setFoodLevel(20);
        player.setSaturation(20.0F);
        player.sendMessage(PREFIX + "§a God mode is now turned §2§lon!");
        return true;
    }

    private static double maxHealth(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        return attribute == null ? 20.0 : attribute.getValue();
    }
}
