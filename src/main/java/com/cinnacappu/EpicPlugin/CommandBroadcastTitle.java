package com.cinnacappu.EpicPlugin;

import java.util.Arrays;
import java.util.Locale;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBroadcastTitle implements CommandExecutor {
    public CommandBroadcastTitle() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 7) {
            sender.sendMessage("§cUsage: /broadcasttitle (big, medium, small) (color) (normal, italic, bold)"
                    + " (fade-in seconds) (duration seconds) (fade-out seconds) (message)");
            return true;
        }

        String size = args[0].toLowerCase(Locale.ROOT);
        String style = args[2].toLowerCase(Locale.ROOT);
        if (!size.equals("big") && !size.equals("medium") && !size.equals("small")) {
            sender.sendMessage("§cSize must be big, medium or small.");
            return true;
        }
        if (!style.equals("normal") && !style.equals("italic") && !style.equals("bold")) {
            sender.sendMessage("§cStyle must be normal, italic or bold.");
            return true;
        }

        try {
            ChatColor color = parseColor(args[1]);
            int fadein = secondsToTicks(args[3]);
            int duration = secondsToTicks(args[4]);
            int fadeout = secondsToTicks(args[5]);
            String format = color.toString();
            if (style.equals("italic")) format += ChatColor.ITALIC;
            if (style.equals("bold")) format += ChatColor.BOLD;
            String msg = format + String.join(" ", Arrays.copyOfRange(args, 6, args.length));

            for (Player player : Bukkit.getOnlinePlayers()) {
                displayTitle(player, size, msg, fadein, duration, fadeout);
            }
            sender.sendMessage("§2§l[EP]§r§a Title broadcast.");
        } catch (IllegalArgumentException ex) {
            sender.sendMessage("§c" + ex.getMessage());
        }
        return true;
    }

    static void displayTitle(Player player, String size, String msg, int fadein, int duration, int fadeout) {
        if (size.equals("small")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        } else if (size.equals("medium")) {
            player.sendTitle("", msg, fadein, duration, fadeout);
        } else {
            player.sendTitle(msg, "", fadein, duration, fadeout);
        }
    }

    static ChatColor parseColor(String value) {
        String color = value.toLowerCase(Locale.ROOT);
        if (color.equals("light_blue")) color = "aqua";
        if (color.equals("grey")) color = "gray";
        if (color.equals("dark_grey")) color = "dark_gray";
        if (color.equals("purple")) color = "dark_purple";
        if (color.equals("pink")) color = "light_purple";
        try {
            ChatColor result = ChatColor.of(color);
            if (result == ChatColor.BOLD || result == ChatColor.ITALIC || result == ChatColor.RESET
                    || result == ChatColor.UNDERLINE || result == ChatColor.STRIKETHROUGH || result == ChatColor.MAGIC) {
                throw new IllegalArgumentException();
            }
            return result;
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Use a valid color name or #RRGGBB.");
        }
    }

    static int secondsToTicks(String value) {
        try {
            int seconds = Integer.parseInt(value);
            if (seconds < 0 || seconds > Integer.MAX_VALUE / 20) throw new NumberFormatException();
            return seconds * 20;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Durations must be whole, non-negative seconds no greater than "
                    + Integer.MAX_VALUE / 20 + ".");
        }
    }
}
