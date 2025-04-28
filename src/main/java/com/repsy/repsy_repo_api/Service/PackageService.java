package com.repsy.repsy_repo_api.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.repsy_repo_api.Domain.PackageMetadata;
import com.repsy.repsy_repo_api.Repository.PackageMetadataRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class PackageService {

    private final PackageMetadataRepository repository;
    private final ObjectMapper objectMapper;

    public PackageService(PackageMetadataRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
    }

    public void upload(String packageName, String version,
                       MultipartFile metaFile, MultipartFile packageFile) throws IOException {

        // 1. meta.json içeriğini oku
        PackageMetadata metadata = objectMapper.readValue(metaFile.getInputStream(), PackageMetadata.class);

        // 2. package.rep dosyasını oku ve modele ekle
        metadata.setPackageFile(packageFile.getBytes());

        // 3. PostgreSQL'e kaydet
        repository.save(metadata);
    }

    public byte[] download(String packageName, String version, String fileName) throws IOException {
        Optional<PackageMetadata> optional = repository.findAll().stream()
                .filter(m -> m.getName().equals(packageName) && m.getVersion().equals(version))
                .findFirst();

        if (optional.isEmpty()) {
            throw new IOException("Package not found");
        }

        PackageMetadata metadata = optional.get();

        if ("package.rep".equals(fileName)) {
            return metadata.getPackageFile();
        } else if ("meta.json".equals(fileName)) {
            return objectMapper.writeValueAsBytes(metadata);
        } else {
            throw new IOException("Invalid file name: " + fileName);
        }
    }
}