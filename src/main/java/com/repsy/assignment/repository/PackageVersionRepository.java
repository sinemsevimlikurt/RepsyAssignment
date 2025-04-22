package com.repsy.assignment.repository;

import com.repsy.assignment.model.PackageVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for PackageVersion entity
 */
@Repository
public interface PackageVersionRepository extends JpaRepository<PackageVersion, Long> {
    
    /**
     * Find all versions of a package by package ID
     * @param packageId the package ID
     * @return list of package versions
     */
    List<PackageVersion> findByPkgIdOrderByCreatedAtDesc(Long packageId);
    
    /**
     * Find a specific version of a package
     * @param packageId the package ID
     * @param version the version string
     * @return Optional containing the package version if found
     */
    Optional<PackageVersion> findByPkgIdAndVersion(Long packageId, String version);
    
    /**
     * Check if a specific version exists for a package
     * @param packageId the package ID
     * @param version the version string
     * @return true if exists, false otherwise
     */
    boolean existsByPkgIdAndVersion(Long packageId, String version);
    
    // TODO: Add methods for version comparison
    // TODO: Add methods for dependency analysis
    // TODO: Add methods for version statistics
}
