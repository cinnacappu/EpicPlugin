package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandNuke implements CommandExecutor {
    public CommandNuke() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player personWithWayTooMuchPower = (Player)sender;
            if (args.length > 1) {
                personWithWayTooMuchPower.sendMessage("§cUsage: /nuke (Player)");
                return true;
            }
            if (args.length < 1) {
                float power = ExplosionSettings.power(sender, 100.0F);
                if (Float.isNaN(power)) return true;
                personWithWayTooMuchPower.getLocation().getWorld().createExplosion(personWithWayTooMuchPower.getLocation(), power, true);
                if (personWithWayTooMuchPower.getGameMode() == GameMode.CREATIVE) {
                    personWithWayTooMuchPower.setHealth(0.0D);
                }

                if (personWithWayTooMuchPower.getGameMode() == GameMode.SPECTATOR) {
                    personWithWayTooMuchPower.setHealth(0.0D);
                }
            } else if (args.length == 1) {
                Player extremelyUnfortunatePlayer = Bukkit.getPlayerExact(args[0]);
                if (extremelyUnfortunatePlayer == null) {
                    personWithWayTooMuchPower.sendMessage("§2§l[EP]§r§c Player '" + args[0] + "' not found");
                    return false;
                }
                float power = ExplosionSettings.power(sender, 100.0F);
                if (Float.isNaN(power)) return true;
                extremelyUnfortunatePlayer.getLocation().getWorld().createExplosion(extremelyUnfortunatePlayer.getLocation(), power, true);
                if (extremelyUnfortunatePlayer.getGameMode() == GameMode.CREATIVE) {
                    extremelyUnfortunatePlayer.setHealth(0.0D);
                }

                if (extremelyUnfortunatePlayer.getGameMode() == GameMode.SPECTATOR) {
                    extremelyUnfortunatePlayer.setHealth(0.0D);
                }
            }

            personWithWayTooMuchPower.sendMessage("§4§lIt has been done.");
        }

        return true;
    }
}
