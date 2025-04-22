package com.repsy.assignment.storage;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * MinIO (Object Storage) implementation of StorageStrategy
 */
@Component
@ConditionalOnProperty(name = "storageStrategy", havingValue = "object-storage")
public class ObjectStorageStrategy implements StorageStrategy {

    private final MinioClient minioClient;
    private final String bucketName;

    public ObjectStorageStrategy(
            @Value("${storage.minio.url}") String minioUrl,
            @Value("${storage.minio.access-key}") String accessKey,
            @Value("${storage.minio.secret-key}") String secretKey,
            @Value("${storage.minio.bucket}") String bucketName) {
        
        this.bucketName = bucketName;
        
        this.minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
        
        // Ensure bucket exists
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
                    
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MinIO bucket", e);
        }
    }

    @Override
    public String store(String packageName, String version, String fileName, InputStream inputStream) {
        try {
            String objectName = getObjectName(packageName, version, fileName);
            
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, -1, 10485760) // 10MB part size
                    .build());
            
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file in MinIO: " + fileName, e);
        }
    }

    @Override
    public InputStream retrieve(String packageName, String version, String fileName) {
        try {
            String objectName = getObjectName(packageName, version, fileName);
            
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve file from MinIO: " + fileName, e);
        }
    }

    @Override
    public void delete(String packageName, String version, String fileName) {
        try {
            String objectName = getObjectName(packageName, version, fileName);
            
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from MinIO: " + fileName, e);
        }
    }

    @Override
    public boolean exists(String packageName, String version, String fileName) {
        try {
            String objectName = getObjectName(packageName, version, fileName);
            
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getObjectName(String packageName, String version, String fileName) {
        return String.format("%s/%s/%s", packageName, version, fileName);
    }
}
