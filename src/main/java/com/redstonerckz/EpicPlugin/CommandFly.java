package com.redstonerckz.EpicPlugin;

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
                    player.sendMessage("§2§l[EP]§r§b Fly is now turned off!");
                    player.setAllowFlight(false);
                } else if (!this.flying_players_list.contains(player)) {
                    this.flying_players_list.add(player);
                    player.sendMessage("§2§l[EP]§r§b Fly is now turned on!");
                    player.setAllowFlight(true);
                }
            } else {
                player.sendMessage("§cYou do not have permission to use this command.");
            }
        } else {
            System.out.println("§2§l[EP]§r§b You cannot do that!");
        }
        return true;
    }
}