// EpicPlugin v3.9 Main Class
package com.redstonerckz.EpicPlugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

public class EpicPlugin extends JavaPlugin {

    public EpicPlugin() {
    }
    @Override
    public void onEnable() {
        int pluginId = 14367;
        Metrics metrics = new Metrics(this, pluginId);
        Bukkit.getLogger().info("§2§l[EP]§r§b v3.9 loading...");
        (new EpicPluginUpdateChecker(this, 95074)).getVersion((version) -> {
            if (this.getDescription().getVersion().equals(version)) {
                Bukkit.getLogger().info("EpicPlugin is up-to-date!");
                Bukkit.getLogger().info("Local Version: v" + this.getDescription().getVersion());
                Bukkit.getLogger().info("Version from API: v" + version);
            } else {
                Bukkit.getLogger().info("§2§l[EP]§r§c EpicPlugin needs an update! Go to §l§2https://www.spigotmc.org/resources/epicplugin.95074/");
                Bukkit.getLogger().info("Local Version: v" + this.getDescription().getVersion());
                Bukkit.getLogger().info("Version from API: v" + version);
            }

        });
        Bukkit.getLogger().info("§6******************************");
        Bukkit.getLogger().info("§2§l[EP]§r§b v3.9 loaded!");
        Bukkit.getLogger().info("§6******************************");
        this.getCommand("epicplugin").setExecutor(new CommandEpicPlugin());

        this.getCommand("h").setExecutor(new CommandH());
        this.getCommand("fly").setExecutor(new CommandFly());
        this.getCommand("c").setExecutor(new CommandC());
        this.getCommand("a").setExecutor(new CommandA());
        this.getCommand("s").setExecutor(new CommandS());
        this.getCommand("sp").setExecutor(new CommandSp());
        this.getCommand("waterbucket").setExecutor(new CommandWaterBucket());
        this.getCommand("boat").setExecutor(new CommandBoat());
        this.getCommand("heal").setExecutor(new CommandHeal());
        this.getCommand("cmdblock").setExecutor(new CommandCmdBlock());
        this.getCommand("ccmdblock").setExecutor(new CommandCCmdBlock());
        this.getCommand("rcmdblock").setExecutor(new CommandRCmdBlock());
        this.getCommand("barrier").setExecutor(new CommandBarrier());
        this.getCommand("k").setExecutor(new CommandK());
        this.getCommand("feed").setExecutor(new CommandFeed());
        this.getCommand("god").setExecutor(new CommandGod());
        this.getCommand("confess").setExecutor(new CommandConfess());
        this.getCommand("broadcast").setExecutor(new CommandBroadcast());
        this.getCommand("calculate").setExecutor(new CommandCalculate());
        this.getCommand("randomnumber").setExecutor(new CommandRandomNumber());
        this.getCommand("explode").setExecutor(new CommandExplode());
        this.getCommand("day").setExecutor(new CommandDay());
        this.getCommand("night").setExecutor(new CommandNight());
        this.getCommand("nuke").setExecutor(new CommandNuke());
        this.getCommand("wclear").setExecutor(new CommandWClear());
        this.getCommand("thunder").setExecutor(new CommandThunder());
        this.getCommand("rain").setExecutor(new CommandRain());
        this.getCommand("dayclear").setExecutor(new CommandDayClear());
        this.getCommand("nukerandom").setExecutor(new CommandNukeRandom());
        this.getCommand("exploderandom").setExecutor(new CommandExplodeRandom());
        this.getCommand("whereis").setExecutor(new CommandWhereIs());
        this.getCommand("tphere").setExecutor(new CommandTPHere());
        this.getCommand("lightning").setExecutor(new CommandLightning());
        this.getCommand("inhand").setExecutor(new CommandInHand());
        this.getCommand("misclick").setExecutor(new CommandMisclick());
        this.getCommand("bed").setExecutor(new CommandBed());
        this.getCommand("tnt").setExecutor(new CommandTNT());
        this.getCommand("tntrandom").setExecutor(new CommandTNTRandom());
        this.getCommand("guess").setExecutor(new CommandGuess());
        this.getCommand("superuserdo").setExecutor(new CommandSuperuserDo());
        this.getCommand("iamtryingtobenicebutitshard").setExecutor(new CommandIAmTryingToBeNiceButItsHard());
        this.getCommand("magic8ball").setExecutor(new CommandMagic8Ball());
        this.getCommand("broadcasttitle").setExecutor(new CommandBroadcastTitle());
        this.getCommand("damage").setExecutor(new CommandDamage());
        this.getCommand("ping").setExecutor(new CommandPing());
        this.getCommand("saveme").setExecutor(new CommandSaveMe());
    }

    public void onDisable() {
        Bukkit.getLogger().info("§6******************************");
        Bukkit.getLogger().info("§2§l[EP]§r§b v3.9 disabled!");
        Bukkit.getLogger().info("§6******************************");
    }
}