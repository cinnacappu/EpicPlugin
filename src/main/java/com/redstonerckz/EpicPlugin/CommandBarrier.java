package com.redstonerckz.EpicPlugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBarrier implements CommandExecutor {
    public CommandBarrier() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            ItemStack BARRIER = new ItemStack(Material.BARRIER, 1);
            player.getInventory().addItem(new ItemStack[]{BARRIER});
            player.sendMessage("Gave 1 §d[Barrier]§r to " + player.getDisplayName());
        }

        return true;
    }
}
