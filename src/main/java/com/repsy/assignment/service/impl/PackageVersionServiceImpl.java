package com.repsy.assignment.service.impl;

import com.repsy.assignment.dto.DependencyDTO;
import com.repsy.assignment.dto.PackageVersionDTO;
import com.repsy.assignment.model.Package;
import com.repsy.assignment.model.PackageVersion;
import com.repsy.assignment.repository.PackageRepository;
import com.repsy.assignment.repository.PackageVersionRepository;
import com.repsy.assignment.service.PackageVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of PackageVersionService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PackageVersionServiceImpl implements PackageVersionService {

    private final PackageVersionRepository versionRepository;
    private final PackageRepository packageRepository;

    @Override
    public PackageVersionDTO createVersion(Long packageId, String version, String author) {
        // Validate package exists
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found: " + packageId));

        // Validate version doesn't exist
        if (versionRepository.existsByPkgIdAndVersion(packageId, version)) {
            throw new IllegalArgumentException("Version " + version + " already exists for package: " + pkg.getName());
        }

        // Create new version
        PackageVersion packageVersion = new PackageVersion();
        packageVersion.setPkg(pkg);
        packageVersion.setVersion(version);
        packageVersion.setAuthor(author);

        // Save and convert to DTO
        return convertToDTO(versionRepository.save(packageVersion));
    }

    @Override
    @Transactional(readOnly = true)
    public PackageVersionDTO getVersion(Long packageId, String version) {
        return versionRepository.findByPkgIdAndVersion(packageId, version)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Version " + version + " not found for package: " + packageId));
    }

    @Override
    @Transactional(readOnly = true)
    public PackageVersionDTO getVersionById(Long id) {
        return versionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackageVersionDTO> getVersions(Long packageId) {
        // Validate package exists
        if (!packageRepository.existsById(packageId)) {
            throw new IllegalArgumentException("Package not found: " + packageId);
        }

        return versionRepository.findByPkgIdOrderByCreatedAtDesc(packageId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteVersion(Long id) {
        if (!versionRepository.existsById(id)) {
            throw new IllegalArgumentException("Version not found: " + id);
        }
        versionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean versionExists(Long packageId, String version) {
        return versionRepository.existsByPkgIdAndVersion(packageId, version);
    }

    /**
     * Convert PackageVersion entity to DTO
     */
    private PackageVersionDTO convertToDTO(PackageVersion version) {
        PackageVersionDTO dto = new PackageVersionDTO();
        dto.setId(version.getId());
        dto.setVersion(version.getVersion());
        dto.setAuthor(version.getAuthor());
        dto.setCreatedAt(version.getCreatedAt());
        dto.setUpdatedAt(version.getUpdatedAt());
        dto.setFileCount(version.getFiles().size());
        
        // Convert dependencies
        List<DependencyDTO> dependencies = version.getDependencies().stream()
                .map(dep -> {
                    DependencyDTO depDto = new DependencyDTO();
                    depDto.setId(dep.getId());
                    depDto.setPackageName(dep.getPackageName());
                    depDto.setVersion(dep.getVersion());
                    depDto.setCreatedAt(dep.getCreatedAt());
                    return depDto;
                })
                .collect(Collectors.toList());
        dto.setDependencies(dependencies);
        
        return dto;
    }
}
