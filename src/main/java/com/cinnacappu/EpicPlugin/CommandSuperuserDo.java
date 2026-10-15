package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Base64;

public class CommandSuperuserDo implements CommandExecutor, Listener {

    private final EpicPlugin plugin;
    private final Map<UUID, PendingRequest> pending = new HashMap<>();
    private final Set<UUID> lecturedPlayers = new HashSet<>();
    private final List<String> lectureHistory;

    public static final Map<UUID, Long> sudoLastAuth = new HashMap<>();

    private static final int TIMEOUT_SECONDS = 30;     // pending sudo prompt timeout
    private static final int MAX_ATTEMPTS = 3;         // max wrong password attempts
    private static final int AUTH_CACHE_SECONDS = 300; // cached sudo session lifetime (5min)

    private static final String[] SUDO_LECTURE_LINES = new String[]{
            "We trust you have received the usual lecture from the local System Administrator.",
            "It usually boils down to these three things:",
            "  #1) Respect the privacy of others.",
            "  #2) Think before you type.",
            "  #3) With great power comes great responsibility."
    };

    public CommandSuperuserDo(EpicPlugin plugin) {
        this.plugin = plugin;
        lectureHistory = plugin.getConfig().getStringList("sudo-lectured");
        for (String uuid : lectureHistory) {
            try {
                lecturedPlayers.add(UUID.fromString(uuid));
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Ignoring an invalid UUID in sudo-lectured: " + uuid);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String storedHash = plugin.getConfig().getString("sudo-password-hash", "");

        if (storedHash == null || storedHash.isBlank()) {
            sender.sendMessage("§2§l[EP]§r§c No sudo password set. Ask console to run §e/passwd§r§c.");
            return true;
        }

        /* ──────────────────────────────────────────
         * Console → run immediately
         * ────────────────────────────────────────── */
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                sender.sendMessage("§2§l[EP]§r§c Usage: /sudo <command...> OR /sudo <player> <command...>");
                return true;
            }
            String consoleCmd = join(args, 0);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCmd);
            sender.sendMessage("§2§l[EP]§r§a Ran as console: §e" + consoleCmd);
            return true;
        }

        /* ──────────────────────────────────────────
         * Must be player
         * ────────────────────────────────────────── */
        if (!(sender instanceof Player)) {
            sender.sendMessage("§2§l[EP]§r§c Only players or console may run this.");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        /* ──────────────────────────────────────────
         * /sudo cancel
         * ────────────────────────────────────────── */
        if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
            PendingRequest pr = pending.remove(uuid);
            if (pr != null) {
                if (pr.expiryTask != null) pr.expiryTask.cancel();
                sender.sendMessage("§2§l[EP]§r§a Sudo request cancelled.");
            } else {
                sender.sendMessage("§2§l[EP]§r§c No pending sudo request to cancel.");
            }
            return true;
        }

        /* ──────────────────────────────────────────
         * Stage 2 — waiting for password
         * ────────────────────────────────────────── */
        if (pending.containsKey(uuid)) {

            if (args.length != 1) {
                player.sendMessage("§2§l[EP]§r§c Password only — re-enter or wait for timeout.");
                return true;
            }

            PendingRequest pr = pending.get(uuid);

            if (pr.isExpired()) {
                pending.remove(uuid);
                if (pr.expiryTask != null) pr.expiryTask.cancel();
                player.sendMessage("§2§l[EP]§r§c Sudo request expired. Re-issue the command.");
                return true;
            }

            boolean correct = hash(args[0]).equals(storedHash);

            if (!correct) {
                pr.attempts++;
                if (pr.attempts >= MAX_ATTEMPTS) {
                    if (pr.expiryTask != null) pr.expiryTask.cancel();
                    pending.remove(uuid);
                    player.sendMessage("§2§l[EP]§r§c Incorrect password. Too many attempts — request cancelled.");
                } else {
                    int left = MAX_ATTEMPTS - pr.attempts;
                    player.sendMessage("§2§l[EP]§r§c Incorrect password. Attempts left: " + left);
                }
                return true;
            }

            // ✅ Password correct → cache credential
            sudoLastAuth.put(uuid, now);

            if (pr.expiryTask != null) pr.expiryTask.cancel();
            pending.remove(uuid);

            executeSudo(player, pr.targetUuid, pr.command);
            return true;
        }

        /* ──────────────────────────────────────────
         * Stage 1 — new sudo request
         * ────────────────────────────────────────── */
        if (args.length == 0) {
            player.sendMessage("§2§l[EP]§r§c Usage: /sudo <command...> OR /sudo <player> <command...>");
            player.sendMessage("§2§l[EP]§r§a Cancel pending sudo: §e/sudo cancel");
            return true;
        }

