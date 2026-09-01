package com.cinnacappu.EpicPlugin;

import java.util.Objects;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CommandEpicPlugin implements CommandExecutor {

    private final EpicPlugin plugin;

    public CommandEpicPlugin(EpicPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§2§l[EP]§r§b EpicPlugin v" + plugin.getDescription().getVersion() + " is running!");
        return true;
    }
}
