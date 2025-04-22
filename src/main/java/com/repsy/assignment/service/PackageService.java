package com.repsy.assignment.service;

import com.repsy.assignment.dto.PackageDTO;
import java.util.List;

/**
 * Service interface for Package operations
 */
public interface PackageService {
    
    /**
     * Create a new package
     * @param name Package name
     * @return Created package
     */
    PackageDTO createPackage(String name);
    
    /**
     * Get a package by ID
     * @param id Package ID
     * @return Package if found
     */
    PackageDTO getPackageById(Long id);
    
    /**
     * Get a package by name
     * @param name Package name
     * @return Package if found
     */
    PackageDTO getPackageByName(String name);
    
    /**
     * List all packages
     * @return List of packages
     */
    List<PackageDTO> getAllPackages();
    
    /**
     * Delete a package
     * @param id Package ID
     */
    void deletePackage(Long id);
    
    /**
     * Check if a package exists
     * @param name Package name
     * @return true if exists
     */
    boolean packageExists(String name);
}
