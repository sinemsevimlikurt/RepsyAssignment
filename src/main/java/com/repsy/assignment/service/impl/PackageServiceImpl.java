package com.repsy.assignment.service.impl;

import com.repsy.assignment.dto.PackageDTO;
import com.repsy.assignment.model.Package;
import com.repsy.assignment.repository.PackageRepository;
import com.repsy.assignment.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of PackageService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PackageServiceImpl implements PackageService {
    
    private final PackageRepository packageRepository;
    
    @Override
    public PackageDTO createPackage(String name) {
        // Validate name
        if (packageRepository.existsByName(name)) {
            throw new IllegalArgumentException("Package with name " + name + " already exists");
        }
        
        // Create new package
        Package pkg = new Package();
        pkg.setName(name);
        
        // Save and convert to DTO
        return convertToDTO(packageRepository.save(pkg));
    }
    
    @Override
    @Transactional(readOnly = true)
    public PackageDTO getPackageById(Long id) {
        return packageRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Package not found: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public PackageDTO getPackageByName(String name) {
        return packageRepository.findByName(name)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Package not found: " + name));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PackageDTO> getAllPackages() {
        return packageRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deletePackage(Long id) {
        if (!packageRepository.existsById(id)) {
            throw new IllegalArgumentException("Package not found: " + id);
        }
        packageRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean packageExists(String name) {
        return packageRepository.existsByName(name);
    }
    
    /**
     * Convert Package entity to DTO
     */
    private PackageDTO convertToDTO(Package pkg) {
        PackageDTO dto = new PackageDTO();
        dto.setId(pkg.getId());
        dto.setName(pkg.getName());
        dto.setCreatedAt(pkg.getCreatedAt());
        dto.setUpdatedAt(pkg.getUpdatedAt());
        dto.setVersionCount(pkg.getVersions().size());
        return dto;
    }
}
