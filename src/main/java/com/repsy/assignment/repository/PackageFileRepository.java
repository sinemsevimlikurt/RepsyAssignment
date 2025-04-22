package com.repsy.assignment.repository;

import com.repsy.assignment.model.PackageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for PackageFile entity
 */
@Repository
public interface PackageFileRepository extends JpaRepository<PackageFile, Long> {
    
    /**
     * Find all files for a specific package version
     * @param packageVersionId the package version ID
     * @return list of package files
     */
    List<PackageFile> findByPackageVersionIdOrderByFileNameAsc(Long packageVersionId);
    
    /**
     * Find a specific file in a package version
     * @param packageVersionId the package version ID
     * @param fileName the file name
     * @return Optional containing the package file if found
     */
    Optional<PackageFile> findByPackageVersionIdAndFileName(Long packageVersionId, String fileName);
    
    /**
     * Check if a file exists in a package version
     * @param packageVersionId the package version ID
     * @param fileName the file name
     * @return true if exists, false otherwise
     */
    boolean existsByPackageVersionIdAndFileName(Long packageVersionId, String fileName);
    
    // TODO: Add methods for file type statistics
    // TODO: Add methods for storage usage analysis
}
