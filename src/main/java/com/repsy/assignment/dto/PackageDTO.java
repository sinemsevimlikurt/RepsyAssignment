package com.repsy.assignment.dto;

import lombok.Data;
import java.time.Instant;

/**
 * DTO for Package entity
 */
@Data
public class PackageDTO {
    private Long id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private int versionCount;
}
