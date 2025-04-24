package com.repsy.assignment.storage;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * Interface for implementing different storage strategies
 */
public interface StorageStrategy {
    /**
     * Store a file in the storage system
     */
    String store(String packageName, String version, String fileName, MultipartFile file) throws IOException;

    /**
     * Retrieve a file from the storage system
     */
    byte[] retrieve(String packageName, String version, String fileName) throws IOException;

    /**
     * Check if a file exists in storage
     */
    boolean exists(String packageName, String version, String fileName);

    /**
     * Delete a file from storage
     */
    void delete(String packageName, String version, String fileName) throws IOException;
}
