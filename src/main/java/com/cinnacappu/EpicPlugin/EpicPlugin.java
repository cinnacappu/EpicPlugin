package com.cinnacappu.EpicPlugin;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class EpicPlugin extends JavaPlugin {

    private static final int BSTATS_PLUGIN_ID = 14367;
    private static final int SPIGOT_RESOURCE_ID = 95074;
    private Metrics metrics;
    private CommandFly flyCommand;
    private CommandGod godCommand;
    private CommandSuperuserDo sudoCommand;
    private CommandShakespeare shakespeareCommand;
    private ConfigSnapshotWriter configWriter;
    private BukkitTask configSaveTask;

    @Override
    public void onEnable() {
        getLogger().info("EpicPlugin v" + getDescription().getVersion() + " loading...");

        saveDefaultConfig();
        configWriter = new ConfigSnapshotWriter(getDataFolder().toPath().resolve("config.yml"));
        metrics = new Metrics(this, BSTATS_PLUGIN_ID);
        new EpicPluginUpdateChecker(this, SPIGOT_RESOURCE_ID).getVersion(version -> {
            String localVersion = getDescription().getVersion();
            if (!EpicPluginUpdateChecker.isNewer(version, localVersion)) {
                getLogger().info("EpicPlugin is up-to-date (v" + localVersion + ").");
            } else {
                getLogger().warning(
                        "EpicPlugin v" + localVersion + " is outdated; latest version is v" + version
                                + ". See https://www.spigotmc.org/resources/epicplugin.95074/");
            }
        });

        registerCommand("epicplugin", new CommandEpicPlugin(this));
        flyCommand = new CommandFly();
        registerCommand("fly", flyCommand);
        getServer().getPluginManager().registerEvents(flyCommand, this);
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
        godCommand = new CommandGod();
        registerCommand("god", godCommand);
        getServer().getPluginManager().registerEvents(godCommand, this);
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
        sudoCommand = new CommandSuperuserDo(this);
        registerCommand("superuserdo", sudoCommand);
        getServer().getPluginManager().registerEvents(sudoCommand, this);
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

        shakespeareCommand = new CommandShakespeare(this);
        registerCommand("shakespeare", shakespeareCommand);
        getServer().getPluginManager().registerEvents(shakespeareCommand, this);

        registerCommand("passwd", new CommandPasswd());
        getLogger().info("EpicPlugin v" + getDescription().getVersion() + " loaded.");
    }

    @Override
    public void onDisable() {
        if (sudoCommand != null) sudoCommand.shutdown();
        if (flyCommand != null) flyCommand.shutdown();
        if (godCommand != null) godCommand.shutdown();
        if (shakespeareCommand != null) shakespeareCommand.shutdown();
        if (metrics != null) metrics.shutdown();
        if (configSaveTask != null) {
            configSaveTask.cancel();
            configSaveTask = null;
        }
        if (configWriter != null && configWriter.isDirty()) saveConfig();
        getLogger().info("EpicPlugin v" + getDescription().getVersion() + " disabled.");
    }

    void saveConfigLater() {
        configWriter.markDirty();
        if (configSaveTask != null) return;
        configSaveTask = getServer().getScheduler().runTaskLater(this, () -> {
            configSaveTask = null;
            ConfigSnapshotWriter writer = configWriter;
            String snapshot = getConfig().saveToString();
            long revision = writer.getRevision();
            getServer().getScheduler().runTaskAsynchronously(this, () -> {
                try {
                    writer.saveSnapshot(snapshot, revision);
                } catch (IOException ex) {
                    getLogger().log(Level.SEVERE, "Could not save config.yml", ex);
                }
            });
        }, 20L);
    }

    @Override
    public void saveConfig() {
        if (configWriter == null) {
            super.saveConfig();
            return;
        }
        if (configSaveTask != null) {
            configSaveTask.cancel();
            configSaveTask = null;
        }
        try {
            configWriter.saveNow(getConfig().saveToString());
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config.yml", ex);
        }
    }

    private void registerCommand(String name, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(name), "Command /" + name + " is missing from plugin.yml")
                .setExecutor(executor);
    }
}
