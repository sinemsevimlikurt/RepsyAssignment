package com.repsy.assignment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

/**
 * Entity representing a file within a package version
 */
@Entity
@Getter
@Setter
@Table(name = "package_files")
public class PackageFile {
    
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // File name
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    // File type/extension
    @Column(name = "file_type", nullable = false)
    private String fileType;
    
    // Path where the file is stored
    @Column(name = "storage_path", nullable = false)
    private String storagePath;
    
    // Storage strategy (e.g., local, s3, etc.)
    @Column(name = "storage_strategy", nullable = false)
    private String storageStrategy;
    
    // File size in bytes
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    // Hash of file contents for integrity
    @Column(name = "content_hash", nullable = false)
    private String contentHash;
    
    // Upload timestamp
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private Instant uploadedAt;
    
    // Many-to-one relationship with package version
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_version_id", nullable = false)
    private PackageVersion packageVersion;
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        uploadedAt = Instant.now();
    }
    
    // TODO: Add file validation methods
    // TODO: Add hash computation methods
    // TODO: Add file type detection methods
    // TODO: Add toString, equals, and hashCode methods if needed
}
