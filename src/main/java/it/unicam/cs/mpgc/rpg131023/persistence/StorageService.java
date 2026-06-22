package it.unicam.cs.mpgc.rpg131023.persistence;

import java.io.IOException;

/**
 * Abstraction for basic input/output string storage operations.
 */
public interface StorageService {
    
    /**
     * Writes the given data to the specified path.
     *
     * @param path The destination path.
     * @param data The data to write.
     * @throws IOException If an I/O error occurs.
     */
    void write(String path, String data) throws IOException;

    /**
     * Reads data from the specified path.
     *
     * @param path The source path.
     * @return The read data.
     * @throws IOException If an I/O error occurs.
     */
    String read(String path) throws IOException;
}
