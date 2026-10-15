package com.cinnacappu.EpicPlugin;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Proxy;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.jupiter.api.Test;

class CommandFlyTest {

    @Test
    void quittingRemovesFlightAndResetsTheToggleForRejoining() {
        CommandFly command = new CommandFly();
        PlayerState state = new PlayerState(GameMode.SURVIVAL);
        command.onCommand(state.player, null, "fly", new String[0]);
        assertTrue(state.allowFlight);
        state.flying = true;
        command.onQuit(new PlayerQuitEvent(state.player, ""));
        assertFalse(state.allowFlight);
        assertFalse(state.flying);
        command.onCommand(state.player, null, "fly", new String[0]);
        assertTrue(state.allowFlight);
    }

    @Test
    void disablingPluginFlightDoesNotRemoveCreativeFlight() {
        CommandFly command = new CommandFly();
        PlayerState state = new PlayerState(GameMode.CREATIVE);
        command.onCommand(state.player, null, "fly", new String[0]);
        state.flying = true;
        command.onCommand(state.player, null, "fly", new String[0]);
        assertTrue(state.allowFlight);
        assertTrue(state.flying);
    }

    @Test
    void quittingDoesNotRemoveSpectatorFlight() {
        CommandFly command = new CommandFly();
        PlayerState state = new PlayerState(GameMode.SPECTATOR);
        command.onCommand(state.player, null, "fly", new String[0]);
        state.flying = true;
        command.onQuit(new PlayerQuitEvent(state.player, ""));
        assertTrue(state.allowFlight);
        assertTrue(state.flying);
    }

    @Test
    void togglingOffPreservesFlightPreviouslyGrantedByAnotherPlugin() {
        CommandFly command = new CommandFly();
        PlayerState state = new PlayerState(GameMode.SURVIVAL);
        state.allowFlight = true;
        state.flying = true;
        command.onCommand(state.player, null, "fly", new String[0]);
        command.onCommand(state.player, null, "fly", new String[0]);
        assertTrue(state.allowFlight);
        assertTrue(state.flying);
    }

    @Test
    void quittingPreservesFlightPreviouslyGrantedByAnotherPlugin() {
        CommandFly command = new CommandFly();
        PlayerState state = new PlayerState(GameMode.SURVIVAL);
        state.allowFlight = true;
        command.onCommand(state.player, null, "fly", new String[0]);
        command.onQuit(new PlayerQuitEvent(state.player, ""));
        assertTrue(state.allowFlight);
    }

    private static final class PlayerState {
        private final UUID uuid = UUID.randomUUID();
        private final Player player;
        private boolean allowFlight;
        private boolean flying;

        private PlayerState(GameMode gameMode) {
            allowFlight = gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR;
            player = (Player) Proxy.newProxyInstance(Player.class.getClassLoader(), new Class<?>[]{Player.class},
                    (proxy, method, args) -> {
                        return switch (method.getName()) {
                            case "getUniqueId" -> uuid;
                            case "hasPermission" -> true;
                            case "getGameMode" -> gameMode;
                            case "getAllowFlight" -> allowFlight;
                            case "sendMessage" -> null;
                            case "setAllowFlight" -> {
                                allowFlight = (boolean) args[0];
                                yield null;
                            }
                            case "setFlying" -> {
                                flying = (boolean) args[0];
                                yield null;
                            }
                            default -> throw new UnsupportedOperationException(method.getName());
                        };
                    });
        }
    }
}
