package com.cinnacappu.EpicPlugin;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Proxy;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

class CommandSuperuserDoTest {

    @Test
    void removesTemporaryOpImmediatelyWhenCommandReturns() {
        PlayerState state = new PlayerState(false);
        CommandSuperuserDo.runWithOp(state.player, () -> assertTrue(state.op));
        assertFalse(state.op);
    }

    @Test
    void restoresNonOpEvenWhenCommandThrows() {
        PlayerState state = new PlayerState(false);
        assertThrows(IllegalStateException.class, () -> CommandSuperuserDo.runWithOp(state.player, () -> {
            assertTrue(state.op);
            throw new IllegalStateException("Command failed");
        }));
        assertFalse(state.op);
    }

    @Test
    void restoresOriginalOpEvenWhenCommandDeopsThePlayer() {
        PlayerState state = new PlayerState(true);
        CommandSuperuserDo.runWithOp(state.player, () -> state.op = false);
        assertTrue(state.op);
    }

    @Test
    void restoresBothNestedPlayersWhenDispatchFails() {
        PlayerState issuer = new PlayerState(false);
        PlayerState target = new PlayerState(false);
        assertThrows(AssertionError.class, () -> CommandSuperuserDo.runWithOp(issuer.player,
                () -> CommandSuperuserDo.runWithOp(target.player, () -> {
                    assertTrue(issuer.op);
                    assertTrue(target.op);
                    throw new AssertionError("Dispatch interrupted");
                })));
        assertFalse(issuer.op);
        assertFalse(target.op);
    }

    @Test
    void nestedSudoForTheSamePlayerRestoresTheOuterPrivilegeState() {
        PlayerState state = new PlayerState(false);
        CommandSuperuserDo.runWithOp(state.player, () -> {
            CommandSuperuserDo.runWithOp(state.player, () -> assertTrue(state.op));
            assertTrue(state.op);
        });
        assertFalse(state.op);
    }

    private static final class PlayerState {
        private boolean op;
        private final Player player;

        private PlayerState(boolean op) {
            this.op = op;
            player = (Player) Proxy.newProxyInstance(Player.class.getClassLoader(), new Class<?>[]{Player.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("isOp")) return this.op;
                        if (method.getName().equals("setOp")) {
                            this.op = (boolean) args[0];
                            return null;
                        }
                        throw new UnsupportedOperationException(method.getName());
                    });
        }
    }
}
