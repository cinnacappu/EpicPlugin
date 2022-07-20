package com.redstonerckz.EpicPlugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandRCmdBlock implements CommandExecutor {
    public CommandRCmdBlock() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            ItemStack REPEATING_COMMAND_BLOCK = new ItemStack(Material.REPEATING_COMMAND_BLOCK);
            player.getInventory().addItem(new ItemStack[]{REPEATING_COMMAND_BLOCK});
            player.sendMessage("Gave 1 §d[Repeating Command Block]§r to " + player.getDisplayName());
        }

        return true;
    }
}