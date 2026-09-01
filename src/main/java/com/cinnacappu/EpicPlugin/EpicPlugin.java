package com.cinnacappu.EpicPlugin;

import java.util.Objects;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class EpicPlugin extends JavaPlugin {

    private static final int BSTATS_PLUGIN_ID = 14367;
    private static final int SPIGOT_RESOURCE_ID = 95074;

    @Override
    public void onEnable() {
        getLogger().info("EpicPlugin v" + getDescription().getVersion() + " loading...");

        new Metrics(this, BSTATS_PLUGIN_ID);
        new EpicPluginUpdateChecker(this, SPIGOT_RESOURCE_ID).getVersion(version -> {
            String localVersion = getDescription().getVersion();
            if (localVersion.equals(version)) {
                getLogger().info("EpicPlugin is up-to-date (v" + localVersion + ").");
            } else {
                getLogger().warning(
                        "EpicPlugin v" + localVersion + " is outdated; latest version is v" + version
                                + ". See https://www.spigotmc.org/resources/epicplugin.95074/");
            }
        });

        registerCommand("epicplugin", new CommandEpicPlugin(this));
        registerCommand("fly", new CommandFly());
        registerCommand("c", new CommandC());
        registerCommand("a", new CommandA());
        registerCommand("s", new CommandS());
        registerCommand("sp", new CommandSp());
        registerCommand("waterbucket", new CommandWaterBucket());
        registerCommand("boat", new CommandBoat());
        CommandHeal healCommand = new CommandHeal();
        registerCommand("heal", healCommand);
        registerCommand("heartup", healCommand);
        registerCommand("cmdblock", new CommandCmdBlock());
        registerCommand("ccmdblock", new CommandCCmdBlock());
        registerCommand("rcmdblock", new CommandRCmdBlock());
        registerCommand("barrier", new CommandBarrier());
        registerCommand("k", new CommandK());
        registerCommand("feed", new CommandFeed());
        registerCommand("god", new CommandGod());
        registerCommand("confess", new CommandConfess());
        registerCommand("broadcast", new CommandBroadcast());
        registerCommand("calculate", new CommandCalculate());
        registerCommand("randomnumber", new CommandRandomNumber());
        registerCommand("explode", new CommandExplode());
        registerCommand("day", new CommandDay());
        registerCommand("night", new CommandNight());
        registerCommand("nuke", new CommandNuke());
        registerCommand("weatherclear", new CommandWeatherClear());
        registerCommand("thunder", new CommandThunder());
        registerCommand("rain", new CommandRain());
        registerCommand("dayclear", new CommandDayClear());
        registerCommand("nukerandom", new CommandNukeRandom());
        registerCommand("exploderandom", new CommandExplodeRandom());
        registerCommand("whereis", new CommandWhereIs());
        registerCommand("tphere", new CommandTPHere());
        registerCommand("lightning", new CommandLightning());
        registerCommand("inhand", new CommandInHand());
        registerCommand("misclick", new CommandMisclick());
        registerCommand("bed", new CommandBed());
        registerCommand("tnt", new CommandTNT());
        registerCommand("tntrandom", new CommandTNTRandom());
        registerCommand("guess", new CommandGuess());
        registerCommand("superuserdo", new CommandSuperuserDo());
        registerCommand("iamtryingtobenicebutitshard", new CommandIAmTryingToBeNiceButItsHard());
        registerCommand("magic8ball", new CommandMagic8Ball());
        registerCommand("broadcasttitle", new CommandBroadcastTitle());
        registerCommand("damage", new CommandDamage());
        registerCommand("ping", new CommandPing());
        registerCommand("saveme", new CommandSaveMe());

        CommandLastDeath lastDeathCommand = new CommandLastDeath();
        registerCommand("lastdeath", lastDeathCommand);
        getServer().getPluginManager().registerEvents(lastDeathCommand, this);

        registerCommand("saturation", new CommandSaturation());
        registerCommand("joinspoof", new CommandJoinSpoof());
        registerCommand("hunger", new CommandHunger());
        registerCommand("zeus", new CommandZeus());
        registerCommand("saturate", new CommandSaturate());

        CommandShakespeare shakespeareCommand = new CommandShakespeare(this);
        registerCommand("shakespeare", shakespeareCommand);
        getServer().getPluginManager().registerEvents(shakespeareCommand, this);

        registerCommand("passwd", new CommandPasswd());
        getLogger().info("EpicPlugin v" + getDescription().getVersion() + " loaded.");
    }

    @Override
    public void onDisable() {
        getLogger().info("EpicPlugin v" + getDescription().getVersion() + " disabled.");
    }

    private void registerCommand(String name, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(name), "Command /" + name + " is missing from plugin.yml")
                .setExecutor(executor);
    }
}
