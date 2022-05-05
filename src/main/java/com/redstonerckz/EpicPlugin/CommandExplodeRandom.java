package com.redstonerckz.EpicPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExplodeRandom implements CommandExecutor {
    public CommandExplodeRandom() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player personWithWayTooMuchPower = (Player) sender;
            List<Player> onlinePlayersList = new ArrayList(Bukkit.getOnlinePlayers());
            int result = ThreadLocalRandom.current().nextInt(0, onlinePlayersList.size());
            String unfortunatePlayer = ((Player)onlinePlayersList.get(result)).getDisplayName();
            Player extremelyUnfortunatePlayer = Bukkit.getPlayer(String.valueOf(unfortunatePlayer));
            extremelyUnfortunatePlayer.getLocation().getWorld().createExplosion(extremelyUnfortunatePlayer.getLocation(), 10.0F, true);
            if (extremelyUnfortunatePlayer.getGameMode() == GameMode.CREATIVE) {
                extremelyUnfortunatePlayer.setHealth(0.0D);
            }

            if (extremelyUnfortunatePlayer.getGameMode() == GameMode.SPECTATOR) {
                extremelyUnfortunatePlayer.setHealth(0.0D);
            }

            personWithWayTooMuchPower.sendMessage("§4It has been done.");
        }

        return true;
    }
}