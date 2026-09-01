package com.cinnacappu.EpicPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBroadcastTitle implements CommandExecutor {
    public CommandBroadcastTitle() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String tempsize = args[0];
            String color = args[1];
            String tempstyle = args[2];
            String temptempfadein = args[3];
            String temptempduration = args[4];
            String temptempfadeout = args[5];
            String msg = "";
            String style = "";

            int tempfadein = (int)Integer.parseInt(temptempfadein);
            int tempduration = (int)Integer.parseInt(temptempduration);
            int tempfadeout = (int)Integer.parseInt(temptempfadeout);

            int fadein = tempfadein * 20;
            int duration = tempduration * 20;
            int fadeout = tempfadeout * 20;

            if (tempstyle.equalsIgnoreCase("normal")) {
                style = "\"bold\":\"false";
            }
            if (tempstyle.equalsIgnoreCase("italic")) {
                style = "\"italics\":\"true";
            }
            if (tempstyle.equalsIgnoreCase("bold")) {
                style = "\"bold\":\"true";
            }

            for (int i = 6; i != args.length; ++i) {
                msg = msg + args[i] + " ";
            }

            if (tempsize.equalsIgnoreCase("small")) {
                String size = "actionbar";
                player.performCommand("title @a times " + fadein + " " + duration + " " + fadeout);
                player.performCommand("title @a " + size + " {\"text\":\"" + msg + "\",\"color\":\"" + color + "\"," + style + "\"}");
            } else if (tempsize.equalsIgnoreCase("medium")) {
                String size = "subtitle";
                player.performCommand("title @a title {\"text\":\"\"}");
                player.performCommand("title @a times " + fadein + " " + duration + " " + fadeout);
                player.performCommand("title @a " + size + " {\"text\":\"" + msg + "\",\"color\":\"" + color + "\"," + style + "\"}");
            } else if (tempsize.equalsIgnoreCase("big")) {
                String size = "title";
                player.performCommand("title @a times " + fadein + " " + duration + " " + fadeout);
                player.performCommand("title @a " + size + " {\"text\":\"" + msg + "\",\"color\":\"" + color + "\"," + style + "\"}");
            }

        }
        return true;
    }
}