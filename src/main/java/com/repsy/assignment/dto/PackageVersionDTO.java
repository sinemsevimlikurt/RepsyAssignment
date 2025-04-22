package com.repsy.assignment.dto;

import lombok.Data;
import java.time.Instant;
import java.util.List;

/**
 * DTO for PackageVersion entity
 */
@Data
public class PackageVersionDTO {
    private Long id;
    private String version;
    private String author;
    private Instant createdAt;
    private Instant updatedAt;
    private int fileCount;
    private List<DependencyDTO> dependencies;
}
