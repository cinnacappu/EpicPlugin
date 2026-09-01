package com.cinnacappu.EpicPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CommandPasswd implements CommandExecutor {

    private String awaitingFirst = null;   // first password entry
    private boolean waitingSecond = false; // flag waiting for confirm

    public CommandPasswd() {}

    private JavaPlugin getPlugin() {
        return JavaPlugin.getPlugin(EpicPlugin.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        /* Must be console */
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("§2§l[EP]§r§c Only console may run this command.");
            return true;
        }

        /* Stage 1 — ask for new password */
        if (!waitingSecond) {

            // If password supplied on same invocation: /passwd <newpass>
            if (args.length == 1) {
                awaitingFirst = args[0];
                waitingSecond = true;
                sender.sendMessage("§2§l[EP]§r§a Retype new password:");
                return true;
            }

            // Else interactive mode: /passwd then prompt
            sender.sendMessage("§2§l[EP]§r§a Enter new password:");
            waitingSecond = true;
            return true;
        }

        /* Stage 2 — confirm password */
        if (waitingSecond) {

            if (args.length != 1) {
                sender.sendMessage("§2§l[EP]§r§c Please enter only the confirmation password.");
                return true;
            }

            String confirm = args[0];

            if (awaitingFirst == null) {
                // Should not happen but safe reset
                resetState();
                sender.sendMessage("§2§l[EP]§r§c Unexpected state. Try again.");
                return true;
            }

            if (!awaitingFirst.equals(confirm)) {
                resetState();
                sender.sendMessage("§2§l[EP]§r§c Passwords do not match. Aborted.");
                return true;
            }

            // ✅ Password confirmed → hash + store
            String hashed = hash(confirm);
            JavaPlugin plugin = getPlugin();
            plugin.getConfig().set("sudo-password-hash", hashed);
            plugin.saveConfig();

            // ✅ Invalidate ALL sudo auth cache
            CommandSuperuserDo.sudoLastAuth.clear();

            sender.sendMessage("§2§l[EP]§r§a Password updated successfully.");

            resetState();
            return true;
        }

        return true;
    }

    private void resetState() {
        awaitingFirst = null;
        waitingSecond = false;
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
