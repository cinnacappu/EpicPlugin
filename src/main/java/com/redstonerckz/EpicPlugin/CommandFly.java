package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly implements CommandExecutor {
    private ArrayList<Player> flying_players_list = new ArrayList();

    public CommandFly() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("epicplugin.fly")) {
                if (this.flying_players_list.contains(player)) {
                    this.flying_players_list.remove(player);
                    player.sendMessage("§2§l[EP]§r§a Fly is now turned §2§loff!");
                    player.setAllowFlight(false);
                } else if (!this.flying_players_list.contains(player)) {
                    this.flying_players_list.add(player);
                    player.sendMessage("§2§l[EP]§r§a Fly is now turned §2§lon!");
                    player.setAllowFlight(true);
                }
            } else {
                Bukkit.getLogger().info("§2§l[EP]§r§c You cannot do that!");
            }
        }
        return true;
    }
}