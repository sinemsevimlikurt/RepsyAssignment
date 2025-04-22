package com.repsy.assignment.service;

import com.repsy.assignment.dto.PackageVersionDTO;
import java.util.List;

/**
 * Service interface for PackageVersion operations
 */
public interface PackageVersionService {
    
    /**
     * Create a new package version
     * @param packageId Package ID
     * @param version Version string
     * @param author Author name
     * @return Created version
     */
    PackageVersionDTO createVersion(Long packageId, String version, String author);
    
    /**
     * Get a specific version
     * @param packageId Package ID
     * @param version Version string
     * @return Version if found
     */
    PackageVersionDTO getVersion(Long packageId, String version);
    
    /**
     * Get version by ID
     * @param id Version ID
     * @return Version if found
     */
    PackageVersionDTO getVersionById(Long id);
    
    /**
     * List all versions of a package
     * @param packageId Package ID
     * @return List of versions
     */
    List<PackageVersionDTO> getVersions(Long packageId);
    
    /**
     * Delete a version
     * @param id Version ID
     */
    void deleteVersion(Long id);
    
    /**
     * Check if version exists
     * @param packageId Package ID
     * @param version Version string
     * @return true if exists
     */
    boolean versionExists(Long packageId, String version);
}