        Player possibleTarget = Bukkit.getPlayerExact(args[0]);
        String cmdToRun;
        UUID targetUuid = null;

        if (possibleTarget != null) {
            if (args.length < 2) {
                player.sendMessage("§2§l[EP]§r§c Usage: /sudo <player> <command...>");
                return true;
            }
            targetUuid = possibleTarget.getUniqueId();
            cmdToRun = join(args, 1);
        } else {
            cmdToRun = join(args, 0);
        }

        /* ──────────────────────────────────────────
         * Check cached authentication
         * ────────────────────────────────────────── */
        Long last = sudoLastAuth.get(uuid);
        if (last != null) {
            long elapsedSec = (now - last) / 1000;
            if (elapsedSec < AUTH_CACHE_SECONDS) {
                executeSudo(player, targetUuid, cmdToRun);
                return true;
            }
            sudoLastAuth.remove(uuid);
        }

        /* ──────────────────────────────────────────
         * First-time lecture
         * ────────────────────────────────────────── */
        sendLectureIfNeeded(player);

        /* ──────────────────────────────────────────
         * Create pending sudo request
         * ────────────────────────────────────────── */
        BukkitTask expiry = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            PendingRequest removed = pending.remove(uuid);
            if (removed != null) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null && p.isOnline()) {
                    p.sendMessage("§2§l[EP]§r§c Sudo request timed out.");
                }
            }
        }, TIMEOUT_SECONDS * 20L);

        pending.put(uuid, new PendingRequest(cmdToRun, expiry, targetUuid));

        player.sendMessage("§2§l[EP]§r§a [sudo] password for " + player.getName() + ":");

        return true;
    }

    /* ──────────────────────────────────────────
     * Execute sudo
     * ────────────────────────────────────────── */
    private void executeSudo(Player issuer, UUID targetUuid, String cmd) {

        // run as target
        if (targetUuid != null) {
            Player target = Bukkit.getPlayer(targetUuid);
            if (target == null || !target.isOnline()) {
                issuer.sendMessage("§2§l[EP]§r§c Target player is no longer online.");
                return;
            }

            try {
                runWithOp(target, () -> Bukkit.dispatchCommand(target, cmd));
                issuer.sendMessage("§2§l[EP]§r§a Ran as §e" + target.getName() + "§a: §e" + cmd);
            } catch (Exception ex) {
                issuer.sendMessage("§2§l[EP]§r§c Error executing command as " + target.getName());
                ex.printStackTrace();
            }

            return;
        }

        // run as issuer
        try {
            runWithOp(issuer, () -> Bukkit.dispatchCommand(issuer, cmd));
            issuer.sendMessage("§2§l[EP]§r§a Ran: §e" + cmd);
        } catch (Exception ex) {
            issuer.sendMessage("§2§l[EP]§r§c Error executing command.");
            ex.printStackTrace();
        }

    }

    static void runWithOp(Player player, Runnable command) {
        boolean wasOp = player.isOp();
        try {
            player.setOp(true);
            command.run();
        } finally {
            player.setOp(wasOp);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PendingRequest request = pending.remove(uuid);
        if (request != null && request.expiryTask != null) request.expiryTask.cancel();
        sudoLastAuth.remove(uuid);
    }

    public void shutdown() {
        for (PendingRequest request : pending.values()) {
            if (request.expiryTask != null) request.expiryTask.cancel();
        }
        pending.clear();
        sudoLastAuth.clear();
        lecturedPlayers.clear();
    }

    /* ──────────────────────────────────────────
     * Utilities
     * ────────────────────────────────────────── */
    private String join(String[] args, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
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

    private void sendLectureIfNeeded(Player executor) {
        if (!lecturedPlayers.add(executor.getUniqueId())) return;

        executor.sendMessage("§2§l[EP]§r§a Before you proceed…");
        for (String line : SUDO_LECTURE_LINES) {
            executor.sendMessage("§7" + line);
        }

        lectureHistory.add(executor.getUniqueId().toString());
        plugin.getConfig().set("sudo-lectured", lectureHistory);
        plugin.saveConfigLater();
    }

    /* pending request container */
    private static class PendingRequest {
        final String command;
        final BukkitTask expiryTask;
        final UUID targetUuid;
        int attempts = 0;
        long createdAt = System.currentTimeMillis();

        PendingRequest(String command, BukkitTask expiryTask, UUID targetUuid) {
            this.command = command;
            this.expiryTask = expiryTask;
            this.targetUuid = targetUuid;
        }

        boolean isExpired() {
            return System.currentTimeMillis() - createdAt > TIMEOUT_SECONDS * 1000L;
        }
    }
}
