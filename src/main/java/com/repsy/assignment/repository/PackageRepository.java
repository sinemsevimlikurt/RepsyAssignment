package com.repsy.assignment.repository;

import com.repsy.assignment.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Package entity
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    
    /**
     * Find a package by its name
     * @param name the package name
     * @return Optional containing the package if found
     */
    Optional<Package> findByName(String name);
    
    /**
     * Check if a package exists with the given name
     * @param name the package name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
    
    // TODO: Add custom query methods for package search
    // TODO: Add methods for package statistics
}
