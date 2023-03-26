package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandGod implements CommandExecutor {
    private ArrayList<Player> god_list = new ArrayList();
    private EpicPlugin plugin;

    public CommandGod() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
                if (this.god_list.contains(player)) {
                    this.god_list.remove(player);
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    player.sendMessage("§2§l[EP]§r§a God mode is now turned §2§loff!");
                } else if (!this.god_list.contains(player)) {
                    this.god_list.add(player);
                    PotionEffectType potionEffectType = PotionEffectType.DAMAGE_RESISTANCE;
                    player.addPotionEffect(new PotionEffect(potionEffectType, 1000000, 5, true, false));
                    player.setArrowsInBody(0);
                    player.setFallDistance(1000000.0F);
                    player.setFireTicks(0);
                    player.setHealth(20.0D);
                    player.setFoodLevel(20);
                    player.sendMessage("§2§l[EP]§r§a God mode is now turned §2§lon!");
                }
        } else {
            Bukkit.getLogger().info("§2§l[EP]§r§c You cannot do that!");
        }
        return true;
    }
}