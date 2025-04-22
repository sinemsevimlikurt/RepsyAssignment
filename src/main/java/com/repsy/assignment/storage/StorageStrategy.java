package com.repsy.assignment.storage;

import java.io.InputStream;

/**
 * Interface for different storage strategies (file-system or object-storage)
 */
public interface StorageStrategy {
    
    /**
     * Store a file
     * @param packageName Package name
     * @param version Version string
     * @param fileName File name (package.rep or meta.json)
     * @param inputStream File content stream
     * @return Storage path or identifier
     */
    String store(String packageName, String version, String fileName, InputStream inputStream);
    
    /**
     * Retrieve a file
     * @param packageName Package name
     * @param version Version string
     * @param fileName File name (package.rep or meta.json)
     * @return File content stream
     */
    InputStream retrieve(String packageName, String version, String fileName);
    
    /**
     * Delete a file
     * @param packageName Package name
     * @param version Version string
     * @param fileName File name (package.rep or meta.json)
     */
    void delete(String packageName, String version, String fileName);
    
    /**
     * Check if a file exists
     * @param packageName Package name
     * @param version Version string
     * @param fileName File name (package.rep or meta.json)
     * @return true if exists
     */
    boolean exists(String packageName, String version, String fileName);
}
