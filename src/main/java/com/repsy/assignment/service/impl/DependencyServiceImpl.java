package com.repsy.assignment.service.impl;

import com.repsy.assignment.dto.DependencyDTO;
import com.repsy.assignment.model.Dependency;
import com.repsy.assignment.model.PackageVersion;
import com.repsy.assignment.repository.DependencyRepository;
import com.repsy.assignment.repository.PackageVersionRepository;
import com.repsy.assignment.service.DependencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of DependencyService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DependencyServiceImpl implements DependencyService {

    private final DependencyRepository dependencyRepository;
    private final PackageVersionRepository versionRepository;

    @Override
    public DependencyDTO addDependency(Long versionId, String packageName, String version) {
        // Validate version exists
        PackageVersion packageVersion = versionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        // Validate dependency doesn't exist
        if (dependencyRepository.existsByPackageVersionIdAndPackageNameAndVersion(versionId, packageName, version)) {
            throw new IllegalArgumentException("Dependency already exists: " + packageName + ":" + version);
        }

        // Check for circular dependency
        if (hasCircularDependency(versionId, packageName)) {
            throw new IllegalArgumentException("Circular dependency detected with package: " + packageName);
        }

        // Create dependency
        Dependency dependency = new Dependency();
        dependency.setPackageVersion(packageVersion);
        dependency.setPackageName(packageName);
        dependency.setVersion(version);

        // Save and convert to DTO
        return convertToDTO(dependencyRepository.save(dependency));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DependencyDTO> getDependencies(Long versionId) {
        return dependencyRepository.findByPackageVersionIdOrderByPackageNameAsc(versionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DependencyDTO> findDependents(String packageName) {
        return dependencyRepository.findByPackageNameOrderByCreatedAtDesc(packageName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void removeDependency(Long id) {
        if (!dependencyRepository.existsById(id)) {
            throw new IllegalArgumentException("Dependency not found: " + id);
        }
        dependencyRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean dependencyExists(Long versionId, String packageName, String version) {
        return dependencyRepository.existsByPackageVersionIdAndPackageNameAndVersion(versionId, packageName, version);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasCircularDependency(Long versionId, String packageName) {
        PackageVersion version = versionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));
        
        // If the dependent package is the same as the current package, it's circular
        if (version.getPkg().getName().equals(packageName)) {
            return true;
        }
        
        // Use a set to track visited packages
        Set<String> visited = new HashSet<>();
        visited.add(version.getPkg().getName());
        
        return checkCircularDependency(packageName, visited);
    }

    /**
     * Recursive helper method to check for circular dependencies
     */
    private boolean checkCircularDependency(String packageName, Set<String> visited) {
        // If we've seen this package before, we have a circular dependency
        if (visited.contains(packageName)) {
            return true;
        }
        
        // Add current package to visited set
        visited.add(packageName);
        
        // Get all dependencies of the current package
        List<Dependency> dependencies = dependencyRepository.findByPackageNameOrderByCreatedAtDesc(packageName);
        
        // Recursively check each dependency
        for (Dependency dependency : dependencies) {
            if (checkCircularDependency(dependency.getPackageName(), new HashSet<>(visited))) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Convert Dependency entity to DTO
     */
    private DependencyDTO convertToDTO(Dependency dependency) {
        DependencyDTO dto = new DependencyDTO();
        dto.setId(dependency.getId());
        dto.setPackageName(dependency.getPackageName());
        dto.setVersion(dependency.getVersion());
        dto.setCreatedAt(dependency.getCreatedAt());
        return dto;
    }
}
