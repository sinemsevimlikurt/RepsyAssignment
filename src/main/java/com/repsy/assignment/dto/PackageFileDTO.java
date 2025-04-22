package com.repsy.assignment.dto;

import lombok.Data;
import java.time.Instant;

/**
 * DTO for PackageFile entity
 */
@Data
public class PackageFileDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private String storageStrategy;
    private Long fileSize;
    private String contentHash;
    private Instant uploadedAt;
}
