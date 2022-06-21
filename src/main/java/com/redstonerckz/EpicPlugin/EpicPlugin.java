// EpicPlugin v3.6 Main Class
package com.redstonerckz.EpicPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public class EpicPlugin extends JavaPlugin {

    public EpicPlugin() {
    }
    @Override
    public void onEnable() {
        int pluginId = 14367;
        Metrics metrics = new Metrics(this, pluginId);
        System.out.println("§2§l[EP]§r§b v3.6 loading...");
        (new EpicPluginUpdateChecker(this, 95074)).getVersion((version) -> {
            if (this.getDescription().getVersion().equals(version)) {
                System.out.println("EpicPlugin is up-to-date!");
                System.out.println("Local Version: v" + this.getDescription().getVersion());
                System.out.println("Version from API: v" + version);
            } else {
                System.out.println("§2§l[EP]§r§c EpicPlugin needs an update! Go to §l§2https://www.spigotmc.org/resources/epicplugin.95074/");
                System.out.println("Local Version: v" + this.getDescription().getVersion());
                System.out.println("Version from API: v" + version);
            }

        });
        System.out.println("§6******************************");
        System.out.println("§2§l[EP]§r§b v3.6 loaded!");
        System.out.println("§6******************************");
        this.getCommand("epicplugin").setExecutor(new CommandEpicPlugin());
        this.getCommand("epreload").setExecutor(new CommandEPReload());
        if (this.getCommand("epreload").equals(true)) {
            this.reloadConfig();
            this.saveConfig();
        }

        this.getCommand("e").setExecutor(new CommandE());
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
        this.getCommand("god").setExecutor(new CommandGod(this));
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
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
    }

    public void onDisable() {
        System.out.println("§6******************************");
        System.out.println("§2§l[EP]§r§b v3.6 disabled!");
        System.out.println("§6******************************");
    }
}