package com.redstonerckz.EpicPlugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBoat implements CommandExecutor {
    public CommandBoat() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            ItemStack BIRCH_BOAT = new ItemStack(Material.BIRCH_BOAT);
            player.getInventory().addItem(new ItemStack[]{BIRCH_BOAT});
            player.sendMessage("§6§lGood luck on your MLG");
        }

        return true;
    }
}