package com.redstonerckz.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CommandTNTRandom implements CommandExecutor {
    public CommandTNTRandom() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            List<Player> onlinePlayersList = new ArrayList(Bukkit.getOnlinePlayers());
            int result = ThreadLocalRandom.current().nextInt(0, onlinePlayersList.size());
            String unfortunatePlayer = ((Player)onlinePlayersList.get(result)).getDisplayName();
            Player sadPlayer = Bukkit.getPlayer(String.valueOf(unfortunatePlayer));
            sadPlayer.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
        }
        return true;
    }
}