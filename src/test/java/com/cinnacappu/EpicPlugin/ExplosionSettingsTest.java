package com.cinnacappu.EpicPlugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExplosionSettingsTest {
    @Test
    void defaultPowerIsPreservedAndCooldownIsSharedAcrossRequests() {
        ExplosionSettings settings = new ExplosionSettings();
        assertEquals(100.0F, settings.acquire(100.0F, 100.0, 5.0, 1));
        assertTrue(Float.isNaN(settings.acquire(10.0F, 100.0, 5.0, 2)));
        assertEquals(10.0F, settings.acquire(10.0F, 100.0, 5.0, 5_000_000_001L));
    }

    @Test
    void configuredLimitAndDisabledModeDoNotEnlargeExplosions() {
        ExplosionSettings settings = new ExplosionSettings();
        assertEquals(4.0F, settings.acquire(100.0F, 4.0, 0, 1));
        assertEquals(100.0F, settings.acquire(100.0F, 1000.0, 0, 2));
        assertEquals(0.0F, settings.acquire(100.0F, 0.0, 0, 3));
    }

    @Test
    void nonFiniteConfigurationFallsBackToBoundedDefaults() {
        ExplosionSettings settings = new ExplosionSettings();
        assertEquals(100.0F, settings.acquire(100.0F, Double.NaN, Double.POSITIVE_INFINITY, 1));
        assertTrue(Float.isNaN(settings.acquire(100.0F, 100.0, 5.0, 2)));
    }
}
