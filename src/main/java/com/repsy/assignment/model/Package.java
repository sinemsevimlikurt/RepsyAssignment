package com.repsy.assignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a package in the system
 */
@Entity
@Getter
@Setter
@Table(name = "packages")
public class Package {
    
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Unique package name
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    
    // Creation timestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    
    // Last update timestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
    
    // One-to-many relationship with package versions
    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageVersion> versions = new ArrayList<>();
    
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
    
    // TODO: Add business methods if needed
    // TODO: Add validation methods
    // TODO: Add toString, equals, and hashCode methods if needed
}
