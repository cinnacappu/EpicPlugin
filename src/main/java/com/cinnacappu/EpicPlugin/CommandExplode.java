package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExplode implements CommandExecutor {
    public CommandExplode() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player personWithTooMuchPower = (Player)sender;
            if (args.length > 1) {
                personWithTooMuchPower.sendMessage("§cUsage: /explode (Player)");
                return true;
            }
            if (args.length < 1) {
                float power = ExplosionSettings.power(sender, 10.0F);
                if (Float.isNaN(power)) return true;
                personWithTooMuchPower.getLocation().getWorld().createExplosion(personWithTooMuchPower.getLocation(), power, true);
                if (personWithTooMuchPower.getGameMode() == GameMode.CREATIVE) {
                    personWithTooMuchPower.setHealth(0.0D);
                }

                if (personWithTooMuchPower.getGameMode() == GameMode.SPECTATOR) {
                    personWithTooMuchPower.setHealth(0.0D);
                }
            } else if (args.length == 1) {
                Player veryUnfortunatePlayer = Bukkit.getPlayerExact(args[0]);
                if (veryUnfortunatePlayer == null) {
                    personWithTooMuchPower.sendMessage("§2§l[EP]§r§c Player '" + args[0] + "' not found");
                    return false;
                }
                float power = ExplosionSettings.power(sender, 10.0F);
                if (Float.isNaN(power)) return true;
                veryUnfortunatePlayer.getLocation().getWorld().createExplosion(veryUnfortunatePlayer.getLocation(), power, true);
                if (veryUnfortunatePlayer.getGameMode() == GameMode.CREATIVE) {
                    veryUnfortunatePlayer.setHealth(0.0D);
                }

                if (veryUnfortunatePlayer.getGameMode() == GameMode.SPECTATOR) {
                    veryUnfortunatePlayer.setHealth(0.0D);
                }
            }

            personWithTooMuchPower.sendMessage("§4It has been done.");
        }

        return true;
    }
}
