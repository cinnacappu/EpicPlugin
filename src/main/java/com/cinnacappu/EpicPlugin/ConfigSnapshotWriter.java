package com.cinnacappu.EpicPlugin;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

final class ConfigSnapshotWriter {

    private final Path file;
    private final TemporaryWriter temporaryWriter;
    private long revision;
    private boolean dirty;

    ConfigSnapshotWriter(Path file) {
        this(file, (temporary, contents) -> Files.writeString(temporary, contents, StandardCharsets.UTF_8));
    }

    ConfigSnapshotWriter(Path file, TemporaryWriter temporaryWriter) {
        this.file = file;
        this.temporaryWriter = temporaryWriter;
    }

    synchronized void markDirty() {
        revision++;
        dirty = true;
    }

    synchronized long getRevision() {
        return revision;
    }

    synchronized boolean isDirty() {
        return dirty;
    }

    void saveSnapshot(String contents, long snapshotRevision) throws IOException {
        synchronized (this) {
            if (snapshotRevision != revision) return;
        }
        Path directory = file.toAbsolutePath().getParent();
        Files.createDirectories(directory);
        Path temporary = Files.createTempFile(directory, file.getFileName().toString(), ".tmp");
        try {
            temporaryWriter.write(temporary, contents);
            synchronized (this) {
                if (snapshotRevision != revision) return;
                try {
                    Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                } catch (AtomicMoveNotSupportedException ex) {
                    Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
                }
                dirty = false;
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    void saveNow(String contents) throws IOException {
        long snapshotRevision;
        synchronized (this) {
            snapshotRevision = ++revision;
            dirty = true;
        }
        saveSnapshot(contents, snapshotRevision);
    }

    @FunctionalInterface
    interface TemporaryWriter {
        void write(Path file, String contents) throws IOException;
    }
}
