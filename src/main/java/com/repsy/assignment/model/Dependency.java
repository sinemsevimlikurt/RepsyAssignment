package com.repsy.assignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

/**
 * Entity representing a dependency of a package version
 */
@Entity
@Getter
@Setter
@Table(name = "dependencies")
public class Dependency {
    
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Name of the required package
    @Column(name = "package_name", nullable = false)
    private String packageName;
    
    // Required version
    @Column(name = "version", nullable = false)
    private String version;
    
    // Creation timestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    
    // Many-to-one relationship with package version
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_version_id", nullable = false)
    private PackageVersion packageVersion;
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
    
    // TODO: Add version constraint validation methods
    // TODO: Add circular dependency detection methods
    // TODO: Add toString, equals, and hashCode methods if needed
}
