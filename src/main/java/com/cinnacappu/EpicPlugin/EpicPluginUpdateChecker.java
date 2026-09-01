package com.cinnacappu.EpicPlugin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Scanner;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

final class EpicPluginUpdateChecker {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(10);

    private final Plugin plugin;
    private final int resourceId;

    EpicPluginUpdateChecker(Plugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    void getVersion(Consumer<String> onSuccess) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            HttpURLConnection connection = null;
            try {
                URI endpoint = URI.create(
                        "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
                connection = (HttpURLConnection) endpoint.toURL().openConnection();
                connection.setConnectTimeout(Math.toIntExact(CONNECT_TIMEOUT.toMillis()));
                connection.setReadTimeout(Math.toIntExact(READ_TIMEOUT.toMillis()));
                connection.setRequestProperty("User-Agent", plugin.getName() + " update checker");

                try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
                    if (scanner.hasNextLine()) {
                        String version = scanner.nextLine().trim();
                        if (!version.isEmpty()) {
                            Bukkit.getScheduler().runTask(plugin, () -> onSuccess.accept(version));
                        }
                    }
                }
            } catch (IOException exception) {
                plugin.getLogger().fine(
                        "Unable to check for an EpicPlugin update: " + exception.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }
}
