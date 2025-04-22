package com.repsy.assignment.service;

import com.repsy.assignment.dto.DependencyDTO;
import java.util.List;

/**
 * Service interface for Dependency operations
 */
public interface DependencyService {
    
    /**
     * Add a dependency
     * @param versionId Version ID
     * @param packageName Dependent package name
     * @param version Required version
     * @return Created dependency
     */
    DependencyDTO addDependency(Long versionId, String packageName, String version);
    
    /**
     * List dependencies of a version
     * @param versionId Version ID
     * @return List of dependencies
     */
    List<DependencyDTO> getDependencies(Long versionId);
    
    /**
     * Find versions depending on a package
     * @param packageName Package name
     * @return List of dependencies
     */
    List<DependencyDTO> findDependents(String packageName);
    
    /**
     * Remove a dependency
     * @param id Dependency ID
     */
    void removeDependency(Long id);
    
    /**
     * Check if dependency exists
     * @param versionId Version ID
     * @param packageName Package name
     * @param version Version string
     * @return true if exists
     */
    boolean dependencyExists(Long versionId, String packageName, String version);
    
    /**
     * Check for circular dependencies
     * @param versionId Version ID
     * @param packageName Package to check
     * @return true if circular dependency would be created
     */
    boolean hasCircularDependency(Long versionId, String packageName);
}
