package com.repsy.assignment.storage;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * MinIO implementation of StorageStrategy
 */
@Component
@ConditionalOnProperty(name = "storageStrategy", havingValue = "minio")
public class MinioStorageStrategy implements StorageStrategy {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public String store(String packageName, String version, String fileName, MultipartFile file) throws IOException {
        String objectName = packageName + "/" + version + "/" + fileName;
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(new ByteArrayInputStream(file.getBytes()), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            return objectName;
        } catch (Exception e) {
            throw new IOException("Failed to upload file to MinIO", e);
        }
    }

    @Override
    public byte[] retrieve(String packageName, String version, String fileName) throws IOException {
        String objectName = packageName + "/" + version + "/" + fileName;
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new IOException("Failed to retrieve file from MinIO", e);
        }
    }

    @Override
    public boolean exists(String packageName, String version, String fileName) {
        String objectName = packageName + "/" + version + "/" + fileName;
        try {
            minioClient.statObject(
                    io.minio.StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void delete(String packageName, String version, String fileName) throws IOException {
        String objectName = packageName + "/" + version + "/" + fileName;
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new IOException("Failed to delete file from MinIO", e);
        }
    }
}
