package it.unicam.cs.mpgc.rpg131023.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Concrete implementation of StorageService that interacts with the local file system.
 */
public class FileStorageService implements StorageService {

    @Override
    public void write(String path, String data) throws IOException {
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directories for path: " + path);
            }
        }
        Files.writeString(Paths.get(path), data);
    }

    @Override
    public String read(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }
}
