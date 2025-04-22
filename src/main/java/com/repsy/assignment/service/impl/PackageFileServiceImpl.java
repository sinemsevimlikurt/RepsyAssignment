package com.repsy.assignment.service.impl;

import com.repsy.assignment.dto.PackageFileDTO;
import com.repsy.assignment.model.PackageFile;
import com.repsy.assignment.model.PackageVersion;
import com.repsy.assignment.repository.PackageFileRepository;
import com.repsy.assignment.repository.PackageVersionRepository;
import com.repsy.assignment.service.PackageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of PackageFileService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PackageFileServiceImpl implements PackageFileService {

    private final PackageFileRepository fileRepository;
    private final PackageVersionRepository versionRepository;
    private static final String STORAGE_ROOT = "package-files"; // TODO: Make configurable

    @Override
    public PackageFileDTO uploadFile(Long versionId, MultipartFile file, String strategy) {
        // Validate version exists
        PackageVersion version = versionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        // Validate file name doesn't exist
        if (fileRepository.existsByPackageVersionIdAndFileName(versionId, file.getOriginalFilename())) {
            throw new IllegalArgumentException("File already exists: " + file.getOriginalFilename());
        }

        try {
            // Create storage path
            String storagePath = createStoragePath(version, file.getOriginalFilename());
            
            // Ensure directory exists
            Files.createDirectories(Paths.get(storagePath).getParent());
            
            // Save file
            Files.write(Paths.get(storagePath), file.getBytes());

            // Create file metadata
            PackageFile packageFile = new PackageFile();
            packageFile.setPackageVersion(version);
            packageFile.setFileName(file.getOriginalFilename());
            packageFile.setFileType(getFileType(file.getOriginalFilename()));
            packageFile.setStoragePath(storagePath);
            packageFile.setStorageStrategy(strategy);
            packageFile.setFileSize(file.getSize());
            packageFile.setContentHash(DigestUtils.md5DigestAsHex(file.getInputStream()));

            // Save and convert to DTO
            return convertToDTO(fileRepository.save(packageFile));
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PackageFileDTO getFile(Long id) {
        return fileRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("File not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackageFileDTO> getFiles(Long versionId) {
        return fileRepository.findByPackageVersionIdOrderByFileNameAsc(versionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] downloadFile(Long id) {
        PackageFile file = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File not found: " + id));
        
        try {
            return Files.readAllBytes(Paths.get(file.getStoragePath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + file.getFileName(), e);
        }
    }

    @Override
    public void deleteFile(Long id) {
        PackageFile file = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File not found: " + id));
        
        try {
            // Delete physical file
            Files.deleteIfExists(Paths.get(file.getStoragePath()));
            
            // Delete metadata
            fileRepository.delete(file);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + file.getFileName(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean fileExists(Long versionId, String fileName) {
        return fileRepository.existsByPackageVersionIdAndFileName(versionId, fileName);
    }

    /**
     * Convert PackageFile entity to DTO
     */
    private PackageFileDTO convertToDTO(PackageFile file) {
        PackageFileDTO dto = new PackageFileDTO();
        dto.setId(file.getId());
        dto.setFileName(file.getFileName());
        dto.setFileType(file.getFileType());
        dto.setStorageStrategy(file.getStorageStrategy());
        dto.setFileSize(file.getFileSize());
        dto.setContentHash(file.getContentHash());
        dto.setUploadedAt(file.getUploadedAt());
        return dto;
    }

    /**
     * Create storage path for a file
     */
    private String createStoragePath(PackageVersion version, String fileName) {
        return Path.of(STORAGE_ROOT,
                version.getPkg().getName(),
                version.getVersion(),
                fileName)
                .toString();
    }

    /**
     * Extract file type from file name
     */
    private String getFileType(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot + 1) : "";
    }
}
