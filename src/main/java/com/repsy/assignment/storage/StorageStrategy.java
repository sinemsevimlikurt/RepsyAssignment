package com.repsy.assignment.storage;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * Interface for implementing different storage strategies
 */
public interface StorageStrategy {
    
    /**
     * Store a file in the storage system
     * @param packageName Package name
     * @param version Package version
     * @param fileName File name (package.rep or meta.json)
     * @param file File content
     * @return Storage path
     * @throws IOException If storage operation fails
     */
    String store(String packageName, String version, String fileName, MultipartFile file) throws IOException;
    
    /**
     * Retrieve a file from the storage system
     * @param packageName Package name
     * @param version Package version
     * @param fileName File name to retrieve
     * @return File content as byte array
     * @throws IOException If retrieval operation fails
     */
    byte[] retrieve(String packageName, String version, String fileName) throws IOException;
    
    /**
     * Check if a file exists in storage
     * @param packageName Package name
     * @param version Package version
     * @param fileName File name to check
     * @return true if file exists
     */
    boolean exists(String packageName, String version, String fileName);
    
    /**
     * Delete a file from storage
     * @param packageName Package name
     * @param version Package version
     * @param fileName File name to delete
     * @throws IOException If deletion operation fails
     */
    void delete(String packageName, String version, String fileName) throws IOException;
}
