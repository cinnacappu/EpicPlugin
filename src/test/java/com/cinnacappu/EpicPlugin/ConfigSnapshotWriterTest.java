package com.cinnacappu.EpicPlugin;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConfigSnapshotWriterTest {

    @TempDir
    Path directory;

    @Test
    void passwordSaveSupersedesAnOlderLectureSnapshot() throws IOException {
        Path file = directory.resolve("config.yml");
        ConfigSnapshotWriter writer = new ConfigSnapshotWriter(file);
        writer.markDirty();
        long lectureRevision = writer.getRevision();
        writer.saveNow("sudo-password-hash: new\nsudo-lectured: [player]\n");
        writer.saveSnapshot("sudo-password-hash: old\nsudo-lectured: [player]\n", lectureRevision);
        assertEquals("sudo-password-hash: new\nsudo-lectured: [player]\n", Files.readString(file));
        assertFalse(writer.isDirty());
    }

    @Test
    void dirtyStateSurvivesStaleSnapshotsUntilLatestStateIsSaved() throws IOException {
        ConfigSnapshotWriter writer = new ConfigSnapshotWriter(directory.resolve("config.yml"));
        writer.markDirty();
        long previous = writer.getRevision();
        writer.markDirty();
        writer.saveSnapshot("previous", previous);
        assertTrue(writer.isDirty());
        assertFalse(Files.exists(directory.resolve("config.yml")));
        writer.saveSnapshot("latest", writer.getRevision());
        assertEquals("latest", Files.readString(directory.resolve("config.yml")));
        assertFalse(writer.isDirty());
    }

    @Test
    void concurrentStaleWritesCannotReplaceShutdownFlush() throws Exception {
        Path file = directory.resolve("config.yml");
        ConfigSnapshotWriter writer = new ConfigSnapshotWriter(file);
        writer.markDirty();
        long oldRevision = writer.getRevision();
        try (var executor = Executors.newFixedThreadPool(4)) {
            List<Future<?>> writes = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                writes.add(executor.submit(() -> {
                    writer.saveSnapshot("old", oldRevision);
                    return null;
                }));
            }
            writer.saveNow("latest");
            for (Future<?> write : writes) write.get();
        }
        assertEquals("latest", Files.readString(file));
        assertFalse(writer.isDirty());
        try (var files = Files.list(directory)) {
            assertEquals(1, files.count());
        }
    }

    @Test
    void failedSaveRemainsDirtyAndCanBeRetried() throws IOException {
        Path parent = directory.resolve("blocked");
        Files.writeString(parent, "existing");
        ConfigSnapshotWriter writer = new ConfigSnapshotWriter(parent.resolve("config.yml"));
        assertThrows(IOException.class, () -> writer.saveNow("latest"));
        assertTrue(writer.isDirty());
        assertEquals("existing", Files.readString(parent));
        Files.delete(parent);
        writer.saveSnapshot("latest", writer.getRevision());
        assertEquals("latest", Files.readString(parent.resolve("config.yml")));
        assertFalse(writer.isDirty());
    }

    @Test
    void slowOldSnapshotDoesNotBlockChangesOrReplacePasswordOrShutdownSave() throws Exception {
        Path file = directory.resolve("config.yml");
        CountDownLatch writing = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        ConfigSnapshotWriter writer = new ConfigSnapshotWriter(file, (temporary, contents) -> {
            if (contents.equals("old")) {
                writing.countDown();
                try {
                    if (!release.await(5, TimeUnit.SECONDS)) throw new IOException("Test write timed out");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new IOException(ex);
                }
            }
            Files.writeString(temporary, contents);
        });
        writer.markDirty();
        long oldRevision = writer.getRevision();
        try (var executor = Executors.newSingleThreadExecutor()) {
            Future<?> oldSave = executor.submit(() -> {
                writer.saveSnapshot("old", oldRevision);
                return null;
            });
            try {
                assertTrue(writing.await(5, TimeUnit.SECONDS));
                assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
                    writer.markDirty();
                    writer.saveNow("latest password and lecture history");
                });
                assertEquals("latest password and lecture history", Files.readString(file));
            } finally {
                release.countDown();
            }
            oldSave.get();
        }
        assertEquals("latest password and lecture history", Files.readString(file));
        assertFalse(writer.isDirty());
        try (var files = Files.list(directory)) {
            assertEquals(1, files.count());
        }
    }
}
