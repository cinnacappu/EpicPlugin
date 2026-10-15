package com.cinnacappu.EpicPlugin;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

final class ExplosionSettings {
    private static final ExplosionSettings shared = new ExplosionSettings();
    private long nextExplosion;

    static float power(CommandSender sender, float requested) {
        JavaPlugin plugin = JavaPlugin.getPlugin(EpicPlugin.class);
        double maximum = plugin.getConfig().getDouble("explosions.max-power", 100.0);
        double seconds = plugin.getConfig().getDouble("explosions.cooldown-seconds", 5.0);
        float result = shared.acquire(requested, maximum, seconds, System.nanoTime());
        if (Float.isNaN(result)) {
            sender.sendMessage("§2§l[EP]§r§c Please wait before causing another explosion.");
        } else if (result == 0) {
            sender.sendMessage("§2§l[EP]§r§c Explosions are disabled in the configuration.");
            return Float.NaN;
        }
        return result;
    }

    float acquire(float requested, double maximum, double seconds, long now) {
        if (!Double.isFinite(maximum)) maximum = 100.0;
        if (!Double.isFinite(seconds)) seconds = 5.0;
        float power = (float) Math.min(requested, Math.max(0.0, Math.min(maximum, 100.0)));
        if (power == 0) return 0;
        if (nextExplosion != 0 && now - nextExplosion < 0) return Float.NaN;
        nextExplosion = now + (long) (Math.max(0.0, Math.min(seconds, 3600.0)) * 1_000_000_000L);
        return power;
    }
}
