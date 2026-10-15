package com.cinnacappu.EpicPlugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UpdateCheckerTest {
    @Test
    void onlyNewerPublishedVersionsTriggerAnUpdateWarning() {
        assertFalse(EpicPluginUpdateChecker.isNewer("4.0", "4.3.1"));
        assertFalse(EpicPluginUpdateChecker.isNewer("4.3.1", "4.3.1"));
        assertFalse(EpicPluginUpdateChecker.isNewer("4.3.1.0", "4.3.1"));
        assertTrue(EpicPluginUpdateChecker.isNewer("4.3.2", "4.3.1"));
        assertTrue(EpicPluginUpdateChecker.isNewer("4.10", "4.9"));
        assertFalse(EpicPluginUpdateChecker.isNewer("unavailable", "4.3.1"));
    }
}
