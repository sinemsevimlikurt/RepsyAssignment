package com.repsy.assignment.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File system implementation of StorageStrategy
 */
@Component
@ConditionalOnProperty(name = "storageStrategy", havingValue = "file-system")
public class FileSystemStorageStrategy implements StorageStrategy {

    @Value("${storage.filesystem.root:/tmp/repsy-storage}")
    private String storageRoot;

    @Override
    public String store(String packageName, String version, String fileName, MultipartFile file) throws IOException {
        Path storagePath = createStoragePath(packageName, version, fileName);
        Files.createDirectories(storagePath.getParent());
        Files.write(storagePath, file.getBytes());
        return storagePath.toString();
    }

    @Override
    public byte[] retrieve(String packageName, String version, String fileName) throws IOException {
        Path storagePath = createStoragePath(packageName, version, fileName);
        if (!Files.exists(storagePath)) {
            throw new IOException("File not found: " + storagePath);
        }
        return Files.readAllBytes(storagePath);
    }

    @Override
    public boolean exists(String packageName, String version, String fileName) {
        return Files.exists(createStoragePath(packageName, version, fileName));
    }

    @Override
    public void delete(String packageName, String version, String fileName) throws IOException {
        Path storagePath = createStoragePath(packageName, version, fileName);
        Files.deleteIfExists(storagePath);
        Path versionPath = storagePath.getParent();
        Path packagePath = versionPath.getParent();
        if (Files.isDirectory(versionPath) && isDirectoryEmpty(versionPath)) {
            Files.delete(versionPath);
            if (Files.isDirectory(packagePath) && isDirectoryEmpty(packagePath)) {
                Files.delete(packagePath);
            }
        }
    }
    /**
     * Create storage path for a file
     */
    private Path createStoragePath(String packageName, String version, String fileName) {
        return Paths.get(storageRoot, packageName, version, fileName);
    }
    /**
     * Check if a directory is empty
     */
    private boolean isDirectoryEmpty(Path path) throws IOException {
        try (var entries = Files.list(path)) {
            return entries.findFirst().isEmpty();
        }
    }
}
