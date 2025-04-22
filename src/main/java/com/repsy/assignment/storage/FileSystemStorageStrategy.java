package com.repsy.assignment.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File system implementation of StorageStrategy
 */
@Component
@ConditionalOnProperty(name = "storageStrategy", havingValue = "file-system")
public class FileSystemStorageStrategy implements StorageStrategy {

    @Value("${storage.filesystem.root:./storage}")
    private String storageRoot;

    @Override
    public String store(String packageName, String version, String fileName, InputStream inputStream) {
        try {
            Path filePath = getFilePath(packageName, version, fileName);
            
            // Create directories if they don't exist
            Files.createDirectories(filePath.getParent());
            
            // Copy file content
            Files.copy(inputStream, filePath);
            
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + fileName, e);
        }
    }

    @Override
    public InputStream retrieve(String packageName, String version, String fileName) {
        try {
            Path filePath = getFilePath(packageName, version, fileName);
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve file: " + fileName, e);
        }
    }

    @Override
    public void delete(String packageName, String version, String fileName) {
        try {
            Path filePath = getFilePath(packageName, version, fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + fileName, e);
        }
    }

    @Override
    public boolean exists(String packageName, String version, String fileName) {
        Path filePath = getFilePath(packageName, version, fileName);
        return Files.exists(filePath);
    }

    private Path getFilePath(String packageName, String version, String fileName) {
        return Paths.get(storageRoot, packageName, version, fileName);
    }
}
