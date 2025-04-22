package com.repsy.assignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a specific version of a package
 */
@Entity
@Getter
@Setter
@Table(name = "package_versions")
public class PackageVersion {
    
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Version string
    @Column(name = "version", nullable = false)
    private String version;
    
    // Author of this version
    @Column(name = "author", nullable = false)
    private String author;
    
    // Creation timestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    
    // Last update timestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
    
    // Many-to-one relationship with package
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Package pkg;
    
    // One-to-many relationship with package files
    @OneToMany(mappedBy = "packageVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageFile> files = new ArrayList<>();
    
    // One-to-many relationship with dependencies
    @OneToMany(mappedBy = "packageVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dependency> dependencies = new ArrayList<>();
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
    
    // TODO: Add version comparison methods
    // TODO: Add dependency management methods
    // TODO: Add validation methods
    // TODO: Add toString, equals, and hashCode methods if needed
}
