package com.repsy.assignment.repository;

import com.repsy.assignment.model.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Dependency entity
 */
@Repository
public interface DependencyRepository extends JpaRepository<Dependency, Long> {
    
    /**
     * Find all dependencies for a specific package version
     * @param packageVersionId the package version ID
     * @return list of dependencies
     */
    List<Dependency> findByPackageVersionIdOrderByPackageNameAsc(Long packageVersionId);
    
    /**
     * Find all versions that depend on a specific package
     * @param packageName the package name
     * @return list of dependencies
     */
    List<Dependency> findByPackageNameOrderByCreatedAtDesc(String packageName);
    
    /**
     * Check if a specific dependency exists for a package version
     * @param packageVersionId the package version ID
     * @param packageName the package name
     * @param version the version string
     * @return true if exists, false otherwise
     */
    boolean existsByPackageVersionIdAndPackageNameAndVersion(Long packageVersionId, String packageName, String version);
    
    // TODO: Add methods for circular dependency detection
    // TODO: Add methods for dependency tree analysis
    // TODO: Add methods for dependency statistics
}
