package com.repsy.assignment.controller;

import com.repsy.assignment.dto.ApiResponse;
import com.repsy.assignment.dto.PackageVersionDTO;
import com.repsy.assignment.service.PackageVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for PackageVersion operations
 */
@RestController
@RequestMapping("/api/v1/packages/{packageId}/versions")
@RequiredArgsConstructor
public class PackageVersionController {

    private final PackageVersionService versionService;

    /**
     * Create a new version
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PackageVersionDTO>> createVersion(
            @PathVariable Long packageId,
            @RequestParam String version,
            @RequestParam String author) {
        try {
            PackageVersionDTO versionDTO = versionService.createVersion(packageId, version, author);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(versionDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get a specific version
     */
    @GetMapping("/{version}")
    public ResponseEntity<ApiResponse<PackageVersionDTO>> getVersion(
            @PathVariable Long packageId,
            @PathVariable String version) {
        try {
            PackageVersionDTO versionDTO = versionService.getVersion(packageId, version);
            return ResponseEntity.ok(ApiResponse.success(versionDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    /**
     * List all versions of a package
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PackageVersionDTO>>> getVersions(@PathVariable Long packageId) {
        try {
            List<PackageVersionDTO> versions = versionService.getVersions(packageId);
            return ResponseEntity.ok(ApiResponse.success(versions));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    /**
     * Delete a version
     */
    @DeleteMapping("/{versionId}")
    public ResponseEntity<ApiResponse<Void>> deleteVersion(
            @PathVariable Long packageId,
            @PathVariable Long versionId) {
        try {
            // Validate version belongs to package
            PackageVersionDTO version = versionService.getVersionById(versionId);
            versionService.deleteVersion(versionId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    /**
     * Check if version exists
     */
    @GetMapping("/exists/{version}")
    public ResponseEntity<ApiResponse<Boolean>> versionExists(
            @PathVariable Long packageId,
            @PathVariable String version) {
        boolean exists = versionService.versionExists(packageId, version);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
}
