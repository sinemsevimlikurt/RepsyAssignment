package com.repsy.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.assignment.dto.ApiResponse;
import com.repsy.assignment.model.Package;
import com.repsy.assignment.model.PackageVersion;
import com.repsy.assignment.service.PackageService;
import com.repsy.assignment.service.PackageVersionService;
import com.repsy.assignment.storage.StorageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller for package deployment operations
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeploymentController {

    private final StorageStrategy storageStrategy;
    private final PackageService packageService;
    private final PackageVersionService versionService;
    private final ObjectMapper objectMapper;

    /**
     * Deploy a package version with its metadata
     */
    @PostMapping(value = "/{packageName}/{version}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PackageVersion>> deployPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("package.rep") MultipartFile packageFile,
            @RequestParam("meta.json") MultipartFile metaFile) {
        
        try {
            // Validate file extensions
            if (!packageFile.getOriginalFilename().endsWith(".rep")) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Package file must have .rep extension"));
            }

            if (!metaFile.getOriginalFilename().endsWith(".json")) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Metadata file must have .json extension"));
            }

            // Store files
            storageStrategy.store(packageName, version, "package.rep", packageFile.getInputStream());
            storageStrategy.store(packageName, version, "meta.json", metaFile.getInputStream());

            // Create or get package
            Package pkg = packageService.getPackageByName(packageName);
            if (pkg == null) {
                pkg = packageService.createPackage(packageName);
            }

            // Create version
            PackageVersion packageVersion = versionService.createVersion(pkg.getId(), version, "system");

            return ResponseEntity.ok(ApiResponse.success(packageVersion));
            
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to process uploaded files: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Internal server error: " + e.getMessage()));
        }
    }
}
