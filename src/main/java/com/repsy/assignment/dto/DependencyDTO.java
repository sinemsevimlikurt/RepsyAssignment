package com.repsy.assignment.dto;

import lombok.Data;
import java.time.Instant;

/**
 * DTO for Dependency entity
 */
@Data
public class DependencyDTO {
    private Long id;
    private String packageName;
    private String version;
    private Instant createdAt;
}
