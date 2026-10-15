package com.cinnacappu.EpicPlugin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CommandGodTest {

    @Test
    void identifiesOnlyTheGodEffectWithItsExpectedRemainingDuration() {
        assertTrue(CommandGod.isGodEffect(5, true, false, false, Integer.MAX_VALUE - 200, 200));
        assertFalse(CommandGod.isGodEffect(6, true, false, false, Integer.MAX_VALUE - 200, 200));
        assertFalse(CommandGod.isGodEffect(5, false, false, false, Integer.MAX_VALUE - 200, 200));
        assertFalse(CommandGod.isGodEffect(5, true, true, false, Integer.MAX_VALUE - 200, 200));
        assertFalse(CommandGod.isGodEffect(5, true, false, true, Integer.MAX_VALUE - 200, 200));
        assertFalse(CommandGod.isGodEffect(5, true, false, false, 1000, 200));
    }

    @Test
    void doesNotClaimReplacementEffectsAfterTicksReset() {
        assertFalse(CommandGod.isGodEffect(5, true, false, false, Integer.MAX_VALUE - 200, -100));
        assertEquals(0, CommandGod.remainingDuration(1000, -100));
    }

    @Test
    void restoresOnlyRemainingTicksOfThePriorEffect() {
        assertEquals(800, CommandGod.remainingDuration(1000, 200));
        assertEquals(0, CommandGod.remainingDuration(1000, 1000));
        assertEquals(0, CommandGod.remainingDuration(1000, 2000));
    }

    @Test
    void preservesInfinitePriorDuration() {
        assertEquals(-1, CommandGod.remainingDuration(-1, 2000));
    }
}
