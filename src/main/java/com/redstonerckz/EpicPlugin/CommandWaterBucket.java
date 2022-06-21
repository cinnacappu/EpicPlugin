package com.redstonerckz.EpicPlugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandWaterBucket implements CommandExecutor {
    public CommandWaterBucket() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player proMLGGod = (Player)sender;
            ItemStack WATER_BUCKET = new ItemStack(Material.WATER_BUCKET);
            proMLGGod.getInventory().addItem(new ItemStack[]{WATER_BUCKET});
            proMLGGod.sendMessage("§2§lEnjoy your MLG");
        }

        return true;
    }
}